package tn.esprit.pi.Services;

import tn.esprit.pi.Entities.Livraison;
import tn.esprit.pi.Entities.StatutLivraison;
import tn.esprit.pi.Entities.Transporteur;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ILivraisonServices {
    public Transporteur findLivreurAvecMoinsDeLivraisons(String delegation);
    public Livraison getLivraisonById(Long id);
    public Map<String, Object> creerLivraison(Long idCommande);
    public List<Livraison> getAllLivraisons();
    public double calculerPrixTotalLivraison(String delegation, double prixCommande);
    public Livraison annulerLivraison(Long livraisonId);
    public void archiverLivraison(Long id);
    public LocalDate estimerDateLivraison(String delegation);
    public Map<String, Long> calculerLivraisonsParStatut();


}
