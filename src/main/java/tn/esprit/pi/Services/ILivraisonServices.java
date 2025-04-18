package tn.esprit.pi.Services;

import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;
import tn.esprit.pi.Entities.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ILivraisonServices {
    //public Transporteur findLivreurAvecMoinsDeLivraisons(String delegation);
   public Livraison getLivraisonById(Long id);
    public Map<String, Object> creerLivraison(Long idCommande);
   public List<Livraison> getAllLivraisons();
    public double calculerPrixTotalLivraison(String delegation, double prixCommande);
    public Livraison annulerLivraison(Long livraisonId);
     public void archiverLivraison(Long id);
    public LocalDate estimerDateLivraison(String delegation);
    public Map<String, Long> calculerLivraisonsParStatut();
    public List<Livraison> getLivraisonsByStatut(StatutLivraison statut);
    public List<Livraison> getLivraisonsByDelegation(String delegation);
    public List<Livraison> getLivraisonsByTelephone(String telephone);
    public List<Livraison> getLivraisonsByClientId(Long clientId);
public List<Livraison> getLivraisonsByTUsername(String username);
 public List<Livraison> getLivraisonsByCUsername(String username);
    public List<Livraison> findByTransporteurId(Long transporteurId);
    public List<Livraison> getLivraisonsBetweenDates(LocalDate startDate, LocalDate endDate);
    public List<Livraison> getLivraisonsByDate(LocalDate dateLivraison);
    public String modifierLivraison(Long livraisonId, StatutLivraison statut, LocalDate DateLivraison);
    public Livraison changerStatut(Long livraisonId);
    public Livraison reaffecterLivraison(Long livraisonId);

 public User findTransporteurAvecMoinsDeLivraisonsReaffectation(String delegation, Long idLivreurAExclure);

    public User findTransporteurAvecMoinsDeLivraisons(String delegation);
    public Map<String, Long> calculerStatusParLivreur( long idLivreur);
}
