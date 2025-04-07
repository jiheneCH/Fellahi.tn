package tn.esprit.panier.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.panier.Entities.DetailsCommande;

public interface DetailsCommandeRepository extends JpaRepository<DetailsCommande, Long> {
}
