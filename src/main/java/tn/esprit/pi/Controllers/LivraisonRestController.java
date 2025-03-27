package tn.esprit.pi.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;
import tn.esprit.pi.Services.LivraisonServiceImpl;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
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
/*
    @PostMapping
    public Livraison createLivraison(@RequestBody Livraison livraison) {
        return livraisonService.createLivraison(
                livraison.getProduits().stream().map(p -> p.getId()).toList(),
                livraison.getNomClient(), livraison.getPrenomClient(),
                livraison.getDelegation(), livraison.getAdresseExacte(),
                livraison.getDateLiv(),
                livraison.getNumTel()
        );
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

    @PutMapping("/archiver/{id}")
    public ResponseEntity<String> archiverLivraison(@PathVariable("id") Long id) {
        livraisonService.archiverLivraison(id);
        return ResponseEntity.ok("Livraison " + id + " archivée avec succès.");
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
    @PutMapping("/update/{id}")
    public ResponseEntity<Livraison> updateLivraison(@PathVariable Long id, @RequestBody Livraison updateLivraison) {
        try {
            Livraison updatedLivraison = livraisonService.updateLivraison(id, updateLivraison);
            return ResponseEntity.ok(updatedLivraison); // Retourner la livraison mise à jour
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null); // En cas d'erreur
        }
    }

*/
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

}
