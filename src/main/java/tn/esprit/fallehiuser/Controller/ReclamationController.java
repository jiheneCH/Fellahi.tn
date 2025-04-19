package tn.esprit.fallehiuser.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.DTO.ReclamationDTO;
import tn.esprit.fallehiuser.DTO.ReclamationResponseDTO;
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

    // ========== USER ENDPOINTS ==========

    // Add a complaint
    @PostMapping("/add")
    public ResponseEntity<String> addReclamation(
            @RequestBody ReclamationDTO reclamationDTO,
            @RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.substring(7); // Remove "Bearer "
            String username = jwtUtils.getUsernameFromToken(token);
            ReclamationSubject subject = ReclamationSubject.valueOf(reclamationDTO.getSubject());
            reclamationService.addReclamation(username, reclamationDTO.getDescription(), subject);
            return ResponseEntity.ok("Complaint added successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid complaint subject.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token.");
        }
    }

    // View logged-in user's complaints
    @GetMapping("/myReclamations")
    public List<Reclamation> getUserReclamations(Authentication authentication) {
        String username = authentication.getName();
        return reclamationService.getReclamationsByUser(username);
    }

    // ========== ADMIN ENDPOINTS ==========

    // View all reclamations
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Reclamation>> getAllReclamations() {
        return ResponseEntity.ok(reclamationService.getAllReclamations());
    }

    // View complaints by status
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/bystatus")
    public ResponseEntity<List<Reclamation>> getReclamationsByStatus(@RequestParam ReclamationStatus status) {
        return ResponseEntity.ok(reclamationService.getReclamationsByStatus(status));
    }

    // Filter complaints by subject
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<List<Reclamation>> getReclamationsBySubject(@RequestParam String subject) {
        return ResponseEntity.ok(reclamationService.getReclamationsBySubject(subject));
    }

    // Filter complaints by date (latest or oldest)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/filterByDate")
    public ResponseEntity<List<Reclamation>> getReclamationsByDate(@RequestParam boolean latest) {
        return ResponseEntity.ok(reclamationService.getReclamationsByDate(latest));
    }

    // Treat a complaint (update status and respond)
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/treat/{id}")
    public ResponseEntity<String> respondToReclamation(
            @PathVariable Long id,
            @RequestBody ReclamationResponseDTO responseDTO,
            Authentication authentication) {

        Reclamation reclamation = reclamationService.findReclamationById(id);
        if (reclamation == null) {
            return ResponseEntity.notFound().build();
        }

        ReclamationStatus currentStatus = reclamation.getStatus();
        ReclamationStatus newStatus;

        try {
            newStatus = ReclamationStatus.valueOf(responseDTO.getUpdatedStatus());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status provided.");
        }

        // Validate status transition
        boolean validTransition = (currentStatus == ReclamationStatus.PENDING && newStatus == ReclamationStatus.IN_PROGRESS)
                || (currentStatus == ReclamationStatus.IN_PROGRESS && newStatus == ReclamationStatus.RESOLVED);

        if (!validTransition) {
            return ResponseEntity.badRequest().body("Invalid status transition.");
        }

        String adminUsername = authentication.getName();
        reclamationService.respondToReclamation(id, adminUsername, responseDTO.getResponseMessage(), newStatus);
        return ResponseEntity.ok("Reclamation treated successfully with response.");
    }
}
