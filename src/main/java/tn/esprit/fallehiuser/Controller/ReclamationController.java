package tn.esprit.fallehiuser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.DTO.ReclamationDTO;
import tn.esprit.fallehiuser.Sercurity.JWTUtil;
import tn.esprit.fallehiuser.Services.ReclamationService;
import tn.esprit.fallehiuser.model.Reclamation;
import tn.esprit.fallehiuser.model.ReclamationStatus;
import tn.esprit.fallehiuser.model.ReclamationSubject;

import java.util.List;

@RestController
@RequestMapping("/reclamations")
@RequiredArgsConstructor
public class ReclamationController {

    private final JWTUtil jwtUtils;

    private final ReclamationService reclamationService;

    // Endpoint for users to add a complaint with a subject
    @PostMapping("/add")
    public ResponseEntity<String> addReclamation(
            @RequestBody ReclamationDTO reclamationDTO,
            @RequestHeader("Authorization") String authHeader) {

        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String username = jwtUtils.getUsernameFromToken(token); // ✅ Call on the instance

            ReclamationSubject reclamationSubject = ReclamationSubject.valueOf(reclamationDTO.getSubject());
            reclamationService.addReclamation(username, reclamationDTO.getDescription(), reclamationSubject);
            return ResponseEntity.ok("Complaint added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid complaint subject.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }
    // Endpoint for users to get their own complaints (viewing complaint status)
    @GetMapping("/myReclamations")
    public List<Reclamation> getUserReclamations(Authentication authentication) {
        String username = authentication.getName();
        return reclamationService.getReclamationsByUser(username);
    }


    // Endpoint for admin to get all pending complaints
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bystatus")
    public ResponseEntity<List<Reclamation>> getReclamationsByStatus(@RequestParam ReclamationStatus status) {
        List<Reclamation> reclamations = reclamationService.getReclamationsByStatus(status);
        return ResponseEntity.ok(reclamations);
    }


    // Endpoint for admin to treat a reclamation (update status)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/treat/{id}")
    public ResponseEntity<String> treatReclamation(
            @PathVariable Long id,
            @RequestParam ReclamationStatus status) {

        // Only allow transition from PENDING to IN_PROGRESS or IN_PROGRESS to RESOLVED
        Reclamation reclamation = reclamationService.findReclamationById(id);
        if (reclamation == null) {
            return ResponseEntity.notFound().build();
        }

        // Check for valid status transition
        if (reclamation.getStatus() == ReclamationStatus.PENDING && status == ReclamationStatus.IN_PROGRESS) {
            reclamationService.updateReclamationStatus(id, ReclamationStatus.IN_PROGRESS);
            return ResponseEntity.ok("Complaint status updated to IN_PROGRESS.");
        } else if (reclamation.getStatus() == ReclamationStatus.IN_PROGRESS && status == ReclamationStatus.RESOLVED) {
            reclamationService.updateReclamationStatus(id, ReclamationStatus.RESOLVED);
            return ResponseEntity.ok("Complaint status updated to RESOLVED.");
        } else {
            return ResponseEntity.badRequest().body("Invalid status transition.");
        }
    }

    // Endpoint for admin to filter complaints by subject
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<Reclamation>> getReclamationsBySubject(@RequestParam String subject) {
        List<Reclamation> filteredReclamations = reclamationService.getReclamationsBySubject(subject);
        return ResponseEntity.ok(filteredReclamations);
    }

    // New endpoint for admin to filter complaints by date (latest or oldest)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filterByDate")
    public ResponseEntity<List<Reclamation>> getReclamationsByDate(@RequestParam boolean latest) {
        List<Reclamation> reclamationsByDate = reclamationService.getReclamationsByDate(latest);
        return ResponseEntity.ok(reclamationsByDate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        List<Reclamation> reclamations = reclamationService.getAllReclamations();
        return ResponseEntity.ok(reclamations);
    }


}
