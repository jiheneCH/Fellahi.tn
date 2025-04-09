package tn.esprit.pi_article.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.Status;
import tn.esprit.pi_article.Entities.TypeProduit;
import tn.esprit.pi_article.Repositories.ArticleRepository;

import java.lang.module.ResolutionException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

@AllArgsConstructor
@Service
public class ArticleServicelmpl implements IArticleServices {
    @Autowired
    ArticleRepository articleRepository;
    private final Random random = new Random();


    @Override
    public List<Article> retriveAllArticle() {
        // Récupérer tous les articles non archivés
        List<Article> articles = articleRepository.findByArchivedFalse();

        // Appliquer les promotions (Black Friday et fin d'année)
        for (Article article : articles) {
            appliquerPromotion(article);
        }

        return articles;
    }

    @Override
    public Article retriveArticle(long idArticle) {

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        if (article.getStatus() == Status.archive) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access to this article is forbidden");
        }

        return article;}


    @Override
    public Article updateArticle(Article article) {
        // Vérifier que l'article n'est pas null et que l'ID est valide
        if (article == null || article.getIdArticle() == 0) {
            throw new IllegalArgumentException("L'article ne peut pas être null ou avoir un ID invalide.");
        }

        // Vérifier si l'article existe dans la base de données
        Article existingArticle = articleRepository.findById(article.getIdArticle())
                .orElseThrow(() -> new RuntimeException("Article non trouvé avec ID : " + article.getIdArticle()));

        // Afficher le nom de l'article avant la mise à jour (pour les logs)
        System.out.println("Mise à jour de l'article : " + existingArticle.getNom());

        // Mise à jour des champs de l'article
        existingArticle.setNom(article.getNom());
        existingArticle.setPrix(article.getPrix());
        existingArticle.setQuantiteDisponible(article.getQuantiteDisponible());
        existingArticle.setQuantiteVendue(article.getQuantiteVendue());
        existingArticle.setReduction(article.getReduction());
        existingArticle.setTypeProduit(article.getTypeProduit());
        existingArticle.setQuantiteInitiale(article.getQuantiteInitiale());  // Mise à jour de la quantité initiale


        // Mise à jour du statut de l'article en fonction de la quantité disponible et initiale
        existingArticle.setStatus(getStatusByQuantity(existingArticle.getQuantiteDisponible(), existingArticle.getQuantiteInitiale()));

        // Calcul des points de fidélité (si nécessaire)
        existingArticle.calculerPointsFidelite();

        // Générer un pack si nécessaire (si la logique le requiert)
        genererPackSiNecessaire();

        // Sauvegarde de l'article après mise à jour
        return articleRepository.save(existingArticle);
    }








    @Override
    public void deleteArticle(long idArticle) {
        articleRepository.deleteById(idArticle);



    }
/*
    @Override
    public void archiverArticle(Long id) {
        Article article = articleRepository.findById(id).get();
        article.setArchived(true);
        article.setStatus(Status.archive);
        articleRepository.save(article);
    }*/
@Override
@Transactional
public void archiverArticle(Long id) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("L'article avec l'ID " + id + " n'existe pas."));

    if (article.isArchived()) {
        throw new RuntimeException("L'article avec l'ID " + id + " est déjà archivé.");
    }

    article.setArchived(true);
    article.setStatus(Status.archive);

    try {
        articleRepository.save(article);
    } catch (Exception e) {
        throw new RuntimeException("Erreur lors de l'archivage de l'article : " + e.getMessage());
    }
}



    @Override
    public boolean existsByNom(String nom) {
        return articleRepository.existsByNom(nom);
    }

 /*   @Override
    public Article addArticle(Article article) {
        if (existsByNom(article.getNom())) {
            throw new RuntimeException("Un article avec ce nom existe déjà.");
        }
        return articleRepository.save(article);
    }*/
 @Override
 public Article addArticle(Article article) {
     // Vérification si le nom existe déjà
     if (existsByNom(article.getNom())) {
         throw new ResponseStatusException(HttpStatus.CONFLICT, "Un article avec ce nom existe déjà.");
     }

     // Vérification des quantités
     if (article.getQuantiteDisponible() == null || article.getQuantiteDisponible() < 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité disponible ne peut pas être négative ou nulle.");
     }

     if (article.getQuantiteVendue() == null || article.getQuantiteVendue() < 0) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas être négative.");
     }

     if (article.getQuantiteVendue() > article.getQuantiteDisponible()) {
         throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas dépasser la quantité disponible.");
     }
     article.setReference(genererReferenceArticle(article.getNom()));
     article.calculerPointsFidelite();
     article.setStatus(getStatusByQuantity(article.getQuantiteDisponible(), article.getQuantiteInitiale()));
     article.setDateAjout(LocalDate.now());
     if (article.getUtilisateur().getId() == null) {
         throw new IllegalArgumentException("L'agriculteur doit être spécifié");
     }

     ajusterPrix(article);




     // Sauvegarde de l'article après validation
     return articleRepository.save(article);
 }





    public String genererReferenceArticle(String nomArticle) {
        // Extraire les 3 premières lettres du nom de l'article (en majuscules)
        String prefix = nomArticle.length() >= 3 ? nomArticle.substring(0, 3).toUpperCase() : nomArticle.toUpperCase();

        // Obtenir la date du jour au format YYYYMMDD
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());

        // Générer un nombre aléatoire entre 1000 et 9999
        int randomNumber = 1000 + random.nextInt(9000);

        // Construire la référence unique
        return prefix + "-" + date + "-" + randomNumber;
    }
    private Status getStatusByQuantity(int quantiteDisponible, int quantiteInitiale) {
        if (quantiteDisponible == 0) {
            return Status.OutOfStock;
        }

        double ratio = (double) quantiteDisponible / quantiteInitiale;

        if (ratio > 0.2) {
            return Status.InStock;
        } else {
            return Status.LowStock;
        }
    }


    @Transactional
    public Article genererPackSiNecessaire() {
        System.out.println("🚀 Début de la génération du pack...");

        // Récupérer les 3 articles les plus vendus
        List<Article> articlesPlusVendus = articleRepository.findTop3ByOrderByQuantiteVendueDesc();

        // Vérifier qu'il y a au moins 2 articles
        if (articlesPlusVendus.size() < 2) {
            System.out.println("⚠ Pas assez d'articles populaires pour générer un pack !");
            throw new RuntimeException("Pas assez d'articles vendus pour générer un pack.");
        }

        // Générer un nom unique pour le pack en concaténant les noms des articles
        StringBuilder nomPackBuilder = new StringBuilder("Pack-");
        for (int i = 0; i < articlesPlusVendus.size(); i++) {
            nomPackBuilder.append(articlesPlusVendus.get(i).getNom());
            if (i < articlesPlusVendus.size() - 1) {
                nomPackBuilder.append("-");
            }
        }
        String nomPack = nomPackBuilder.toString();
        System.out.println("🛒 Nom du pack généré: " + nomPack);

        // Vérifier si un pack avec les mêmes articles existe déjà
        List<Long> articlesPackIds = new ArrayList<>();
        for (Article article : articlesPlusVendus) {
            articlesPackIds.add(article.getIdArticle());
        }
        Collections.sort(articlesPackIds); // Trier les IDs pour la comparaison

        Optional<Article> packExist = articleRepository.findByArticlesPackIn(articlesPackIds);
        if (packExist.isPresent()) {
            System.out.println("⚠ Un pack avec les mêmes articles existe déjà. Aucun pack n'a été créé.");
            return packExist.get(); // Retourner le pack existant
        }

        // Générer une référence unique pour le pack
        String referencePack = genererReferenceArticle(nomPack);
        System.out.println("🔖 Référence du pack générée: " + referencePack);

        // Calcul du prix total avec réduction de 10%
        double prixTotal = 0.0;
        for (Article article : articlesPlusVendus) {
            prixTotal += article.getPrix();
        }
        prixTotal *= 0.9;
        System.out.println("💰 Prix du pack après réduction: " + prixTotal);

        // Déterminer la quantité du pack (minimum des stocks disponibles)
        int quantitePack = Integer.MAX_VALUE;
        for (Article article : articlesPlusVendus) {
            if (article.getQuantiteDisponible() < quantitePack) {
                quantitePack = article.getQuantiteDisponible();
            }
        }

        // Vérifier si la quantité est 0
        if (quantitePack == 0) {
            System.out.println("❌ Aucune quantité disponible pour générer un pack !");
            throw new RuntimeException("Impossible de créer un pack avec quantité 0.");
        }
        System.out.println("📦 Quantité disponible pour le pack: " + quantitePack);

        // Déterminer le statut en fonction de la quantité disponible
        Status statutPack = getStatusByQuantity(quantitePack, quantitePack);
        System.out.println("🏷️ Statut du pack : " + statutPack);

        // Création du pack
        Article pack = new Article();
        pack.setNom(nomPack);
        pack.setReference(referencePack);
        pack.setPrix(prixTotal);
        pack.setQuantiteDisponible(quantitePack);
        pack.setStatus(statutPack);
        pack.setPack(true);
        pack.setQuantiteVendue(0);
        pack.setArticlesPack(articlesPackIds);
        pack.setDateAjout(LocalDate.now());


        // Calculer les points de fidélité
        pack.calculerPointsFidelite();

        System.out.println("✅ Pack généré avec succès !");
        return articleRepository.save(pack);
    }


    public List<Article> verifierSeuilCritiqueEtEnvoyerAlerte() {
        List<Article> articlesFaibles = articleRepository.findByStatusAndAlerteEnvoyeeFalse(Status.LowStock);

        for (Article article : articlesFaibles) {
            System.out.println("⚠ Alerte : Stock faible pour l'article : " + article.getNom());

            // Remettre `alerteEnvoyee = false` pour que l'alerte soit toujours envoyée tant que le stock est faible
            article.setAlerteEnvoyee(false);
            articleRepository.save(article);
        }

        return articlesFaibles;
    }


    @Scheduled(fixedRate = 60000) // Vérification toutes les 60 secondes
    public void verifierSeuils() {
        System.out.println("🔄 Vérification des seuils critiques...");
        verifierSeuilCritiqueEtEnvoyerAlerte(); // Appel direct sans articleService
    }

    public List<Article> getArticlesTendance() {
        return articleRepository.findTop5ByOrderByQuantiteVendueDesc();
    }

    public void ajusterPrix(Article article) {
        Integer quantiteInitiale = article.getQuantiteInitiale();
        Integer quantiteDisponible = article.getQuantiteDisponible();

        if (quantiteInitiale == null || quantiteDisponible == null || quantiteInitiale == 0) {
            return; // Éviter la division par 0
        }

        double pourcentageRestant = (quantiteDisponible * 100.0) / quantiteInitiale;
        double nouveauPrix = article.getPrix();

        if (pourcentageRestant <= 20) {
            nouveauPrix *= 0.90; // Réduction de 10%
        } else if (pourcentageRestant >= 70) {
            nouveauPrix *= 1.05; // Augmentation de 5%
        }

        article.setPrix(nouveauPrix);
    }

    public List<Article> rechercherArticles(String nom, Status status, TypeProduit typeProduit) {
        if (nom != null && status != null && typeProduit != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndStatusAndTypeProduit(nom, status, typeProduit);
        } else if (nom != null && status != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndStatus(nom, status);
        } else if (nom != null && typeProduit != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndTypeProduit(nom, typeProduit);
        } else if (status != null && typeProduit != null) {
            return articleRepository.findByStatusAndTypeProduit(status, typeProduit);
        } else if (nom != null) {
            return articleRepository.findByNomContainingIgnoreCase(nom);
        } else if (status != null) {
            return articleRepository.findByStatus(status);
        } else if (typeProduit != null) {
            return articleRepository.findByTypeProduit(typeProduit);
        }

        // Si aucun paramètre n'est fourni, retourner tous les articles
        return articleRepository.findAll();
    }





    // Récupère tous les articles
    public List<Article> getArticles() {
        // Supposons que tu as une méthode pour obtenir la liste des articles
        List<Article> articles = articleRepository.findAll();

        // Appliquer les réductions aux articles
        for (Article article : articles) {
            // Appliquer la promotion si la date actuelle est dans la période des fêtes de fin d'année
            appliquerPromotion(article);
        }

        return articles;
    }

    // Méthode qui applique la promotion en fonction des dates spécifiques
    public void appliquerPromotion(Article article) {
        // Si aujourd'hui est entre le 27 et le 31 décembre, appliquer une réduction de 25%
        LocalDate aujourdHui =LocalDate.now();


        // Fêtes de fin d'année (27 décembre au 31 décembre)
        LocalDate debutFeteFinAnnee = LocalDate.of(aujourdHui.getYear(), 12, 27);
        LocalDate finFeteFinAnnee = LocalDate.of(aujourdHui.getYear(), 12, 31);

        // Black Friday (quatrième jeudi de novembre)
        LocalDate blackFriday = getBlackFriday(aujourdHui.getYear());

        if ((aujourdHui.isEqual(debutFeteFinAnnee) || aujourdHui.isAfter(debutFeteFinAnnee)) &&
                (aujourdHui.isEqual(finFeteFinAnnee) || aujourdHui.isBefore(finFeteFinAnnee))) {
            // Si aujourd'hui est dans la période des fêtes de fin d'année
            article.setPromotion(25.0); // Appliquer une réduction de 25%
        } else if (aujourdHui.isEqual(blackFriday) || aujourdHui.isAfter(blackFriday)) {
            // Si aujourd'hui est Black Friday
            article.setPromotion(50.0); // Exemple : une réduction de 50% pour Black Friday
        } else {
            // Si aucune promotion n'est active
            article.setPromotion(0.0); // Aucune réduction
        }

        // Calculer le prix final après application de la promotion
        article.setPrixFinal(calculerPrixFinal(article));
    }

    // Méthode pour calculer le prix final avec réduction
    public Double calculerPrixFinal(Article article) {
        if (article.getPromotion() > 0) {
            return article.getPrix() * (1 - article.getPromotion() / 100); // Appliquer la réduction
        } else {
            return article.getPrix(); // Aucun changement si aucune promotion
        }
    }

    // Méthode pour récupérer le Black Friday de l'année en cours
    private LocalDate getBlackFriday(int year) {
        // Le Black Friday est le quatrième jeudi de novembre
        LocalDate firstDayOfNovember = LocalDate.of(year, 11, 1);
        int dayOfWeek = firstDayOfNovember.getDayOfWeek().getValue(); // Jour de la semaine du 1er novembre

        // Calculer le quatrième jeudi de novembre
        int daysToAdd = (4 - dayOfWeek + 7) % 7 + 21; // 21 jours + ajustement pour le jeudi
        return firstDayOfNovember.plusDays(daysToAdd);
    }
    @Override
    public List<Article> getArticlesTendanceeee() {
        List<Article> tousLesArticles = articleRepository.findAll(); // récupère tout
        List<Article> articlesRecents = new ArrayList<>();

        LocalDate dateLimite = LocalDate.now().minusDays(30);

        // Filtrage manuel des articles récents
        for (Article article : tousLesArticles) {
            if (article.getDateAjout() != null && article.getDateAjout().isAfter(dateLimite)) {
                articlesRecents.add(article);
            }
        }

        // Tri manuel par quantité vendue décroissante
        for (int i = 0; i < articlesRecents.size() - 1; i++) {
            for (int j = i + 1; j < articlesRecents.size(); j++) {
                if (articlesRecents.get(j).getQuantiteVendue() > articlesRecents.get(i).getQuantiteVendue()) {
                    Article temp = articlesRecents.get(i);
                    articlesRecents.set(i, articlesRecents.get(j));
                    articlesRecents.set(j, temp);
                }
            }
        }

        // Extraire top 5
        List<Article> top5 = new ArrayList<>();
        int max = Math.min(5, articlesRecents.size());
        for (int i = 0; i < max; i++) {
            top5.add(articlesRecents.get(i));
        }

        return top5;
    }


}
