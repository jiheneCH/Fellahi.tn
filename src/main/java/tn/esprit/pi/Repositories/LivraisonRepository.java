package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Livraison;

import java.util.List;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>{
    List<Livraison> findByArchivedFalse();
}
