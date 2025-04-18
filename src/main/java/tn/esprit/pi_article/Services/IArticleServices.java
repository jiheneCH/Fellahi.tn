package tn.esprit.pi_article.Services;

import org.springframework.data.domain.Page;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.StatusAgri;
import tn.esprit.pi_article.Entities.TypeProduit;

import java.util.List;
import java.util.Map;

public interface IArticleServices {


    public List<Article> retriveAllArticle();
    public Article retriveArticle (long idArticle);
   // public Article addArticle (Article article);
    public Article updateArticle (Article article);
   // public void deleteArticle (long idArticle);
    void archiverArticle(Long id);
    boolean existsByNom(String nom);



    public Article genererPackSiNecessaire() ;
    public List<Article> verifierSeuilCritiqueEtEnvoyerAlerte();

    public List<Article> rechercherArticles(String nom, StatusAgri statusAgri, TypeProduit typeProduit);
    public List<Article> getArticlesTendanceeee() ;
    public List<Object[]> getAjoutsParJour() ;
    public Double getChiffreAffairesTotal() ;
    public Map<String, Object> getChiffreAffairesParArticle() ;
    public Double getQuantiteTotalVendue() ;
    public List<Map<String, Object>> getChiffreAffairesParCategorie() ;
    public List<Map<String, Object>> getAjoutsParMois() ;




    //public List<Article> getArticlesByUtilisateurId(Long id) ;
   // public Article ajouterArticleparuser(Long id, Article article) ;









    public List<Article> getArticlesParUtilisateur(Long id) ;
    public Article ajouterArticlePourUtilisateur(Long idUtilisateur, Article article) ;
    public Article genererPackSiNecessaireeee(Long idUtilisateur) ;
 public List<Article> getArticlesLowStockAndOutOfStock() ;
 public Page<Article> getArticles(int page, int size) ;

 }

