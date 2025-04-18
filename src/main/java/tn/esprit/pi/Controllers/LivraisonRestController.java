package tn.esprit.pi.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;
import tn.esprit.pi.Services.LivraisonServiceImpl;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/livraisons")
public class LivraisonRestController {
    @Autowired
    private LivraisonServiceImpl livraisonService;
    @PostMapping("/creer/{idCommande}")
    public ResponseEntity<Map<String, Object>> creerLivraison(@PathVariable Long idCommande) {
        Map<String, Object> response = livraisonService.creerLivraison(idCommande);
        return ResponseEntity.ok(response);

    }
    @PutMapping("/annuler/{id}")
    public ResponseEntity<Livraison> annulerLivraison(@PathVariable Long id) {
        try {
            Livraison livraison = livraisonService.annulerLivraison(id);
            return ResponseEntity.ok(livraison);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @PutMapping("/archiver/{id}")
    public ResponseEntity<String> archiverLivraison(@PathVariable("id") Long id) {
        livraisonService.archiverLivraison(id);
        return ResponseEntity.ok("Livraison " + id + " archivée avec succès.");
    }

    @GetMapping
    public List<Livraison> getAllLivraisons() {
        return livraisonService.getAllLivraisons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livraison> getLivraisonById(@PathVariable("id") Long id) {
        Livraison livraison = livraisonService.getLivraisonById(id);
        return ResponseEntity.ok(livraison);
    }
    @GetMapping("/statut")
    public Map<String, Long> calculerLivraisonsParStatut() {
        return livraisonService.calculerLivraisonsParStatut();
    }

    @GetMapping("/byStatut")
    public ResponseEntity<List<Livraison>> getLivraisonsByStatut(@RequestParam StatutLivraison statut) {
        List<Livraison> livraisons = livraisonService.getLivraisonsByStatut(statut);
        return ResponseEntity.ok(livraisons);
    }
    @GetMapping("/byDelegation")
    public List<Livraison> getLivraisonsByDelegation(@RequestParam String delegation) {
        return livraisonService.getLivraisonsByDelegation(delegation);
    }
    @GetMapping("/client/{clientId}")
    public List<Livraison> getLivraisonsByClientId(@PathVariable Long clientId) {
        return livraisonService.getLivraisonsByClientId(clientId);
    }
    @PostMapping("/search-by-telephone")
    public ResponseEntity<?> getLivraisonsByTelephone(@RequestBody String telephone) {
        if (telephone == null || telephone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Numéro de téléphone requis !");
        }

        // Supprime les espaces autour du numéro
        telephone = telephone.trim();

        List<Livraison> livraisons = livraisonService.getLivraisonsByTelephone(telephone);

        if (!livraisons.isEmpty()) {
            return ResponseEntity.ok(livraisons);
        } else {
            return ResponseEntity.badRequest().body("Aucune livraison trouvée pour ce numéro.");
        }
    }
    @GetMapping("/transporteur/{id}")
    public List<Livraison> getLivraisonsByTransporteurId(@PathVariable Long id) {
        return livraisonService.findByTransporteurId(id);
    }
    @GetMapping("/livraisonEntreDate")
    public List<Livraison> getLivraisonsBetweenDates(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate endDate) {

        return livraisonService.getLivraisonsBetweenDates(startDate, endDate);
    }
    @GetMapping("/livraisonDate")
    public List<Livraison> getLivraisonsByDate(
            @RequestParam("dateLivraison") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate dateLivraison) {

        return livraisonService.getLivraisonsByDate(dateLivraison);
    }
    @PutMapping("/update/{id}")
    public String modifierLivraison(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        StatutLivraison statut = (updates.get("statut") != null) ? StatutLivraison.valueOf(updates.get("statut").toString()) : null;
        LocalDate dateLivraison = null;

        if (updates.get("dateLivraison") != null) {
            try {
                dateLivraison = LocalDate.parse(updates.get("dateLivraison").toString());
            } catch (Exception e) {
                return "Erreur : Format de date invalide. Utilisez 'YYYY-MM-DD'.";
            }
        }

        return livraisonService.modifierLivraison(id, statut, dateLivraison);
    }
    @PutMapping("/changer/{id}")
    public ResponseEntity<String> changerStatut(@PathVariable("id") Long id) {
        livraisonService.changerStatut(id);
        return ResponseEntity.ok("Livraison " + id + "est marqué comme Livrée");
    }

    @PutMapping("/reaffecter/{id}")
    public ResponseEntity<Livraison> reaffecterLivraison(@PathVariable Long id) {
        try {
            Livraison livraison = livraisonService.reaffecterLivraison(id);
            return ResponseEntity.ok(livraison);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PostMapping("/{id}/incident")
    public ResponseEntity<String> signalerIncident(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {
        String type = body.get("typeIncident");
        String description = body.get("descriptionIncident");

        livraisonService.signalerIncidentLivraison(id, type, description);
        return ResponseEntity.ok("Incident signalé avec succès.");
    }
    @GetMapping("/transporteur/{id}/statut")
    public ResponseEntity<List<Livraison>> getByTransporteurAndStatut(
            @PathVariable("id") Long transporteurId,
            @RequestParam("statut") StatutLivraison  statut) {

        List<Livraison> livraisons = livraisonService.getLivraisonsByTransporteurAndStatut(transporteurId, statut);
        return ResponseEntity.ok(livraisons);
    }

    @GetMapping("/byUsername")
    public List<Livraison> getByUsername(@RequestParam("username") String username) {
        return livraisonService.getLivraisonsByTUsername(username);
    }
    @GetMapping("/ByCUsername")
    public List<Livraison> getByCUsername(@RequestParam("username") String cUsername) {
        return livraisonService.getLivraisonsByCUsername(cUsername);
    }
    @GetMapping("/statutTransporteur/{id}")
    public Map<String, Long> calculerStatutTransporteur(@PathVariable("id") Long id) {
        return livraisonService.calculerStatusParLivreur( id);
    }
}
