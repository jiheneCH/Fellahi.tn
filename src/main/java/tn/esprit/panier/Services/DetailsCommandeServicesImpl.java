package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Commande;
import tn.esprit.panier.Entities.DetailsCommande;

import java.util.List;

public class DetailsCommandeServicesImpl implements IDetailsCommandeServices{
    @Override
    public List<DetailsCommande> retrieveAllDetailsCommande() {
        return List.of();
    }

    @Override
    public DetailsCommande retrieveDetailsCommande(long idDetailsCommande) {
        return null;
    }

    @Override
    public DetailsCommande updateDetailsCommande(DetailsCommande detailsCommande) {
        return null;
    }

    @Override
    public void deleteDetailsCommande(long idDetailsCommande) {

    }

    @Override
    public DetailsCommande addDetailsCommande(Commande commande) {
        return null;
    }
}
