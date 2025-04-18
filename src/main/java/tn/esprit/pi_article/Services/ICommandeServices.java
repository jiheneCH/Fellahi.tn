package tn.esprit.pi_article.Services;

import tn.esprit.pi_article.Entities.Commande;

public interface ICommandeServices {
    public void traiterCommande(Long idCommande) ;
    public void creerCommande(Commande commande) ;
    }
