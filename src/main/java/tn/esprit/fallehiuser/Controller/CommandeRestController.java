package tn.esprit.fallehiuser.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.Services.ICommandeServices;
import tn.esprit.fallehiuser.model.Commande;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200/")
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
    @Operation(description = "affichage du Commande par ID")
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
    public ResponseEntity<String> addArticleToCommande(
            @RequestParam int idClient,
            @RequestParam int idArticle,
            @RequestParam int quantite) {
        try {
            iCommandeServices.addArticleToCommande(idArticle, quantite, idClient);
            return ResponseEntity.ok("✅ Article ajouté ou quantité mise à jour avec succès !");
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().body("❌ Erreur : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("❗ Erreur serveur : " + e.getMessage());
        }
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
    @PutMapping("/validerCommande/{id}")
    public ResponseEntity<String> validerCommande(@PathVariable Long id) {
        try {
            iCommandeServices.validerCommande(id);
            return ResponseEntity.ok("Commande validée avec succès.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    @PostMapping("/reduction/{commandeId}")
    public ResponseEntity<String> appliquerReduction(
            @PathVariable("commandeId") Long commandeId,
            @RequestParam("pointsFideliteUtilises") int pointsFideliteUtilises) {

        try {
            // Appel du service pour appliquer la réduction
            iCommandeServices.appliquerReductionParFidelite(commandeId, pointsFideliteUtilises);

            return ResponseEntity.ok("Réduction appliquée avec succès.");
        } catch (IllegalStateException e) {
            // Gestion des erreurs liées au statut de la commande
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            // Gestion des erreurs liées aux points de fidélité
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Gestion des erreurs génériques
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur est survenue.");
        }
    }
    @GetMapping("/{clientId}/en-cours")
    public ResponseEntity<?> getCommandeEnCours(@PathVariable Long clientId) {
        try {
            Commande commande = iCommandeServices.getCommandeEnCours(clientId);
            return ResponseEntity.ok(commande);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur : " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<User> getClient(@PathVariable Long id) {
        User user  = iCommandeServices.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/valider-onsite/{id}")
    public ResponseEntity<String> validerCommandeONSITE(@PathVariable("id") Long commandeId) {
        try {
            iCommandeServices.validerCommandeONSITE(commandeId);  // Appel à la méthode de validation de commande
            return ResponseEntity.ok("Commande validée et SMS envoyé !");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur : " + e.getMessage());
        }
    }

    /*@PutMapping("/valider-online/{id}")
    public ResponseEntity<String> validerCommandeONLINE(@PathVariable("id") Long commandeId) {
        try {
            iCommandeServices.validerCommandeONLINE(commandeId);
            return ResponseEntity.ok("Commande validée avec succès en mode 'onLINE'.");
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur : " + e.getMessage());
        }
    }*/



   /* @PostMapping("/create-payment-intent")
    public ResponseEntity<?> createPaymentIntent(@RequestBody PaymentRequest request) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("amount", request.getAmount());
            params.put("currency", request.getCurrency());
            params.put("payment_method_types", request.getPaymentMethodTypes());

            PaymentIntent intent = stripeService.createPaymentIntent(params);
            return ResponseEntity.ok(Map.of("clientSecret", intent.getClientSecret()));
        } catch (Exception e) {
            e.printStackTrace(); // 👈 utile pour afficher l’erreur côté serveur
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }*/




    @GetMapping("/client/id")
    public Long getClientId(@RequestParam String name) {
        return iCommandeServices.getUserIdByName(name);
    }

    /* @PostMapping("/send")
     public ResponseEntity<String> sendSms(@RequestParam String to, @RequestParam String message) {
         twilioService.sendSms(to, message);
         return ResponseEntity.ok("Message envoyé !");
     }*/
    @PutMapping("/annulerCommande/{id}")
    public ResponseEntity<String> annulerCommande(@PathVariable("id") Long commandeId) {
        try {
            iCommandeServices.cancelledCommande(commandeId);
            return ResponseEntity.ok("Commande annulée avec succès.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'annulation de la commande.");
        }
    }
    @GetMapping("/client/{idClient}")
    public List<Commande> getCommandesByClientId(@PathVariable Long idClient) {
        return iCommandeServices.getCommandesByClientId(idClient);
    }
    @PutMapping("/commandes/status/{id}")
    public ResponseEntity<String> changerStatut(@PathVariable Long id) {
        String message = iCommandeServices.changerStatutSiCancelled(id);
        if (message.contains("changé")) {
            return ResponseEntity.ok(message);
        } else if (message.contains("non trouvée")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        } else {
            return ResponseEntity.badRequest().body(message);
        }
    }
    @GetMapping("/commande-stats")
    public Map<String, Object> getCommandeStats() {
        return iCommandeServices.getCommandeStats();
    }

    private final Map<String, List<String>> recettes = Map.of(
            "couscous", List.of("carottes", "navets", "pommes de terre", "courgettes", "semoule", "pois chiches"),
            "tajine", List.of("poulet", "pommes de terre", "olives", "citron", "épices"),
            "salade mechouia", List.of("poivrons", "tomates", "ail", "huile d'olive"),
            "spaghetti bolognaise", List.of("spaghetti", "viande hachée", "tomates", "oignon", "ail", "huile d'olive", "parmesan"),
            "pizza margherita", List.of("pâte à pizza", "tomates", "mozzarella", "basilic", "huile d'olive")


    );

    @GetMapping("/suggest")
    public ResponseEntity<List<String>> getSuggestions(@RequestParam String repas) {
        String req = repas.toLowerCase().trim();

        if (recettes.containsKey(req)) {
            return ResponseEntity.ok(recettes.get(req));
        } else {
            // Recherche partielle (ex: "je veux faire un couscous")
            Optional<List<String>> suggestion = recettes.entrySet().stream()
                    .filter(entry -> req.contains(entry.getKey()))
                    .map(Map.Entry::getValue)
                    .findFirst();

            return suggestion.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.ok(List.of("Désolé, je ne connais pas encore cette recette.")));
        }
    }


}


