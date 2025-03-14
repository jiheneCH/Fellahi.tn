package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Commande;

import java.util.List;

public interface ICommandeServices {
    public List<Commande> retrieveAllCommande();
    public Commande retrieveCommande(long idCommande);
    public Commande updateCommande(Commande commande);
    public void deleteCommande(long idCommande);
    public Commande addCommande(Commande commande, long idCommande);
}
