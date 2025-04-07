package tn.esprit.panier.Controllers;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.panier.Entities.Commande;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Services.ICommandeServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/commande")
@Tag(name= "Gestion des commandes")
public class CommandeRestController {
    @Autowired
    ICommandeServices iCommandeServices;
    @Operation(description = "affichage de toutes les Commandes")

    @GetMapping("/retrieveAllCommande")
    public List<Commande> afficherCommande(){
        return iCommandeServices.retrieveAllCommande();

    }
    @Operation(description = "affichage du Produit par ID")
    @GetMapping("/retrieveCommande/{idCommande}")
    public Commande afficherCommande(@PathVariable("idCommande") long idCommande){
        return iCommandeServices.retrieveCommande(idCommande);
    }
    @Operation(description = "ajouter une Commande")

    @PostMapping("/addCommande")
    public Commande ajouterCommande(@RequestBody Commande commande){
        return iCommandeServices.addCommande(commande);
    }
    @Operation(description = "modifier une Commande")
    @PutMapping("/updateCommande")
    public Commande updateCommande(@RequestBody Commande commande){
        return iCommandeServices.updateCommande(commande);
    }
    @Operation(description = "supprimer une Commande")
    @DeleteMapping("/deleteCommande/{id}")
    public ResponseEntity<String> deleteCommande(@PathVariable Long id) {
        try {
            iCommandeServices.deleteCommande(id);
            return ResponseEntity.ok("Commande supprimée avec succès.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de la suppression de la commande.");
        }
    }

    @PostMapping("/addArticleToCommande")
    public String addArticleToCommande(@RequestParam int idCommande, @RequestParam int idArticle, @RequestParam int quantite) {
        iCommandeServices.addArticleToCommande(idCommande, idArticle, quantite);
        return "Article ajouté/quantité mise à jour avec succès!";
    }
    @PutMapping("/updateArticleInCommande")
    public String updateArticleInCommande(
            @RequestParam long idCommande,
            @RequestParam long idArticle,
            @RequestParam int nouvelleQuantite) {

        try {
            // Appel à la méthode du service pour modifier l'article dans la commande
            iCommandeServices.updateArticleInCommande(idCommande, idArticle, nouvelleQuantite);

            // Réponse positive si la modification a réussi
            return "Article modifié/quantité mise à jour avec succès dans la commande.";

        } catch (IllegalArgumentException e) {
            // En cas d'erreur, retour d'un message d'erreur
            return "Erreur : " + e.getMessage();
        } catch (Exception e) {
            // En cas d'exception générale
            return "Erreur inattendue : " + e.getMessage();
        }
    }
    @DeleteMapping("/deleteArticleInCommande")
    public String deleteArticleInCommande(@RequestParam long idCommande, @RequestParam long idArticle) {
        try {
            // Appel du service pour supprimer l'article de la commande
            iCommandeServices.deleteArticleInCommande(idCommande, idArticle);

            // Retourner un message de succès
            return "Article supprimé et prix total mis à jour avec succès!";
        } catch (IllegalStateException e) {
            // Si le statut de la commande empêche la suppression
            return "Erreur : " + e.getMessage();
        } catch (Exception e) {
            // Si une erreur générale se produit
            return "Une erreur est survenue : " + e.getMessage();
        }
    }
    @PostMapping("/create/{clientId}")  // POST pour créer une commande
    public ResponseEntity<String> createCommande(@PathVariable long clientId) {
        // Appel du service pour créer la commande
        String result = iCommandeServices.createCommande(clientId);

        // Retourner la réponse HTTP
        if (result.contains("Erreur")) {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST); // Si erreur, code 400
        } else {
            return new ResponseEntity<>(result, HttpStatus.CREATED); // Si succès, code 201
        }
    }

}

