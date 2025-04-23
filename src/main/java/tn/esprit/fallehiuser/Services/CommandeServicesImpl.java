package tn.esprit.fallehiuser.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.Repository.ArticleRepository;
import tn.esprit.fallehiuser.Repository.CommandeRepository;
import tn.esprit.fallehiuser.Repository.DetailsCommandeRepository;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.ResourceNotFoundException;
import tn.esprit.fallehiuser.model.*;


import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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
    private UserRepository userRepository;
   /* @Autowired
    private TwilioService twilioService;*/

    @Override
    public List<Commande> retrieveAllCommande() {
        return commandeRepository.findAll();
    }

    @Override
    public Commande retrieveCommande(long idCommande) {
        return commandeRepository.findById(idCommande)
                .orElseThrow(() -> new ResourceNotFoundException("Commande avec l'ID " + idCommande + " non trouvée."));
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
            String status = commande.getStatusCommande().toString(); // Si le statut est un Enum, on le convertit en String
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
        Long clientId = commande.getUser().getId();

        User user = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client avec ID " + clientId + " introuvable"));

        commande.setUser(user); // On utilise l’objet complet de la BDD

        return commandeRepository.save(commande);
    }
    @Override
    public String createCommande(long clientId) {
        try {
            // Trouver le client par ID
            User user = userRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID " + clientId));

            // Récupérer toutes les commandes du client
            List<Commande> commandes = commandeRepository.findByUser(user);

            // Vérifier si le client a une commande en cours
            for (Commande commande : commandes) {
                // Comparer l'énumération directement
                if (commande.getStatusCommande() == StatusCommande.Processing) {
                    // Si une commande en cours existe, on ne permet pas la création de la nouvelle commande
                    return "Le client a déjà une commande en cours, une nouvelle commande ne peut pas être créée.";
                }
            }

            // Si aucune commande en cours n'a été trouvée, on crée la nouvelle commande
            Commande nouvelleCommande = new Commande();
            nouvelleCommande.setUser(user);
            nouvelleCommande.setStatusCommande(StatusCommande.Processing); // Nouvelle commande en cours

            // Générer une référence unique pour la commande
            String referenceCommande = generateReferenceCommande(user);
            nouvelleCommande.setReferenceCommande(referenceCommande);

            // Sauvegarder la commande dans la base de données
            commandeRepository.save(nouvelleCommande);

            return "Nouvelle commande créée avec succès pour le client. Référence : " + referenceCommande;

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

    // Méthode pour générer une référence unique pour la commande
    private String generateReferenceCommande(User user) {
        // Utilisation de l'ID client et de la date actuelle pour générer une référence unique
        String clientIdStr = String.valueOf(user.getId());
        String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        return "CMD-" + clientIdStr + "-" + dateStr;
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
    public void calculerTotalPointsFidelite(Commande commande) {
        int totalPoints = 0;

        if (commande.getDetailsCommande() != null) {
            for (DetailsCommande details : commande.getDetailsCommande()) {
                totalPoints += details.getPointfidelityTotalArticle();
            }
        }

        commande.setPointfidelityTotalCommande(totalPoints); // <- Correction ici
        commandeRepository.save(commande);
    }

    @Override
    public void addArticleToCommande(long idArticle, int quantite, long clientId) {
        try {
            // 1. Récupérer le client
            User user = userRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID " + clientId));

            // 2. Récupérer toutes les commandes du client
            List<Commande> commandes = commandeRepository.findByUser(user);

            // 3. Chercher une commande en cours
            Commande commandeEnCours = commandes.stream()
                    .filter(commande -> commande.getStatusCommande() == StatusCommande.Processing)
                    .findFirst()
                    .orElse(null);

            // 4. Si aucune commande en cours, en créer une nouvelle
            if (commandeEnCours == null) {
                commandeEnCours = new Commande();
                commandeEnCours.setUser(user);
                commandeEnCours.setStatusCommande(StatusCommande.Processing);
                commandeEnCours.setReferenceCommande(generateReferenceCommande(user));
                commandeEnCours.setDetailsCommande(new HashSet<>());
                commandeRepository.save(commandeEnCours);
            }

            // 5. Initialiser les détails s'ils sont nuls (sécurité)
            if (commandeEnCours.getDetailsCommande() == null) {
                commandeEnCours.setDetailsCommande(new HashSet<>());
            }

            // 6. Récupérer l'article
            Article article = articleRepository.findById(idArticle)
                    .orElseThrow(() -> new IllegalArgumentException("Article non trouvé avec l'ID " + idArticle));

            // 7. Vérifier le statut de l’article
            if (article.getStatusAgri() == StatusAgri.OutOfStock) {
                throw new IllegalStateException("L'article est en rupture de stock (OutOfStock) et ne peut pas être ajouté.");
            }

            // 8. Vérifier la quantité disponible
            if (article.getQuantiteDisponible() == null || article.getQuantiteDisponible() < quantite) {
                throw new IllegalStateException("La quantité demandée dépasse la quantité disponible.");
            }

            // 9. Vérifier si l'article est déjà présent dans la commande
            Optional<DetailsCommande> existingDetail = commandeEnCours.getDetailsCommande().stream()
                    .filter(dc -> dc.getArticle().getIdArticle() == idArticle)
                    .findFirst();

            if (existingDetail.isPresent()) {
                DetailsCommande dc = existingDetail.get();
                int nouvelleQuantite = dc.getQuantite() + quantite;

                if (article.getQuantiteDisponible() < nouvelleQuantite) {
                    throw new IllegalStateException("Quantité insuffisante pour mettre à jour l'article dans le panier.");
                }

                dc.setQuantite(nouvelleQuantite);
            } else {
                DetailsCommande newDetail = new DetailsCommande();
                newDetail.setArticle(article);
                newDetail.setQuantite(quantite);
                newDetail.setCommande(commandeEnCours);
                commandeEnCours.getDetailsCommande().add(newDetail);
            }

            // 10. Recalculer prix/points
            commandeEnCours.getDetailsCommande().forEach(details -> {
                details.calculerPrixTotal();
                details.calculerPointfidelityTotal();
            });

            calculerPrixTotalCommande(commandeEnCours);
            calculerTotalPointsFidelite(commandeEnCours);

            // 11. Sauvegarder la commande mise à jour
            commandeRepository.save(commandeEnCours);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout d'article à la commande : " + e.getMessage(), e);
        }
    }




    @Override
    public void updateArticleInCommande(long idCommande, long idArticle, int nouvelleQuantite) {
        try {
            // 1. Récupérer la commande par ID
            Commande commande = commandeRepository.findById(idCommande)
                    .orElseThrow(() -> new IllegalArgumentException("Commande non trouvée avec l'ID " + idCommande));

            // 2. Récupérer le statut de la commande
            String status = String.valueOf(commande.getStatusCommande());
            System.out.println("Statut de la commande : " + status); // Ajout du log pour vérifier le statut

            // 3. Vérification du statut de la commande
            if (status == null || status.trim().isEmpty()) {
                throw new IllegalStateException("Le statut de la commande est invalide (null ou vide).");
            }

            // 4. Si le statut est "valider", on bloque la modification
            if (status.equalsIgnoreCase("valider")) {
                System.out.println("La commande est déjà validée, impossible de modifier un article.");
                return;  // Retourner sans effectuer la modification
            }

            // 5. Si le statut est "encours", on fait la modification
            if (status.equalsIgnoreCase("encours")) {
                System.out.println("La commande est en cours, modification de l'article...");

                // 6. Récupérer l'article dans les détails de la commande
                Optional<DetailsCommande> existingDetail = commande.getDetailsCommande().stream()
                        .filter(dc -> dc.getArticle().getIdArticle() == idArticle)
                        .findFirst();

                if (existingDetail.isPresent()) {
                    // 7. Si l'article existe déjà, mettre à jour la quantité
                    DetailsCommande dc = existingDetail.get();
                    dc.setQuantite(nouvelleQuantite);

                    // 8. Sauvegarder la commande (avec cascade, les détails sont aussi sauvegardés)
                    commandeRepository.save(commande);

                    // 9. Recalculer le prix total de la commande
                    calculerPrixTotalCommande(commande);
                } else {
                    System.out.println("L'article avec l'ID " + idArticle + " n'existe pas dans cette commande.");
                }

            } else {
                System.out.println("Le statut de la commande n'est ni 'encours' ni 'valider'. Impossible de modifier l'article.");
            }

        } catch (IllegalArgumentException e) {
            // 10. Gestion des erreurs d'argument
            e.printStackTrace();
            throw new RuntimeException("Erreur lors de la mise à jour de l'article : " + e.getMessage(), e);
        } catch (Exception e) {
            // 11. Gestion des erreurs générales
            e.printStackTrace();
            throw new RuntimeException("Erreur inattendue : " + e.getMessage(), e);
        }
    }


    @Override
    public void deleteArticleInCommande(Long commandeId, Long articleId) {
        List<DetailsCommande> detailsList = detailsCommandeRepository.findAll();

        // Récupérer la commande par son ID
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("La commande avec ID " + commandeId + " n'existe pas"));

        // Parcourir les détails de la commande pour trouver l'article à supprimer
        for (DetailsCommande detail : detailsList) {
            if (Long.valueOf(detail.getCommande().getIdCommande()).equals(commandeId) &&
                    Long.valueOf(detail.getArticle().getIdArticle()).equals(articleId)) {
                // Supprimer uniquement le détail de la commande (pas l'article)
                detailsCommandeRepository.delete(detail);

                // Recalculer le prix total de la commande en appelant la méthode calculerPrixTotalCommande
                calculerPrixTotalCommande(commande);

                return; // Sortir dès qu'on a trouvé et supprimé
            }
        }

        throw new RuntimeException("L'article avec ID " + articleId +
                " n'existe pas dans la commande ID " + commandeId);
    }
    @Override
    @Transactional
    public void validerCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + commandeId));

        if (commande.getStatusCommande() == StatusCommande.Pending) {
            // Validation de la commande
            commande.setStatusCommande(StatusCommande.Confirmed);
            commande.setDateCommande(new Date());

            User user = commande.getUser();

            // Calcul des points de fidélité et du prix total
            int totalPointsFidelite = 0;
            double prixTotal = 0;
            StringBuilder articlesAchetes = new StringBuilder();

            for (DetailsCommande details : commande.getDetailsCommande()) {
                Article article = details.getArticle();
                int quantiteAchetee = details.getQuantite();

                // Décrémenter la quantité disponible de l'article
                int nouvelleQuantite = article.getQuantiteDisponible() - quantiteAchetee;
                article.setQuantiteDisponible(nouvelleQuantite);

                articleRepository.save(article); // Sauvegarder la mise à jour du stock

                totalPointsFidelite += details.getPointfidelityTotalArticle();
                prixTotal += quantiteAchetee * article.getPrix();
                articlesAchetes.append("- ")
                        .append(article.getNom())
                        .append(" x")
                        .append(quantiteAchetee)
                        .append("\n");
            }

            // Ajouter les points au client
            user.setPointFidelityClient(user.getPointFidelityClient() + totalPointsFidelite);

            // Sauvegarder les modifications
            userRepository.save(user);
            commandeRepository.save(commande);

            // ✅ Tu peux décommenter cette partie pour envoyer un SMS :
        /*
        if (user.getPhone() != null && !user.getPhone().isEmpty()) {
            String message = "Bonjour " + user.getName() +
                    ",\n\nVotre commande #" + commande.getReferenceCommande() + " a été validée avec succès.\n" +
                    "Date de la commande : " + commande.getDateCommande() + "\n" +
                    "Prix total : " + prixTotal + " TND\n\n" +
                    "Articles achetés :\n" + articlesAchetes +
                    "\nMerci pour votre confiance !";

            twilioService.sendSms(user.getPhone(), message);
        }
        */

        } else {
            throw new IllegalStateException("La commande n'est pas en attente et ne peut pas être validée.");
        }
    }



    @Override
    public void cancelledCommande(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + commandeId));

        if (commande.getStatusCommande() == StatusCommande.Pending) {
            // Changer le statut à annulé
            commande.setStatusCommande(StatusCommande.Cancelled);



            // Sauvegarder la commande mise à jour
            commandeRepository.save(commande);

          /*  // Envoyer le SMS de notification si le téléphone est disponible
            Client client = commande.getClient();
            if (client.getPhone() != null && !client.getPhone().isEmpty()) {
                String message = "Bonjour " + client.getName() +
                        ",\n\nVotre commande #" + commande.getReferenceCommande() +
                        " a été annulée.\n\nMerci pour votre compréhension.";

                twilioService.sendSms(client.getPhone(), message);
            }*/
        }

        else {
            throw new IllegalStateException("La commande n'est pas dans un état 'EN ATTENTE' pour pouvoir être annulée.");
        }
    }


    @Override
    public void validerCommandeONSITE(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + commandeId));

        if (commande.getStatusCommande() == StatusCommande.Processing) {
            // Validation de la commande
            commande.setStatusCommande(StatusCommande.Pending);
            commande.setDateCommande(new Date());

            // Mettre à jour le status de paiement à 'onSite'
            commande.setPayment(StatusPayment.ONSITE);

            // Récupérer le client associé à la commande
            User user = commande.getUser();

            // Calculer les points de fidélité totaux de la commande
            int totalPointsFidelite = 0;
            for (DetailsCommande details : commande.getDetailsCommande()) {
                totalPointsFidelite += details.getPointfidelityTotalArticle();
            }

            // Ajouter les points de fidélité au client
            user.setPointFidelityClient(user.getPointFidelityClient() + totalPointsFidelite);
            userRepository.save(user);

            // Calculer le prix total de la commande
            double prixTotal = 0;
            StringBuilder articlesAchetes = new StringBuilder();
            for (DetailsCommande details : commande.getDetailsCommande()) {
                prixTotal += details.getPrixTotalArticle(); // Utilisation du prix total calculé pour chaque article

                // Liste des articles achetés
                articlesAchetes.append(details.getArticle().getNom()).append(" (")
                        .append(details.getQuantite()).append(" x ").append(details.getArticle().getPrix()).append(" TND)\n");
            }

            // Sauvegarder la commande après sa validation
            commandeRepository.save(commande);

            /*// ✅ Envoi du SMS de confirmation
            if (client.getPhone() != null && !client.getPhone().isEmpty()) {
                String message = "Bonjour " + client.getName() +
                        ",\n\nVotre commande #" + commande.getReferenceCommande() + " a été validée avec succès.\n" +
                        "Date de la commande : " + commande.getDateCommande() + "\n" +
                        "Statut de paiement : " + commande.getPayment() + "\n" +
                        "Prix total : " + prixTotal + " TND\n\n" +
                        "Articles achetés :\n" + articlesAchetes.toString() +
                        "\nMerci pour votre confiance !";
                twilioService.sendSms(client.getPhone(), message);  // Envoi du SMS
            }*/

        } else {
            throw new IllegalStateException("La commande n'est pas dans un état 'ENCOURS'");
        }
    }
    @Override
    public void validerCommandeONLINE(Long commandeId) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + commandeId));

        if (commande.getStatusCommande() == StatusCommande.Processing) {
            // Validation de la commande
            commande.setStatusCommande(StatusCommande.Confirmed);
            commande.setDateCommande(new Date());

            // Mettre à jour le status de paiement à 'onSite'
            commande.setPayment(StatusPayment.ONLINE);

            // Récupérer le client associé à la commande
            User user = commande.getUser();

            userRepository.save(user);

            // Calculer le prix total de la commande
            double prixTotal = 0;
            StringBuilder articlesAchetes = new StringBuilder();
            for (DetailsCommande details : commande.getDetailsCommande()) {
                prixTotal += details.getPrixTotalArticle(); // Utilisation du prix total calculé pour chaque article

                // Liste des articles achetés
                articlesAchetes.append(details.getArticle().getNom()).append(" (")
                        .append(details.getQuantite()).append(" x ").append(details.getArticle().getPrix()).append(" TND)\n");
            }

            // Sauvegarder la commande après sa validation
            commandeRepository.save(commande);

           /* // ✅ Envoi du SMS de confirmation
            if (client.getPhone() != null && !client.getPhone().isEmpty()) {
                String message = "Bonjour " + client.getName() +
                        ",\n\nVotre commande #" + commande.getReferenceCommande() + " a été validée avec succès.\n" +
                        "Date de la commande : " + commande.getDateCommande() + "\n" +
                        "Statut de paiement : " + commande.getPayment() + "\n" +
                        "Prix total : " + prixTotal + " TND\n\n" +
                        "Articles achetés :\n" + articlesAchetes.toString() +
                        "\nMerci pour votre confiance !";
                twilioService.sendSms(client.getPhone(), message);  // Envoi du SMS
            }*/

        } else {
            throw new IllegalStateException("La commande n'est pas dans un état 'ENCOURS'");
        }
    }



    @Override
    public void appliquerReductionParFidelite(Long commandeId, int pointsFideliteUtilises) {
        Commande commande = commandeRepository.findById(commandeId)
                .orElseThrow(() -> new RuntimeException("Commande introuvable avec l'ID : " + commandeId));

        // Vérification si la commande est validée
        if (commande.getStatusCommande() == StatusCommande.Confirmed) {
            throw new IllegalStateException("La commande est déjà validée. Impossible d'appliquer une réduction.");
        }

        // Vérification que le nombre de points de fidélité utilisés est valide
        if (pointsFideliteUtilises < 100) {
            throw new IllegalArgumentException("Le nombre de points de fidélité utilisé doit être supérieur ou égal à 100.");
        }

        // Récupérer le client associé à la commande
        User user = commande.getUser();

        // Vérification que le client a suffisamment de points
        if (user.getPointFidelityClient() < pointsFideliteUtilises) {
            throw new IllegalArgumentException("Le client n'a pas suffisamment de points de fidélité.");
        }

        // Calcul du montant de la réduction en fonction des points
        int reduction = (pointsFideliteUtilises / 100) * 10; // Chaque tranche de 100 points = 10 DT
        if (reduction > commande.getPrixTotalCommande()) {
            reduction = commande.getPrixTotalCommande(); // La réduction ne peut pas dépasser le total de la commande
        }

        // Appliquer la réduction
        commande.setPrixTotalCommande(commande.getPrixTotalCommande() - reduction);

        // Diminuer les points de fidélité du client
        user.setPointFidelityClient(user.getPointFidelityClient() - pointsFideliteUtilises);

        // Sauvegarder la commande et le client avec les modifications
        commandeRepository.save(commande);
        userRepository.save(user);

        System.out.println("Réduction appliquée : " + reduction + " DT");
    }
    public Commande getCommandeEnCours(Long clientId) {
        try {
            // Trouver le client par ID
            User user = userRepository.findById(clientId)
                    .orElseThrow(() -> new IllegalArgumentException("Client non trouvé avec l'ID " + clientId));

            // Récupérer toutes les commandes du client
            List<Commande> commandes = commandeRepository.findByUser(user);

            // Vérifier s'il existe une commande en cours
            Commande commandeEnCours = commandes.stream()
                    .filter(commande -> commande.getStatusCommande() == StatusCommande.Processing)
                    .findFirst()
                    .orElse(null);

            if (commandeEnCours == null) {
                throw new RuntimeException("Aucune commande en cours trouvée pour ce client");
            }

            return commandeEnCours;

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la récupération de la commande en cours : " + e.getMessage());
        }
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Client not found"));
    }
    public Long getUserIdByName(String name) {
        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new RuntimeException("Client not found"));
        return user.getId();
    }
    public List<Commande> getCommandesByClientId(Long clientId) {
        User user = userRepository.findById(clientId).orElse(null);

        if (user != null) {
            return user.getCommande().stream()
                    .filter(commande ->commande.getStatusCommande() != StatusCommande.Processing)
                    .collect(Collectors.toList());
        } else {
            return List.of();
        }


    }
    @Override
    public String changerStatutSiCancelled(Long idCommande) {
        Optional<Commande> optionalCommande = commandeRepository.findById(idCommande);

        if (optionalCommande.isPresent()) {
            Commande commande = optionalCommande.get();

            // Vérifier si le statut est CANCELLED
            if (commande.getStatusCommande() == StatusCommande.Cancelled) {
                commande.setStatusCommande(StatusCommande.Pending); // Changer le statut en PENDING
                commandeRepository.save(commande);
                return "Le statut de la commande a été changé en 'PENDING'.";
            } else {
                return "Le statut actuel de la commande n'est pas 'CANCELLED'.";
            }
        } else {
            return "Commande non trouvée.";
        }
    }
    public Map<String, Object> getCommandeStats() {
        List<Commande> commandes = commandeRepository.findAll();

        long totalCommandes = commandes.size();
        long pendingCount = commandes.stream().filter(c -> c.getStatusCommande() == StatusCommande.Pending).count();
        long cancelledCount = commandes.stream().filter(c -> c.getStatusCommande() == StatusCommande.Cancelled).count();
        long confirmedCount = commandes.stream().filter(c -> c.getStatusCommande() == StatusCommande.Confirmed).count();

        double pendingPercentage = (double) pendingCount / totalCommandes * 100;
        double cancelledPercentage = (double) cancelledCount / totalCommandes * 100;
        double confirmedPercentage = (double) confirmedCount / totalCommandes * 100;

        // Retourner les statistiques sous forme de Map
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCommandes", totalCommandes);
        stats.put("pendingCount", pendingCount);
        stats.put("cancelledCount", cancelledCount);
        stats.put("confirmedCount", confirmedCount);
        stats.put("pendingPercentage", pendingPercentage);
        stats.put("cancelledPercentage", cancelledPercentage);
        stats.put("confirmedPercentage", confirmedPercentage);

        return stats;
    }








}