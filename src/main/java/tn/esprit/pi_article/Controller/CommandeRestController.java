package tn.esprit.pi_article.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pi_article.Entities.Commande;
import tn.esprit.pi_article.Services.CommandeServiceImpl;
@AllArgsConstructor
@RestController
@RequestMapping("/api/commandes")
public class CommandeRestController {

    @Autowired
    private CommandeServiceImpl commandeService;  // Injection de CommandeServiceImpl

    @PostMapping("/traiter/{idCommande}")
    public ResponseEntity<String> traiterCommande(@PathVariable Long idCommande) {
        try {
            commandeService.traiterCommande(idCommande);
            return ResponseEntity.ok("Commande traitée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
    @PostMapping("/creer")
    public ResponseEntity<String> creerCommande(@RequestBody Commande commande) {
        try {
            // Vous pouvez ici appeler un service pour sauvegarder la commande dans la base de données
            commandeService.creerCommande(commande);
            return ResponseEntity.ok("Commande créée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
