package tn.esprit.pi_article.Controller;


import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Services.ArticleServicelmpl;
import tn.esprit.pi_article.Services.IArticleServices;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/Article")
@Validated

public class ArticleRestController {
    @Autowired
    IArticleServices iArticleServices;
/*
    @GetMapping("/retriveAllArticle")
    public List<Article> afficherAllArticle(){
        return iArticleServices.retriveAllArticle();
    }
    @GetMapping("/retrieveArticle/{idA}")
    public Article afficherArticle(@PathVariable("idA") long idArticle){
        return iArticleServices.retriveArticle(idArticle);
    }

    @PostMapping("/addArtice")
    public Article ajouterArticle (@RequestBody Article article){
        return iArticleServices.addArticle(article);
    }

    @PutMapping("/archiverArticle/{id}")
    public ResponseEntity<String> archiverArticle(@PathVariable("id") Long id) {
        iArticleServices.archiverArticle(id);
        return ResponseEntity.ok("Stock " + id + " archivée avec succès.");
    }



@PostMapping("/addArtice")
public ResponseEntity<?> ajouterArticle(@Valid @RequestBody Article article) {
    Article newArticle = iArticleServices.addArticle(article);
    return ResponseEntity.ok(newArticle);
}

 */
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


 /*   @PostMapping("/addArticle")
    public ResponseEntity<?> ajouterArticle(@Valid @RequestBody Article article) {
        try {
            Article savedArticle = iArticleServices.addArticle(article);
            return ResponseEntity.ok(savedArticle);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }*/
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
    public Article updateArticle(@RequestBody Article article) {
        return iArticleServices.updateArticle(article);
    }


}
