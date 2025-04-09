package tn.esprit.pi_article.Services;

import tn.esprit.pi_article.Entities.utilisateur;

import java.util.List;

public interface IUtilisateurServices {
    public utilisateur ajouterUtilisateur(utilisateur utilisateur) ;
    public List<utilisateur> getAllUtilisateurs() ;
    }
