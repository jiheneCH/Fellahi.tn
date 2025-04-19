package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.fallehiuser.model.Commande;

import java.util.Optional;

@Repository
public interface CommandeRepository  extends JpaRepository<Commande, Long> {
    Optional<Commande> findById(Long idCommande);
}
