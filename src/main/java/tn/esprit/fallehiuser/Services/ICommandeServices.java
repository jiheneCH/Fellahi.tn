package tn.esprit.fallehiuser.Services;


import tn.esprit.fallehiuser.model.Commande;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Map;

public interface ICommandeServices {
    public List<Commande> retrieveAllCommande();
    public Commande retrieveCommande(long idCommande);
    public Commande updateCommande(Commande commande);
    public void deleteCommande(long idCommande);
    public Commande addCommande(Commande commande);
    public void calculerPrixTotalCommande(Commande commande);
    public void addArticleToCommande(long idArticle, int quantite, long userId);
    public void updateArticleInCommande(long idCommande, long idArticle, int nouvelleQuantite);
    public void deleteArticleInCommande(Long commandeId, Long articleId);
    public String createCommande(long userId);
    public void validerCommande(Long commandeId);
    public void appliquerReductionParFidelite(Long commandeId, int pointsFideliteUtilises);
    public Commande getCommandeEnCours(Long userId);
    public User getUserById(Long id);
    public void validerCommandeONSITE(Long commandeId);
    public void validerCommandeONLINE(Long commandeId);
    public Long getUserIdByName(String name);
    public void cancelledCommande(Long commandeId);
    public List<Commande> getCommandesByClientId(Long clientId);
    public String changerStatutSiCancelled(Long idCommande);
    public Map<String, Object> getCommandeStats();
}
