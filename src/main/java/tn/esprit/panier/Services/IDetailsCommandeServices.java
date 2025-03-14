package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Commande;
import tn.esprit.panier.Entities.DetailsCommande;

import java.util.List;

public interface IDetailsCommandeServices {
    public List<DetailsCommande> retrieveAllDetailsCommande();
    public DetailsCommande retrieveDetailsCommande(long idDetailsCommande);
    public DetailsCommande updateDetailsCommande(DetailsCommande detailsCommande);
    public void deleteDetailsCommande(long idDetailsCommande);
    public DetailsCommande addDetailsCommande(Commande commande);
}
