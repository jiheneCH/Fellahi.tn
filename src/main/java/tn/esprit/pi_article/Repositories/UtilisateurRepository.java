package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.utilisateur;

public interface UtilisateurRepository extends JpaRepository<utilisateur, Long > {
}
