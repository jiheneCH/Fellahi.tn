package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Transporteur;

@Repository
public interface TransporteurRepository extends JpaRepository<Transporteur, Long> {
    @Query("SELECT t FROM Transporteur t WHERE t.delegation = :delegation ORDER BY t.nbLivraisons ASC LIMIT 1")
    Transporteur findLivreurWithLeastDeliveries(String delegation);
}
