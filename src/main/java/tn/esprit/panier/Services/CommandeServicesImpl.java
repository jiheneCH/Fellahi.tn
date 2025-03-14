package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Commande;

import java.util.List;

public class CommandeServicesImpl implements ICommandeServices{
    @Override
    public List<Commande> retrieveAllCommande() {
        return List.of();
    }

    @Override
    public Commande retrieveCommande(long idCommande) {
        return null;
    }

    @Override
    public Commande updateCommande(Commande commande) {
        return null;
    }

    @Override
    public void deleteCommande(long idCommande) {

    }

    @Override
    public Commande addCommande(Commande commande, long idCommande) {
        return null;
    }
}
