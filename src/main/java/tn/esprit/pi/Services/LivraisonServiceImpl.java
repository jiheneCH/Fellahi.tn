package tn.esprit.pi.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi.Entities.*;
import tn.esprit.pi.Repositories.*;

import java.time.LocalDate;
import java.util.*;

@Service
@AllArgsConstructor
public class LivraisonServiceImpl implements ILivraisonServices{
    @Autowired
    private LivraisonRepository livraisonRepository;

    @Autowired
    private TransporteurRepository transporteurRepository;

    @Autowired
    private ProduitRepository produitRepository;
    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private ClientRepository clientRepository;


    @Transactional
    public Map<String, Object> creerLivraison(Long idCommande) {

        Commande commande = (Commande) commandeRepository.findById(idCommande).orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        Long idClient = commande.getIdClient();
        // 2. Récupérer le client associé
        Optional<Client> client = clientRepository.findById(commande.getIdClient());

        String delegationClient = client.get().getDelegation();

        // 3. Trouver le transporteur disponible dans la même délégation
        Transporteur transporteur = transporteurRepository
                .findLivreurWithLeastDeliveries(delegationClient);

        // 4. Calculer la date de livraison (3 jours après la date de commande)
        LocalDate dateLivraison = LocalDate.now().plusDays(3);

        // 5. Calculer le prix total (prix de la commande + 7DT)
        double prixTotalLivraison = commande.getPrixTotal() + 7;

        // 6. Créer la livraison
        Livraison livraison = new Livraison();
        livraison.setClientId(client.get().getId());
        livraison.setCommandeId(commande.getId());
        livraison.setTransporteurId(transporteur.getId());
        livraison.setDateLivraison(dateLivraison);
        livraison.setStatut(StatutLivraison.EN_ATTENTE);
        livraison.setPrixTotal(prixTotalLivraison);

        // 7. Sauvegarder la livraison
        livraisonRepository.save(livraison);
        transporteur.setNbLivraisons(transporteur.getNbLivraisons() + 1);
        transporteurRepository.save(transporteur);

        Map<String, Object> response = new HashMap<>();
        response.put("transporteur", Map.of(
                "id", transporteur.getId(),
                "nom", transporteur.getNom(),
                "delegation", transporteur.getDelegation(),
                "nbLivraisons", transporteur.getNbLivraisons()
        ));
        response.put("client", Map.of(
                "id", client.get().getId(),
                "nom", client.get().getNom(),
                "prenom", client.get().getPrenom(),
                "email", client.get().getEmail(),
                "telephone", client.get().getTelephone(),
                "adresse", client.get().getAdresse(),
                "delegation", client.get().getDelegation()
        ));
        response.put("commande", Map.of(
                "id", commande.getId(),
                "prix", commande.getPrixTotal()
        ));
        response.put("livraison", Map.of(
                "id", livraison.getId(),
                "dateLivraison", livraison.getDateLivraison(),
                "statut", livraison.getStatut(),
                "prixTotal", livraison.getPrixTotal()
        ));
        return response;
    }


    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findByArchivedFalse();
    }


    public Livraison getLivraisonById(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (livraison.getStatut() == StatutLivraison.ARCHIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return livraison;
    }

    public void archiverLivraison(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setArchived(true);
        livraison.setStatut(StatutLivraison.ARCHIVE);

        // Vérifier si un transporteur est associé
        Transporteur transporteur = livraison.getTransporteur();
        if (transporteur != null && transporteur.getNbLivraisons() > 0) {
            transporteur.setNbLivraisons(transporteur.getNbLivraisons() - 1);
            transporteurRepository.save(transporteur);
        }

        livraisonRepository.save(livraison);
    }

    public Livraison annulerLivraison(Long livraisonId) {
        // Récupérer la livraison existante par son ID
        Livraison livraison = livraisonRepository.findById(livraisonId).get();

        livraison.setStatut(StatutLivraison.ANNULE);
        // Vérifier si un transporteur est associé
        Transporteur transporteur = livraison.getTransporteur();
        if (transporteur != null && transporteur.getNbLivraisons() > 0) {
            transporteur.setNbLivraisons(transporteur.getNbLivraisons() - 1);
            transporteurRepository.save(transporteur);
        }
        return livraisonRepository.save(livraison);
    }
}












   /* @Transactional
    public Livraison createLivraison(List<Long> produitIds, String nomClient, String prenomClient,
                                     String delegation, String adresseExacte, Date dateLiv, Long numTel) {
        // Récupérer les produits à partir de leurs IDs
        List<Produit> produits = produitRepository.findAllById(produitIds);

        // Calcul du prix total
        double prixTotal = produits.stream().mapToDouble(Produit::getPrix).sum();

        // Trouver le transporteur ayant le moins de livraisons dans la délégation donnée
        Transporteur transporteur = transporteurRepository.findLivreurWithLeastDeliveries(delegation);

        // Si dateLiv est null, on l'attribue à 3 jours après la date actuelle
        if (dateLiv == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DAY_OF_MONTH, 3); // Ajoute 3 jours à la date actuelle
            dateLiv = calendar.getTime(); // Récupère la nouvelle date
        }

        // Création de la livraison
        Livraison livraison = new Livraison();
        livraison.setProduits(produits);
        livraison.setPrixTotal(prixTotal);
        livraison.setNomClient(nomClient);
        livraison.setPrenomClient(prenomClient);
        livraison.setDelegation(delegation);
        livraison.setAdresseExacte(adresseExacte);
        livraison.setNumTel(numTel);
        livraison.setDateLiv(dateLiv); // Assigner la dateLiv (avec la nouvelle date si null)
        livraison.setTransporteur(transporteur);
        livraison.setNomTransporteur(transporteur.getNom());
        livraison.setStatut(StatutLivraison.EN_ATTENTE);

        // Mettre à jour le nombre de livraisons du transporteur
        transporteur.setNbLivraisons(transporteur.getNbLivraisons() + 1);
        transporteurRepository.save(transporteur);

        // Enregistrer la livraison
        return livraisonRepository.save(livraison);
    }

    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findByArchivedFalse();
    }
    public Livraison getLivraisonById(Long id) {
        return livraisonRepository.findById(id).get();
    }

    public Livraison getLivraisonById(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (livraison.getStatut() == StatutLivraison.ARCHIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return livraison;
    }


    public void archiverLivraison(Long id) {
        Livraison livraison = livraisonRepository.findById(id).get();
        livraison.setArchived(true);
        livraison.setStatut(StatutLivraison.ARCHIVE);
        livraisonRepository.save(livraison);
    }
    @Transactional
    public Livraison annulerLivraison(Long livraisonId) {
        // Récupérer la livraison existante par son ID
        Livraison livraison = livraisonRepository.findById(livraisonId).get();

        livraison.setStatut(StatutLivraison.ANNULE);
        return livraisonRepository.save(livraison);
    }

public void archiverLivraison(Long id) {
    Livraison livraison = livraisonRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

    livraison.setArchived(true);
    livraison.setStatut(StatutLivraison.ARCHIVE);

    // Vérifier si un transporteur est associé
    Transporteur transporteur = livraison.getTransporteur();
    if (transporteur != null && transporteur.getNbLivraisons() > 0) {
        transporteur.setNbLivraisons(transporteur.getNbLivraisons() - 1);
        transporteurRepository.save(transporteur);
    }

    livraisonRepository.save(livraison);
}

    @Transactional
    public Livraison annulerLivraison(Long livraisonId) {
        Livraison livraison = livraisonRepository.findById(livraisonId)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        livraison.setStatut(StatutLivraison.ANNULE);

        // Vérifier si un transporteur est associé
        Transporteur transporteur = livraison.getTransporteur();
        if (transporteur != null && transporteur.getNbLivraisons() > 0) {
            transporteur.setNbLivraisons(transporteur.getNbLivraisons() - 1);
            transporteurRepository.save(transporteur);
        }

        return livraisonRepository.save(livraison);
    }

    @Transactional
    public Livraison updateLivraison(Long id, Livraison updateLivraison) {
        // Trouver la livraison existante
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        // Si la livraison est déjà livrée, aucune modification n'est autorisée
        if (livraison.getStatut() == StatutLivraison.LIVRE) {
            throw new RuntimeException("Impossible de modifier une livraison déjà marquée comme LIVRE.");
        }

        // Stocker l'ancien statut pour vérifier si on passe à LIVRE
        StatutLivraison ancienStatut = livraison.getStatut();

        // Modification des champs autorisés
        if (livraison.getStatut() == StatutLivraison.EN_ATTENTE) {
            if (updateLivraison.getNomClient() != null) {
                livraison.setNomClient(updateLivraison.getNomClient());
            }
            if (updateLivraison.getPrenomClient() != null) {
                livraison.setPrenomClient(updateLivraison.getPrenomClient());
            }
            if (updateLivraison.getDelegation() != null) {
                livraison.setDelegation(updateLivraison.getDelegation());
            }
            if (updateLivraison.getAdresseExacte() != null) {
                livraison.setAdresseExacte(updateLivraison.getAdresseExacte());
            }
            if (updateLivraison.getNumTel() != null) {
                livraison.setNumTel(updateLivraison.getNumTel());
            }
            if (updateLivraison.getDateLiv() != null) {
                livraison.setDateLiv(updateLivraison.getDateLiv());
            }
            if (updateLivraison.getStatut() != null) {
                livraison.setStatut(updateLivraison.getStatut());
            }
        } else if (livraison.getStatut() == StatutLivraison.EN_COURS ||
                livraison.getStatut() == StatutLivraison.RETARDE) {
            if (updateLivraison.getDateLiv() != null) {
                livraison.setDateLiv(updateLivraison.getDateLiv());
            }
            if (updateLivraison.getStatut() != null) {
                livraison.setStatut(updateLivraison.getStatut());
            }
        }

        // Si la livraison passe au statut LIVRE, on décrémente nbLivraisons du transporteur
        if (ancienStatut != StatutLivraison.LIVRE && livraison.getStatut() == StatutLivraison.LIVRE) {
            Transporteur transporteur = livraison.getTransporteur();
            if (transporteur != null && transporteur.getNbLivraisons() > 0) {
                transporteur.setNbLivraisons(transporteur.getNbLivraisons() - 1);
                transporteurRepository.save(transporteur);
            }
        }

        return livraisonRepository.save(livraison);
    }
*/
/*
    public Livraison updateLivraison(Long id, Livraison updateLivraison) {
        // Trouver la livraison existante
        Livraison livraison = livraisonRepository.findById(id).get();
        if (livraison.getStatut() == StatutLivraison.EN_ATTENTE) {

            if (updateLivraison.getNomClient() != null) {
                livraison.setNomClient(updateLivraison.getNomClient());
            }
            if (updateLivraison.getPrenomClient() != null) {
                livraison.setPrenomClient(updateLivraison.getPrenomClient());
            }
            if (updateLivraison.getDelegation() != null) {
                livraison.setDelegation(updateLivraison.getDelegation());
            }
            if (updateLivraison.getAdresseExacte() != null) {
                livraison.setAdresseExacte(updateLivraison.getAdresseExacte());
            }
            if (updateLivraison.getNumTel() != null) {
                livraison.setNumTel(updateLivraison.getNumTel());
            }
            if (updateLivraison.getDateLiv() != null) {
                livraison.setDateLiv(updateLivraison.getDateLiv());
            }
            if (updateLivraison.getStatut() != null) {
                livraison.setStatut(updateLivraison.getStatut());
            }
        } else if (livraison.getStatut() == StatutLivraison.LIVRE ||
                livraison.getStatut() == StatutLivraison.EN_COURS ||
                livraison.getStatut() == StatutLivraison.RETARDE) {

            if (updateLivraison.getDateLiv() != null) {
                livraison.setDateLiv(updateLivraison.getDateLiv());
            }

            if (updateLivraison.getStatut() != null) {
                livraison.setStatut(updateLivraison.getStatut());
            }

            if (updateLivraison.getNomClient() != null ||
                    updateLivraison.getPrenomClient() != null ||
                    updateLivraison.getDelegation() != null ||
                    updateLivraison.getAdresseExacte() != null ||
                    updateLivraison.getNumTel() != null) {

            }
        } else {
            if (updateLivraison.getDateLiv() != null) {
                livraison.setDateLiv(updateLivraison.getDateLiv());
            }
            if (updateLivraison.getStatut() != null) {
                livraison.setStatut(updateLivraison.getStatut());
            }
            if (updateLivraison.getNomClient() != null ||
                    updateLivraison.getPrenomClient() != null ||
                    updateLivraison.getDelegation() != null ||
                    updateLivraison.getAdresseExacte() != null ||
                    updateLivraison.getNumTel() != null) {

            }
        }

        Livraison updatedLivraison = livraisonRepository.save(livraison);

        return updatedLivraison;
    }
*/
