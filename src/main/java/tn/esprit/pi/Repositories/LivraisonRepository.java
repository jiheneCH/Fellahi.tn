package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;
import tn.esprit.pi.Entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>{
    Optional<Livraison> findById(Long id);
    List<Livraison> findByArchivedFalse();
    long countByStatut(StatutLivraison statut);

    long countByStatutAndTransporteur_Id(StatutLivraison statut, long id);

    List<Livraison> findByStatut(StatutLivraison statut);
    List<Livraison> findByClient_Delegation(String delegation);
    List<Livraison> findByTransporteur_Id(Long transporteurId);
    List<Livraison> findByDateLivraisonBetween(LocalDate startDate, LocalDate endDate);
    List<Livraison> findByDateLivraison(LocalDate dateLivraison);
    List<Livraison> findByTransporteur_IdAndStatut(Long id, StatutLivraison  statut);
    List<Livraison> findByTransporteur_IdAndClientTelephone(Long transporteurId, String numeroTelephone);
    List<Livraison> findByClient_Id(Long clientId);
    List<Livraison> findByClient_Telephone(String telephone);
List<Livraison> findByTransporteur_Username(String username);
    List<Livraison> findByClient_Username(String username);
}
