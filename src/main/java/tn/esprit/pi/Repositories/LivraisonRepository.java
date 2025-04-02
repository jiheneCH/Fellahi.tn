package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Client;
import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>{
    Optional<Livraison> findById(Long id);
    List<Livraison> findByArchivedFalse();
    long countByStatut(StatutLivraison statut);
    List<Livraison> findByStatut(StatutLivraison statut);
    List<Livraison> findByClient_Delegation(String delegation);
    List<Livraison> findByTransporteur_Id(Long transporteurId);
    List<Livraison> findByDateLivraisonBetween(LocalDate startDate, LocalDate endDate);
    List<Livraison> findByDateLivraison(LocalDate dateLivraison);

    List<Livraison> findByClient_Id(Long clientId);

}
