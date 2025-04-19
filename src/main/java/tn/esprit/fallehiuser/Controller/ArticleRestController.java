package tn.esprit.fallehiuser.Controller;
import org.springframework.data.domain.Page;


import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.model.Article;
import tn.esprit.fallehiuser.model.StatusAgri;
import tn.esprit.fallehiuser.model.TypeProduit;
import tn.esprit.fallehiuser.Services.IArticleServices;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200")


@RestController
@AllArgsConstructor
@RequestMapping("/PI_article")
@Validated


public class ArticleRestController {
    @Autowired
    IArticleServices iArticleServices;






    /*@GetMapping("/retriveAllArticle")
    public List<Article> afficherAllArticle(){
        return iArticleServices.retriveAllArticle();
    }
    */
/*
    @GetMapping("/retrieveArticle/{idA}")
    public Article afficherArticle(@PathVariable("idA") long idArticle){
        return iArticleServices.retriveArticle(idArticle);
    }

    /*
    @PostMapping("/addArtice")
    public Article ajouterArticle (@RequestBody Article article){
        return iArticleServices.addArticle(article);
    }


    @PutMapping("/archiverArticle/{id}")
    public ResponseEntity<String> archiverArticle(@PathVariable("id") Long id) {
        iArticleServices.archiverArticle(id);
        return ResponseEntity.ok("Stock " + id + " archivée avec succès.");
    }

/*

@PostMapping("/addArtice")
public ResponseEntity<?> ajouterArticle(@Valid @RequestBody Article article) {
    Article newArticle = iArticleServices.addArticle(article);
    return ResponseEntity.ok(newArticle);
}
*/






    ///////////////

    @GetMapping("/retriveAllArticle")
    public List<Article> afficherAllArticle(){
        return iArticleServices.retriveAllArticle();
    }
    @GetMapping("/retrieveArticle/{idA}")
    public ResponseEntity<?> afficherArticle(@PathVariable("idA") Long idArticle) {
        Optional<Article> article = Optional.ofNullable(iArticleServices.retriveArticle(idArticle));

        if (article.isPresent()) {
            return ResponseEntity.ok(article.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Article avec l'ID " + idArticle + " non trouvé.");
        }
    }












    ////////////////////////////////////

 /*   @PostMapping("/addArticle")
    public ResponseEntity<?> ajouterArticle(@Valid @RequestBody Article article) {
        try {
            Article savedArticle = iArticleServices.addArticle(article);
            return ResponseEntity.ok(savedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/






    ////////////////////////////
    /*
 @PostMapping("/addArticle")
 public ResponseEntity<?> ajouterArticle (@Valid @RequestBody Article article) {
     try {
         // Ajouter l'article après validation
         Article savedArticle = iArticleServices.addArticle(article);
         return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
     } catch (ResponseStatusException e) {
         // Gestion des erreurs spécifiques avec ResponseStatusException
         return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
     } catch (RuntimeException e) {
         // Gestion des autres erreurs
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     }
 }



*/

@PutMapping("/archiverArticle/{id}")
public ResponseEntity<?> archiverArticle(@PathVariable("id") Long id) {
    try {
        iArticleServices.archiverArticle(id);
        return ResponseEntity.ok("L'article " + id + " a été archivé avec succès.");
    } catch (RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
    }
}
    @PutMapping("/updateArticle")
    public ResponseEntity<?> updateArticle(@RequestBody Article article) {
        try {
            // Vérification de la requête reçue (debug)
            System.out.println("Requête reçue pour update : " + article);

            // Appel du service pour mettre à jour l'article
            Article updatedArticle = iArticleServices.updateArticle(article);

            return ResponseEntity.ok(updatedArticle);
        } catch (RuntimeException e) {
            // Gère les erreurs métier (ex : article non trouvé, nom déjà existant...)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Gère toute autre erreur (ex : problème de connexion à la base)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur interne : " + e.getMessage());
        }
    }





    @PostMapping("/genererPack")
    public ResponseEntity<Article> genererPack() {
        Article pack = iArticleServices.genererPackSiNecessaire();
        return ResponseEntity.ok(pack);
    }
    @GetMapping("/3-lowstock")
    public ResponseEntity<List<Article>> testSeuils() {
        List<Article> articlesFaibles = iArticleServices.verifierSeuilCritiqueEtEnvoyerAlerte();
        return ResponseEntity.ok(articlesFaibles);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(
            @RequestParam(required = false) String nom,
            @RequestParam(required = false) StatusAgri statusAgri,
            @RequestParam(required = false) TypeProduit typeProduit
    ) {
        List<Article> articles = iArticleServices.rechercherArticles(nom, statusAgri, typeProduit);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/tendanceeee")
    public ResponseEntity<List<Article>> getArticlesTendanceee() {
        return ResponseEntity.ok(iArticleServices.getArticlesTendanceeee());
    }
    @GetMapping(value = "/articles/{id}/qrcode", produces = MediaType.IMAGE_PNG_VALUE)
    public byte[] getArticleQRCode(@PathVariable Long id) throws Exception {
        Article article = iArticleServices.retriveArticle(id);
        String qrContent = "ID:" + article.getIdArticle() + "|Nom:" + article.getNom();

        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(qrContent, BarcodeFormat.QR_CODE, 200, 200);

        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
        return pngOutputStream.toByteArray();
    }
    @GetMapping("/ajouts-par-jour")
    public ResponseEntity<List<Object[]>> getAjoutsParJour() {
        List<Object[]> ajoutsParJour = iArticleServices.getAjoutsParJour();

        if (ajoutsParJour.isEmpty()) {
            return ResponseEntity.noContent().build(); // Retourne 204 si aucun ajout n'est trouvé
        }

        return ResponseEntity.ok(ajoutsParJour); // Retourne les ajouts par jour
    }
    @GetMapping("/chiffre-affaires")
    public ResponseEntity<Double> getChiffreAffairesTotal() {
        Double chiffreAffaires = iArticleServices.getChiffreAffairesTotal();
        return ResponseEntity.ok(chiffreAffaires);
    }
    @GetMapping("/chiffre-affaires-article")
    public ResponseEntity<Map<String, Object>> getChiffreAffairesParArticle() {
        Map<String, Object> response = iArticleServices.getChiffreAffairesParArticle();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/quantite-total-vendue")
    public Double getQuantiteTotalVendue() {
        return iArticleServices.getQuantiteTotalVendue();
    }
    @GetMapping("/chiffre-affaires-par-categorie")
    public List<Map<String, Object>> getChiffreAffairesParCategorie() {
        return iArticleServices.getChiffreAffairesParCategorie();
    }
    @GetMapping("/ajouts-par-mois")
    public List<Map<String, Object>> getAjoutsParMois() {
        return iArticleServices.getAjoutsParMois();
    }
    /*
    @GetMapping("/mes-articles/{id}")
    public ResponseEntity<List<Article>> getMesArticles(@PathVariable Long id) {
        return ResponseEntity.ok(iArticleServices.getArticlesByUtilisateurId(id));
    }
    // Récupérer les articles de l'agriculteur
    @GetMapping("/afficherproduitagriculteur/{id}")
    public List<Article> getArticlesParAgriculteur(@PathVariable Long id) {
        return iArticleServices.getArticlesParUtilisateur(id);
    }

    // Ajouter un article pour l'agriculteur
    @PostMapping("/ajouterproduitagriculteur/{id}")
    public Article ajouterArticle(@PathVariable Long id, @RequestBody Article article) {
        return iArticleServices.ajouterArticleparuser(id, article);
    }
*/


    @GetMapping("/afficherproduitagriculteur/{id}")
    public ResponseEntity<List<Article>> getArticlesParUtilisateur(@PathVariable Long id) {
        try {
            // Appel du service pour obtenir les articles de l'utilisateur
            List<Article> articles = iArticleServices.getArticlesParUtilisateur(id);
            return ResponseEntity.ok(articles);  // Retourne une réponse 200 avec la liste des articles
        } catch (RuntimeException e) {
            // Si un utilisateur n'est pas trouvé ou si son rôle n'est pas FARMER
            return ResponseEntity.status(404).body(null);  // Retourne une réponse 404 si l'utilisateur est introuvable
        }
    }
    @PostMapping("/ajouterproduitagriculteur/{idUtilisateur}")
    public ResponseEntity<Article> ajouterArticlePourUtilisateur(
            @PathVariable Long idUtilisateur,
            @RequestBody Article article) {

        try {
            // Appel du service pour ajouter l'article
            Article articleAjoute = iArticleServices.ajouterArticlePourUtilisateur(idUtilisateur, article);

            // Retourner l'article ajouté avec un code HTTP 201 (création réussie)
            return new ResponseEntity<>(articleAjoute, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Si l'utilisateur n'est pas un agriculteur ou s'il y a une erreur
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 - Mauvaise demande
        }
    }
    @PostMapping("/generer-pack/{idUtilisateur}")
    public ResponseEntity<Article> genererPack(@PathVariable Long idUtilisateur) {
        try {
            // Appeler la méthode service pour générer un pack
            Article pack = iArticleServices.genererPackSiNecessaireeee(idUtilisateur);

            // Retourner une réponse avec le pack généré
            return ResponseEntity.status(HttpStatus.CREATED).body(pack);
        } catch (RuntimeException e) {
            // Si une erreur se produit, retourner une réponse avec un message d'erreur
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
    @GetMapping("/articles/low-stock-out-of-stock")
    public List<Article> getLowStockAndOutOfStockArticles() {
        return iArticleServices.getArticlesLowStockAndOutOfStock();
    }
    @GetMapping("/articles")
    public Page<Article> getArticles(@RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "10") int size) {
        // Appelle le service pour obtenir les articles paginés
        return iArticleServices.getArticles(page, size);
    }
}
