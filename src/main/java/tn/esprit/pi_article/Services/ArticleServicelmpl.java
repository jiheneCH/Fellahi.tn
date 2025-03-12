package tn.esprit.pi_article.Services;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.Status;
import tn.esprit.pi_article.Repositories.ArticleRepository;

import java.lang.module.ResolutionException;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class ArticleServicelmpl implements IArticleServices {
    @Autowired
    ArticleRepository articleRepository;

    @Override
    public List<Article> retriveAllArticle() {
        return articleRepository.findByArchivedFalse();
    }

    @Override
    public Article retriveArticle(long idArticle) {

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        if (article.getStatus() == Status.archive) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access to this article is forbidden");
        }

        return article;}
    /*

    @Override
    public Article addArticle(Article article) {
        return articleRepository.save(article);    }*/

    @Override
    public Article updateArticle(Article article) {
        // Vérifier si l'article existe dans la base de données
        Article existingArticle = articleRepository.findById(article.getIdArticle())
                .orElseThrow(() -> new EntityNotFoundException("Article non trouvé avec l'ID : " + article.getIdArticle()));

        // Garder le nom et la référence d'origine
        article.setNom(existingArticle.getNom());
        article.setReference(existingArticle.getReference());

        // Enregistrer les modifications
        return articleRepository.save(article);
    }


    @Override
    public void deleteArticle(long idArticle) {
        articleRepository.deleteById(idArticle);



    }
/*
    @Override
    public void archiverArticle(Long id) {
        Article article = articleRepository.findById(id).get();
        article.setArchived(true);
        article.setStatus(Status.archive);
        articleRepository.save(article);
    }*/
@Override
@Transactional
public void archiverArticle(Long id) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("L'article avec l'ID " + id + " n'existe pas."));

    if (article.isArchived()) {
        throw new RuntimeException("L'article avec l'ID " + id + " est déjà archivé.");
    }

    article.setArchived(true);
    article.setStatus(Status.archive);

    try {
        articleRepository.save(article);
    } catch (Exception e) {
        throw new RuntimeException("Erreur lors de l'archivage de l'article : " + e.getMessage());
    }
}



    @Override
    public boolean existsByNom(String nom) {
        return articleRepository.existsByNom(nom);
    }

 /*   @Override
    public Article addArticle(Article article) {
        if (existsByNom(article.getNom())) {
            throw new RuntimeException("Un article avec ce nom existe déjà.");
        }
        return articleRepository.save(article);
    }*/
 @Override
 public Article addArticle(Article article) {
     // Vérification si le nom existe déjà
     if (existsByNom(article.getNom())) {
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Un article avec ce nom existe déjà.");
     }

     // Vérification des quantités
     if (article.getQuantiteDisponible() == null || article.getQuantiteDisponible() < 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité disponible ne peut pas être négative ou nulle.");
     }

     if (article.getQuantiteVendue() == null || article.getQuantiteVendue() < 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas être négative.");
     }

     if (article.getQuantiteVendue() > article.getQuantiteDisponible()) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas dépasser la quantité disponible.");
     }

     // Sauvegarde de l'article après validation
     return articleRepository.save(article);
 }





}
