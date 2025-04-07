package tn.esprit.panier.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.panier.Entities.*;
import tn.esprit.panier.Repositories.ArticleRepository;
import tn.esprit.panier.Repositories.ClientRepository;
import tn.esprit.panier.Repositories.CommandeRepository;
import tn.esprit.panier.Repositories.DetailsCommandeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CommandeServicesImpl implements ICommandeServices {
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private DetailsCommandeRepository detailsCommandeRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Commande> retrieveAllCommande() {
        return commandeRepository.findAll();
    }

    @Override
    public Commande retrieveCommande(long idCommande) {
        return commandeRepository.findById(idCommande).get();
    }

    @Override
    public Commande updateCommande(Commande commande) {
        return commandeRepository.save(commande);
    }

    @Override
    public void deleteCommande(long idCommande) {
        try {
            // Trouver la commande par ID
            Commande commande = commandeRepository.findById(idCommande)
                    .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée avec l'ID " + idCommande));

            // Récupérer le statut de la commande
            String status = commande.getStatus().toString(); // Si le statut est un Enum, on le convertit en String
            System.out.println("Statut de la commande : " + status); // Pour le log

            // Vérification du statut
            if ("encours".equalsIgnoreCase(status)) {
                // Si le statut est ENCOURS, on supprime la commande
                commandeRepository.deleteById(idCommande);
                System.out.println("Commande avec ID " + idCommande + " supprimée avec succès.");
            } else if ("valider".equalsIgnoreCase(status)) {
                // Si le statut est VALIDE, on ne permet pas la suppression
                System.out.println("La commande est déjà validée, impossible de la supprimer.");
                throw new IllegalStateException("Impossible de supprimer une commande validée.");
            } else {
                System.out.println("La commande ne peut pas être supprimée, son statut n'est ni 'ENCOURS' ni 'VALIDE'.");
                // On pourrait ajouter d'autres conditions si nécessaire pour d'autres statuts
                throw new IllegalStateException("Le statut de la commande ne permet pas la suppression.");
            }

        } catch (IllegalArgumentException e) {
            // Gestion des erreurs si la commande n'existe pas
            System.err.println("Erreur : " + e.getMessage());
            throw new RuntimeException("Une erreur est survenue : " + e.getMessage(), e);
        } catch (IllegalStateException e) {
            // Gestion des erreurs de statut
            System.err.println("Erreur de statut : " + e.getMessage());
            throw e;  // Propager l'exception si le statut ne permet pas la suppression
        } catch (Exception e) {
            // Gestion des erreurs inattendues
            System.err.println("Erreur inattendue : " + e.getMessage());
            throw new RuntimeException("Erreur inattendue : " + e.getMessage(), e);
        }
    }


    @Override
    public Commande addCommande(Commande commande) {
        Long clientId = commande.getClient().getIdClient();

        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client avec ID " + clientId + " introuvable"));

        commande.setClient(client); // On utilise l’objet complet de la BDD

        return commandeRepository.save(commande);
    }
    @Override
    public String createCommande(long clientId) {
        try {
            // Trouver le client par ID
            Client client = clientRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID " + clientId));

            // Récupérer toutes les commandes du client
            List<Commande> commandes = commandeRepository.findByClient(client);

            // Vérifier si le client a une commande en cours
            for (Commande commande : commandes) {
                // Comparer l'énumération directement
                if (commande.getStatus() == Status.encours) {
                    // Si une commande en cours existe, on ne permet pas la création de la nouvelle commande
                    return "Le client a déjà une commande en cours, une nouvelle commande ne peut pas être créée.";
                }
            }

            // Si aucune commande en cours n'a été trouvée, on crée la nouvelle commande
            Commande nouvelleCommande = new Commande();
            nouvelleCommande.setClient(client);
            nouvelleCommande.setStatus(Status.encours); // Nouvelle commande en cours
            commandeRepository.save(nouvelleCommande);

            return "Nouvelle commande créée avec succès pour le client.";

        } catch (IllegalArgumentException e) {
            // Gestion des erreurs
            System.err.println("Erreur : " + e.getMessage());
            return "Erreur : " + e.getMessage();
        } catch (Exception e) {
            // Gestion des erreurs inattendues
            System.err.println("Erreur inattendue : " + e.getMessage());
            return "Erreur inattendue : " + e.getMessage();
        }
    }


    public void calculerPrixTotalCommande(Commande commande) {
        int total = 0;
        if (commande.getDetailsCommande() != null) {
            for (DetailsCommande details : commande.getDetailsCommande()) {
                total += details.getPrixTotalArticle();
            }
        }
        commande.setPrixTotalCommande(total);
        commandeRepository.save(commande); // mettre à jour la BDD
    }

    @Override
    public void addArticleToCommande(long idCommande, long idArticle, int quantite) {
        try {
            // Trouver la commande par ID
            Commande commande = commandeRepository.findById(idCommande)
                    .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée avec l'ID " + idCommande));

            // Récupérer le statut de la commande
            String status = String.valueOf(commande.getStatus());
            System.out.println("Statut de la commande : " + status); // Ajout du log pour vérifier le statut

            // Vérification du statut de la commande
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalStateException("Le statut de la commande est invalide (null ou vide).");
            }

            // Si le statut est "valider", on bloque l'ajout
            if (status.equalsIgnoreCase("valider")) {
                System.out.println("La commande est déjà validée, impossible d'ajouter un article.");
                return;  // Retourner sans effectuer l'ajout
            }

            // Si le statut est "encours", on fait l'ajout
            if (status.equalsIgnoreCase("encours")) {
                System.out.println("La commande est en cours, ajout de l'article...");

                // Trouver l'article par ID
                Article article = articleRepository.findById(idArticle)
                        .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID " + idArticle));

                // Vérifier si l'article existe déjà dans la commande
                Optional<DetailsCommande> existingDetail = commande.getDetailsCommande().stream()
                        .filter(dc -> dc.getArticle().getIdArticle() == idArticle)
                        .findFirst();

                if (existingDetail.isPresent()) {
                    // Si l'article existe déjà, on met à jour la quantité
                    DetailsCommande dc = existingDetail.get();
                    dc.setQuantite(dc.getQuantite() + quantite);
                } else {
                    // Sinon, on crée un nouveau détail de commande
                    DetailsCommande newDetail = new DetailsCommande();
                    newDetail.setArticle(article);
                    newDetail.setQuantite(quantite);
                    newDetail.setCommande(commande); // Lien avec la commande

                    // Ajouter aux détails de la commande
                    commande.getDetailsCommande().add(newDetail);
                }

                // Sauvegarder la commande (avec cascade, les détails sont aussi sauvegardés)
                commandeRepository.save(commande);

                // Recalculer le prix total de la commande
                calculerPrixTotalCommande(commande);

            } else {
                System.out.println("Le statut de la commande n'est ni 'encours' ni 'valider'. Impossible d'ajouter l'article.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            throw new RuntimeException("Une erreur est survenue : " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            throw new RuntimeException("Erreur inattendue : " + e.getMessage(), e);
        }
    }

    @Override
    public void updateArticleInCommande(long idCommande, long idArticle, int nouvelleQuantite) {
        try {
            // Trouver la commande par ID
            Commande commande = commandeRepository.findById(idCommande)
                    .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée avec l'ID " + idCommande));

            // Récupérer le statut de la commande
            String status = String.valueOf(commande.getStatus());
            System.out.println("Statut de la commande : " + status); // Ajout du log pour vérifier le statut

            // Vérification du statut de la commande
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalStateException("Le statut de la commande est invalide (null ou vide).");
            }

            // Si le statut est "valider", on bloque la modification
            if (status.equalsIgnoreCase("valider")) {
                System.out.println("La commande est déjà validée, impossible de modifier un article.");
                return;  // Retourner sans effectuer la modification
            }

            // Si le statut est "encours", on fait la modification
            if (status.equalsIgnoreCase("encours")) {
                System.out.println("La commande est en cours, modification de l'article...");

                // Trouver l'article par ID dans les détails de la commande
                Optional<DetailsCommande> existingDetail = commande.getDetailsCommande().stream()
                        .filter(dc -> dc.getArticle().getIdArticle() == idArticle)
                        .findFirst();

                if (existingDetail.isPresent()) {
                    // Si l'article existe déjà, on met à jour la quantité
                    DetailsCommande dc = existingDetail.get();
                    dc.setQuantite(nouvelleQuantite);

                    // Sauvegarder la commande (avec cascade, les détails sont aussi sauvegardés)
                    commandeRepository.save(commande);

                    // Recalculer le prix total de la commande
                    calculerPrixTotalCommande(commande);
                } else {
                    System.out.println("L'article avec l'ID " + idArticle + " n'existe pas dans cette commande.");
                }

            } else {
                System.out.println("Le statut de la commande n'est ni 'encours' ni 'valider'. Impossible de modifier l'article.");
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Erreur : " + e.getMessage());
            throw new RuntimeException("Une erreur est survenue : " + e.getMessage(), e);
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
            throw new RuntimeException("Erreur inattendue : " + e.getMessage(), e);
        }
    }

    @Override
    public void deleteArticleInCommande(long idCommande, long idArticle) {

        }


}