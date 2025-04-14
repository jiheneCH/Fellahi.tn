package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.utilisateur;

import java.util.Optional;

public interface UtilisateurRepository extends JpaRepository<utilisateur, Long > {
    Optional<utilisateur> findById(Long id);

}
