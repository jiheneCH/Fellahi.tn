package tn.esprit.pi_article.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.utilisateur;
import tn.esprit.pi_article.Repositories.ArticleRepository;
import tn.esprit.pi_article.Repositories.UtilisateurRepository;

import java.util.List;

@Service

public class UtilisateurServicelmpl implements  IUtilisateurServices{
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private ArticleRepository articleRepository;

    // Associer plusieurs articles à un utilisateur

    public utilisateur ajouterUtilisateur(utilisateur utilisateur) {
        return utilisateurRepository.save(utilisateur);
    }

    public List<utilisateur> getAllUtilisateurs() {
        return utilisateurRepository.findAll();
    }
}
