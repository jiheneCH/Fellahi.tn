package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pi_article.Entities.Commande;
import tn.esprit.pi_article.Entities.StatusCommande;

import java.util.List;

public interface CommandeRepository  extends JpaRepository<Commande, Long> {
    List<Commande> findByStatus(StatusCommande status);

}
