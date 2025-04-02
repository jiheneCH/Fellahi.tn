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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class LivraisonServiceImpl implements ILivraisonServices {
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

    @Override
    public Transporteur findLivreurAvecMoinsDeLivraisons(String delegation) {
        // Récupérer tous les transporteurs dans la même délégation
        List<Transporteur> transporteurs = transporteurRepository.findAll();

        // Filtrer les transporteurs dans la même délégation
        transporteurs = transporteurs.stream()
                .filter(t -> t.getDelegation().equals(delegation))
                .collect(Collectors.toList());

        // Trouver le transporteur avec le nombre minimal de livraisons
        return transporteurs.stream()
                .min(Comparator.comparingInt(Transporteur::getNbLivraisons))
                .orElseThrow(() -> new RuntimeException("Aucun transporteur disponible dans cette délégation"));
    }

    @Transactional
    @Override
    public Map<String, Object> creerLivraison(Long idCommande) {
        // 1. Récupérer la commande
        Commande commande = commandeRepository.findById(idCommande)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));

        // 2. Récupérer le client associé
        Client client = clientRepository.findById(commande.getIdClient())
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));

        String delegationClient = client.getDelegation();

        // 3. Trouver le transporteur disponible dans la même délégation
        Transporteur transporteur = findLivreurAvecMoinsDeLivraisons(delegationClient);

        // 4. Calculer la date de livraison (3 jours après la date de commande)
        LocalDate dateLivraison = estimerDateLivraison(delegationClient);

        // 5. Calculer le prix total en utilisant la méthode définie
        double prixTotalLivraison = calculerPrixTotalLivraison(delegationClient, commande.getPrixTotal());

        // 6. Créer la livraison
        Livraison livraison = new Livraison();
        livraison.setClientId(client.getId());
        livraison.setCommandeId(commande.getId());
        livraison.setTransporteurId(transporteur.getId());
        livraison.setDateLivraison(dateLivraison);
        livraison.setStatut(StatutLivraison.EN_ATTENTE);
        livraison.setPrixTotal(prixTotalLivraison);

        // 7. Sauvegarder la livraison
        livraisonRepository.save(livraison);
        transporteur.setNbLivraisons(transporteur.getNbLivraisons() + 1);
        transporteurRepository.save(transporteur);

        // 8. Construire la réponse
        Map<String, Object> response = new HashMap<>();
        response.put("transporteur", Map.of(
                "id", transporteur.getId(),
                "nom", transporteur.getNom(),
                "delegation", transporteur.getDelegation(),
                "nbLivraisons", transporteur.getNbLivraisons()
        ));
        response.put("client", Map.of(
                "id", client.getId(),
                "nom", client.getNom(),
                "prenom", client.getPrenom(),
                "email", client.getEmail(),
                "telephone", client.getTelephone(),
                "adresse", client.getAdresse(),
                "delegation", client.getDelegation()
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


    @Override
    public List<Livraison> getAllLivraisons() {
        return livraisonRepository.findByArchivedFalse();
    }

    @Override
    public Livraison getLivraisonById(Long id) {
        Livraison livraison = livraisonRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (livraison.getStatut() == StatutLivraison.ARCHIVE) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return livraison;
    }

    @Override
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

    @Override
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

    private static final Map<String, Integer> REGION_TARIFS = new HashMap<>();

    static {
        REGION_TARIFS.put("Bizerte", 7);
        REGION_TARIFS.put("Beja", 7);
        REGION_TARIFS.put("Jendouba", 7);
        REGION_TARIFS.put("Kef", 7);

        REGION_TARIFS.put("Tunis", 5);
        REGION_TARIFS.put("Ariana", 5);
        REGION_TARIFS.put("Ben Arous", 5);
        REGION_TARIFS.put("Zaghouan", 5);
        REGION_TARIFS.put("Manouba", 5);
        REGION_TARIFS.put("Nabeul", 5);

        REGION_TARIFS.put("Siliana", 8);
        REGION_TARIFS.put("Sousse", 8);
        REGION_TARIFS.put("Kairouan", 8);
        REGION_TARIFS.put("Kasserine", 8);
        REGION_TARIFS.put("Monastir", 8);
        REGION_TARIFS.put("Mahdia", 8);

        REGION_TARIFS.put("Tozeur", 10);
        REGION_TARIFS.put("Sidi Bouzid", 10);
        REGION_TARIFS.put("Sfax", 10);
        REGION_TARIFS.put("Gafsa", 10);

        REGION_TARIFS.put("Tataouine", 15);
        REGION_TARIFS.put("Gabes", 15);
        REGION_TARIFS.put("Kebili", 15);
        REGION_TARIFS.put("Medenine", 15);
    }

    @Override
    public double calculerPrixTotalLivraison(String delegation, double prixCommande) {
        int fraisLivraison = REGION_TARIFS.getOrDefault(delegation, 10); // 20 DT par défaut
        return prixCommande + fraisLivraison;
    }

    public LocalDate estimerDateLivraison(String delegation) {
        Map<String, Integer> REGION_DELAIS = new HashMap<>();

        REGION_DELAIS.put("Bizerte", 2);
        REGION_DELAIS.put("Beja", 2);
        REGION_DELAIS.put("Jendouba", 2);
        REGION_DELAIS.put("Kef", 2);  // Région 1

        REGION_DELAIS.put("Tunis", 1);
        REGION_DELAIS.put("Ariana", 1);
        REGION_DELAIS.put("Ben Arous", 1);
        REGION_DELAIS.put("Zaghouan", 1);
        REGION_DELAIS.put("Manouba", 1);
        REGION_DELAIS.put("Nabeul", 1); // Région 2

        REGION_DELAIS.put("Siliana", 3);
        REGION_DELAIS.put("Sousse", 3);
        REGION_DELAIS.put("Kasserine", 3);
        REGION_DELAIS.put("Kairouan", 3);
        REGION_DELAIS.put("Monastir", 3);
        REGION_DELAIS.put("Mahdia", 3); // Région 3

        REGION_DELAIS.put("Tozeur", 4);
        REGION_DELAIS.put("Sidi Bouzid", 4);
        REGION_DELAIS.put("Sfax", 4);
        REGION_DELAIS.put("Gafsa", 4); // Région 4

        REGION_DELAIS.put("Tataouine", 5);
        REGION_DELAIS.put("Gabes", 5);
        REGION_DELAIS.put("Kebili", 5);
        REGION_DELAIS.put("Medenine", 5); // Région 5

        // Par défaut, on met 3 jours si la délégation n'est pas trouvée
        int delai = REGION_DELAIS.getOrDefault(delegation, 3);

        return LocalDate.now().plusDays(delai);
    }

    @Override
    public Map<String, Long> calculerLivraisonsParStatut() {
        // Récupérer le nombre de livraisons avec différents statuts
        long livraisonsLivrees = livraisonRepository.countByStatut(StatutLivraison.LIVRE);
        long livraisonsAnnulees = livraisonRepository.countByStatut(StatutLivraison.ANNULE);
        long livraisonsEnAttente = livraisonRepository.countByStatut(StatutLivraison.EN_ATTENTE);
        long livraisonsEnCours = livraisonRepository.countByStatut(StatutLivraison.EN_COURS);
        long livraisonsRetarde = livraisonRepository.countByStatut(StatutLivraison.RETARDE);

        // Créer une map de résultats à retourner
        Map<String, Long> result = new HashMap<>();
        result.put("Livraisons Livrées", livraisonsLivrees);
        result.put("Livraisons Annulées", livraisonsAnnulees);
        result.put("Livraisons En Attente", livraisonsEnAttente);
        result.put("Livraisons En Cours", livraisonsEnCours);
        result.put("Livraisons Retardé", livraisonsRetarde);

        return result;
    }

    @Override
    public List<Livraison> getLivraisonsByStatut(StatutLivraison statut) {
        if (statut != null) {
            return livraisonRepository.findByStatut(statut);
        }
        return livraisonRepository.findAll();
    }

    @Override
    public List<Livraison> getLivraisonsByDelegation(String delegation) {
        return livraisonRepository.findByClient_Delegation(delegation);
    }

    @Override
    public List<Livraison> findByTransporteurId(Long transporteurId) {
        return livraisonRepository.findByTransporteur_Id(transporteurId);
    }

    @Override
    public List<Livraison> getLivraisonsBetweenDates(LocalDate startDate, LocalDate endDate) {
        // Appel de la méthode du repository pour récupérer les livraisons entre les deux dates
        return livraisonRepository.findByDateLivraisonBetween(startDate, endDate);
    }

    @Override
    public List<Livraison> getLivraisonsByDate(LocalDate dateLivraison) {
        // Appel de la méthode du repository pour récupérer les livraisons d'une date spécifique
        return livraisonRepository.findByDateLivraison(dateLivraison);
    }

    @Override
    public List<Livraison> getLivraisonsByClientId(Long clientId) {
        return livraisonRepository.findByClient_Id(clientId);
    }

    @Override
    public String modifierLivraison(Long id, StatutLivraison statut, LocalDate dateLivraisonSaisie) {
        // Vérifier si la livraison existe
        Optional<Livraison> livraisonOpt = livraisonRepository.findById(id);
        if (!livraisonOpt.isPresent()) {
            return "Erreur : Livraison non trouvée.";
        }
        Livraison livraison = livraisonOpt.get();

        // Vérifier si la modification est autorisée
        if (livraison.getStatut() == StatutLivraison.LIVRE ||
                livraison.getStatut() == StatutLivraison.ARCHIVE ||
                livraison.getStatut() == StatutLivraison.ANNULE) {
            return "Vous ne pouvez pas modifier cette livraison. Statut verrouillé.";
        }

        boolean modifEffectuee = false;

        // Mise à jour du statut si fourni
        if (statut != null ) {
            try {
                livraison.setStatut(StatutLivraison.valueOf(String.valueOf(statut)));
                modifEffectuee = true;
            } catch (IllegalArgumentException e) {
                return "Erreur : Statut invalide.";
            }
        }

        // Mise à jour de la date si fournie et valide
        if (dateLivraisonSaisie != null) {
            if (dateLivraisonSaisie.isAfter(livraison.getDateLivraison())) {
                livraison.setDateLivraison(dateLivraisonSaisie);
                modifEffectuee = true;
            } else {
                return "Erreur : La nouvelle date doit être postérieure à la date actuelle.";
            }
        }

        if (modifEffectuee) {
            livraisonRepository.save(livraison);
            return "Mise à jour réussie.";
        } else {
            return "Aucune modification effectuée.";
        }
    }
    @Override
    public Livraison reaffecterLivraison(Long livraisonId) {
        // Récupérer la livraison par ID
        Livraison livraison = livraisonRepository.findById(livraisonId)
                .orElseThrow(() -> new RuntimeException("Livraison non trouvée"));

        // Vérifier si la livraison a déjà un livreur affecté
        if (livraison.getTransporteur() == null) {
            throw new RuntimeException("Aucun livreur actuel n'est affecté à cette livraison");
        }

        // Récupérer le livreur actuel
        Transporteur livreurActuel = livraison.getTransporteur();

        // Récupérer la délégation du client
        String delegationClient = livraison.getCommande().getClient().getDelegation();

        // Trouver un nouveau livreur dans la même délégation, excluant le livreur actuel
        Transporteur nouveauLivreur = findLivreurAvecMoinsDeLivraisons(delegationClient);

        if (nouveauLivreur == null) {
            throw new RuntimeException("Aucun autre livreur disponible dans cette délégation");
        }

        // Décrémenter le nombre de livraisons de l'ancien livreur
        livreurActuel.setNbLivraisons(livreurActuel.getNbLivraisons() - 1);
        transporteurRepository.save(livreurActuel);

        // Incrémenter le nombre de livraisons du nouveau livreur
        nouveauLivreur.setNbLivraisons(nouveauLivreur.getNbLivraisons() + 1);
        transporteurRepository.save(nouveauLivreur);

        // Affecter le nouveau livreur à la livraison
        livraison.setTransporteur(nouveauLivreur);
        livraisonRepository.save(livraison);

        return livraison;
    }
}














