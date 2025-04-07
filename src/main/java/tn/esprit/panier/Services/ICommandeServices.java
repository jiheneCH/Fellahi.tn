package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.Commande;

import java.util.List;

public interface ICommandeServices {
    public List<Commande> retrieveAllCommande();
    public Commande retrieveCommande(long idCommande);
    public Commande updateCommande(Commande commande);
    public void deleteCommande(long idCommande);
    public Commande addCommande(Commande commande);
    public void calculerPrixTotalCommande(Commande commande);
    public void addArticleToCommande(long idCommande, long idArticle, int quantite);
    public void updateArticleInCommande(long idCommande, long idArticle, int nouvelleQuantite);
    public void deleteArticleInCommande(long idCommande, long idArticle);
    public String createCommande(long clientId);
}
