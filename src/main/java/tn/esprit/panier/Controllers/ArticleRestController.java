package tn.esprit.panier.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.Commande;
import tn.esprit.panier.Services.IArticleServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tn.esprit.panier.Services.ICommandeServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/article")
@Tag(name= "Gestion des articles")
public class ArticleRestController {

    @Autowired
    IArticleServices iArticleServices;
    @Operation(description = "affichage de toutes les Articles")

    @GetMapping("/retrieveAllArticle")
    public List<Article> afficherArticle(){
        return iArticleServices.retrieveAllArticle();

    }
    @Operation(description = "affichage du Article par ID")
    @GetMapping("/retrieveArticle/{id}")
    public Article afficherArticle(@PathVariable("id") long id){
        return iArticleServices.retrieveArticle(id);
    }
    @Operation(description = "ajouter Article")

    @PostMapping("/addArticle")
    public Article ajouterArticle(@RequestBody Article article){
        return iArticleServices.addArticle(article);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Article>> searchArticles(@RequestParam("nom") String nom) {
        List<Article> articles = iArticleServices.searchByNom(nom);
        return ResponseEntity.ok(articles);
    }
    @GetMapping("/searchByPrice")
    public ResponseEntity<List<Article>> searchArticlesPrix(@RequestParam("nom") String nom) {
        // Recherche des articles par nom avec un tri croissant par prix
        List<Article> articles = iArticleServices.searchByNomAndSortByPrixAsc(nom);
        return ResponseEntity.ok(articles);
    }

}
