package tn.esprit.pi.Services;

import tn.esprit.pi.Entities.Livraison;

import java.util.List;
import java.util.Map;

public interface ILivraisonServices {
    public Livraison getLivraisonById(Long id);
    public List<Livraison> getAllLivraisons();
    public Map<String, Object> creerLivraison(Long idCommande);
}
