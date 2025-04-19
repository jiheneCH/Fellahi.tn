package tn.esprit.fallehiuser.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.fallehiuser.model.Livraison;
import tn.esprit.fallehiuser.model.StatutLivraison;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LivraisonRepository extends JpaRepository<Livraison, Long>{
    Optional<Livraison> findById(Long id);
    List<Livraison> findByArchivedFalse();
    long countByStatut(StatutLivraison statut);

    long countByStatutAndTransporteur_Id(StatutLivraison statut, long id);

    long countByStatutAndClient_Governorate(StatutLivraison statut, String clientDelegation);
    List<Livraison> findByStatut(StatutLivraison statut);
    List<Livraison> findByClient_Governorate(String delegation);
    List<Livraison> findByTransporteur_Id(Long transporteurId);
    List<Livraison> findByDateLivraisonBetween(LocalDate startDate, LocalDate endDate);
    List<Livraison> findByDateLivraison(LocalDate dateLivraison);
    List<Livraison> findByTransporteur_IdAndStatut(Long id, StatutLivraison  statut);
    List<Livraison> findByClient_Id(Long clientId);
    List<Livraison> findByClient_PhoneNumber(String telephone);
List<Livraison> findByTransporteur_Username(String username);
    List<Livraison> findByClient_Username(String username);
List<Livraison> findLivraisonByClient_Governorate(String delegation);


    Optional<Livraison> findByRefLivraison(String refLivraison);

}
