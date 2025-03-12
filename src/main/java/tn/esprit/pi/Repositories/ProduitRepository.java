package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Produit;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
}
