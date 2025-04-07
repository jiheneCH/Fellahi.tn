package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Article;

import java.util.List;

public interface IArticleServices {
    public List<Article> retrieveAllArticle();
    public Article retrieveArticle(long idArticle);
    public Article addArticle(Article article);
    public List<Article> searchByNom(String nom);
    public List<Article> searchByNomAndSortByPrixAsc(String nom);
}
