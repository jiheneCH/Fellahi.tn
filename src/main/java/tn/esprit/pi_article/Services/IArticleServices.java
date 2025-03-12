package tn.esprit.pi_article.Services;

import tn.esprit.pi_article.Entities.Article;

import java.util.List;

public interface IArticleServices {

    public List<Article> retriveAllArticle();
    public Article retriveArticle (long idArticle);
    public Article addArticle (Article article);
    public Article updateArticle (Article article);
    public void deleteArticle (long idArticle);
    void archiverArticle(Long id);
    boolean existsByNom(String nom);


}

