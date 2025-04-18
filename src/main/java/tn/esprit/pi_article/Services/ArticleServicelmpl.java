package tn.esprit.pi_article.Services;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;


import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pi_article.Entities.*;
import tn.esprit.pi_article.Repositories.ArticleRepository;
import tn.esprit.pi_article.Repositories.UserRepository;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
@CrossOrigin(origins = "*") // Permet les requêtes CORS depuis n'importe quelle origine (utile en développement)


@AllArgsConstructor
@Service
public class ArticleServicelmpl implements IArticleServices {

    @Autowired
    ArticleRepository articleRepository;
    private final Random random = new Random();
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Article> retriveAllArticle() {
        try {
            // Assurez-vous que cette méthode renvoie une liste d'articles correcte
            return articleRepository.findAll();  // ou toute autre méthode selon votre logique
        } catch (Exception e) {
            // Si une exception se produit, la loguer et la re-throw pour la gestion d'erreurs
            System.err.println("Erreur lors de la récupération des articles : " + e.getMessage());
            throw new RuntimeException("Erreur lors de la récupération des articles", e);
        }
    }

    @Override
    public Article retriveArticle(long idArticle) {

        Article article = articleRepository.findById(idArticle)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));

        if (article.getStatusAgri() == StatusAgri.archive.archive) {
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
        existingArticle.setStatusAgri(getStatusByQuantity(existingArticle.getQuantiteDisponible(), existingArticle.getQuantiteInitiale()));

        // Calcul des points de fidélité (si nécessaire)
        existingArticle.calculerPointsFidelite();



        // Sauvegarde de l'article après mise à jour
        return articleRepository.save(existingArticle);
    }







/*
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
    }
*/




@Override
@Transactional
public void archiverArticle(Long id) {
    Article article = articleRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("L'article avec l'ID " + id + " n'existe pas."));

    if (article.isArchived()) {
        throw new RuntimeException("L'article avec l'ID " + id + " est déjà archivé.");
    }

    article.setArchived(true);
    article.setStatusAgri(StatusAgri.archive);

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

    /*
   @Override
    public Article addArticle(Article article) {
        if (existsByNom(article.getNom())) {
            throw new RuntimeException("Un article avec ce nom existe déjà.");
        }
        return articleRepository.save(article);
    }
*/

/*
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


     article.setReference(genererReferenceArticle(article.getNom()));
     article.calculerPointsFidelite();
     article.setStatusAgri(getStatusByQuantity(article.getQuantiteDisponible(), article.getQuantiteInitiale()));
     article.setDateAjout(LocalDate.now());
     /*if (article.getUtilisateur().getId() == null) {
         throw new IllegalArgumentException("L'agriculteur doit être spécifié");
     }*/


    /////////////////////////////////////

    /*
////////////////////////////////////
     ajusterPrix(article);




     // Sauvegarde de l'article après validation
     return articleRepository.save(article);
 }


*/


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




    private StatusAgri getStatusByQuantity(int quantiteDisponible, int quantiteInitiale) {
        if (quantiteDisponible == 0) {
            return StatusAgri.OutOfStock;
        }

        double ratio = (double) quantiteDisponible / quantiteInitiale;

        if (ratio > 0.2) {
            return StatusAgri.InStock;
        } else {
            return StatusAgri.LowStock;
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
        StatusAgri statutPack = getStatusByQuantity(quantitePack, quantitePack);
        System.out.println("🏷️ Statut du pack : " + statutPack);

        // Création du pack
        Article pack = new Article();
        pack.setNom(nomPack);
        pack.setReference(referencePack);
        pack.setPrix(prixTotal);
        pack.setQuantiteDisponible(quantitePack);
        pack.setStatusAgri(statutPack);
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
        List<Article> articlesFaibles = articleRepository.findByStatusAgriIn(List.of(StatusAgri.LowStock, StatusAgri.OutOfStock));

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

    public List<Article> rechercherArticles(String nom, StatusAgri statusAgri, TypeProduit typeProduit) {
        if (nom != null && statusAgri != null && typeProduit != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndStatusAgriAndTypeProduit(nom, statusAgri, typeProduit);
        } else if (nom != null && statusAgri != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndStatusAgri(nom, statusAgri);
        } else if (nom != null && typeProduit != null) {
            return articleRepository.findByNomContainingIgnoreCaseAndTypeProduit(nom, typeProduit);
        } else if (statusAgri != null && typeProduit != null) {
            return articleRepository.findByStatusAgriAndTypeProduit(statusAgri, typeProduit);
        } else if (nom != null) {
            return articleRepository.findByNomContainingIgnoreCase(nom);
        } else if (statusAgri != null) {
            return articleRepository.findByStatusAgri(statusAgri);
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

        // Extraire top 3
        List<Article> top3 = new ArrayList<>();
        int max = Math.min(3, articlesRecents.size());
        for (int i = 0; i < max; i++) {
            top3.add(articlesRecents.get(i));
        }

        return top3;
    }
    public List<Object[]> getAjoutsParJour() {
        return articleRepository.countAjoutsParJour();
    }
    public Double getChiffreAffairesTotal() {
        return articleRepository.getChiffreAffairesTotal();
    }
    public Map<String, Object> getChiffreAffairesParArticle() {
        List<Object[]> result = articleRepository.getChiffreAffairesParArticle();

        // On initialise une variable pour stocker le total
        double totalChiffreAffaires = 0.0;

        // On crée une liste pour stocker le chiffre d'affaire de chaque article
        List<Map<String, Object>> articlesChiffreAffaires = new ArrayList<>();

        // On parcourt les résultats et on calcule le chiffre d'affaire par article
        for (Object[] row : result) {
            Article article = (Article) row[0];
            double chiffreAffairesArticle = (double) row[1];
            totalChiffreAffaires += chiffreAffairesArticle;

            // On crée un map pour chaque article
            Map<String, Object> articleData = new HashMap<>();
            articleData.put("article", article);
            articleData.put("chiffreAffaires", chiffreAffairesArticle);

            articlesChiffreAffaires.add(articleData);
        }

        // On prépare la réponse avec les données des articles et le total
        Map<String, Object> response = new HashMap<>();
        response.put("articles", articlesChiffreAffaires);
        response.put("totalChiffreAffaires", totalChiffreAffaires);

        return response;
    }
    public Double getQuantiteTotalVendue() {
        return articleRepository.getQuantiteTotalVendue();
    }
    public List<Map<String, Object>> getChiffreAffairesParCategorie() {
        List<Object[]> result = articleRepository.getChiffreAffairesParCategorie();
        List<Map<String, Object>> response = new ArrayList<>();
        for (Object[] row : result) {
            Map<String, Object> data = new HashMap<>();
            data.put("categorie", row[0]);
            data.put("chiffreAffaires", row[1]);
            response.add(data);
        }
        return response;
    }
    public List<Map<String, Object>> getAjoutsParMois() {
        List<Object[]> result = articleRepository.getAjoutsParMois();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : result) {
            Map<String, Object> data = new HashMap<>();
            Integer monthNumber = (Integer) row[0];
            Long count = (Long) row[1];

            String monthName = getMonthName(monthNumber);

            data.put("mois", monthName);
            data.put("nombreAjouts", count);

            response.add(data);
        }

        return response;
    }

    private String getMonthName(int monthNumber) {
        String[] mois = {
                "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
                "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
        };
        return mois[monthNumber - 1]; // -1 car les tableaux commencent à 0
    }
    /*
    public List<Article> getArticlesByUtilisateurId(Long id) {
        return articleRepository.findByUtilisateurId(id);
    }
    // Trouver un agriculteur par son nom et obtenir ses articles

    */
    /*
    public List<Article> getArticlesParUtilisateur(Long id) {
        utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        return articleRepository.findByUtilisateurIdAndArchivedFalse(utilisateur.getId());
    }
    */


    /*

    public Article ajouterArticleparuser(Long id, Article article) {
        utilisateur utilisateur = utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        if (existsByNom(article.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un article avec ce nom existe déjà.");
        }

        if (article.getQuantiteDisponible() == null || article.getQuantiteDisponible() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité disponible ne peut pas être négative ou nulle.");
        }

        if (article.getQuantiteVendue() == null || article.getQuantiteVendue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas être négative.");
        }

        article.setReference(genererReferenceArticle(article.getNom()));
        article.calculerPointsFidelite();
        article.setStatusAgri(getStatusByQuantity(article.getQuantiteDisponible(), article.getQuantiteInitiale()));
        article.setDateAjout(LocalDate.now());
        article.setUtilisateur(utilisateur);
        ajusterPrix(article);

        return articleRepository.save(article);
    }

*/



    public List<Article> getArticlesParUtilisateur(Long id) {
        // Trouver l'utilisateur par son ID
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur est un agriculteur
        if (user.getRole() == null || user.getRole().getRoleName() != RoleName.FARMER) {
            throw new RuntimeException("L'utilisateur n'est pas un agriculteur.");
        }

        // Récupérer uniquement les articles non archivés associés à l'utilisateur
        return articleRepository.findByUserAndArchivedFalse(user);}
    public Article ajouterArticlePourUtilisateur(Long idUtilisateur, Article article) {
        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si c'est un agriculteur
        if (user.getRole() == null || user.getRole().getRoleName() != RoleName.FARMER) {
            throw new RuntimeException("L'utilisateur n'est pas un agriculteur.");
        }

        // Vérifier si un article avec le même nom existe déjà
        if (articleRepository.existsByNom(article.getNom())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Un article avec ce nom existe déjà.");
        }

        // Vérifier que la quantité disponible est correcte
        if (article.getQuantiteDisponible() == null || article.getQuantiteDisponible() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité disponible ne peut pas être négative ou nulle.");
        }

        // Vérifier que la quantité vendue est correcte
        if (article.getQuantiteVendue() == null || article.getQuantiteVendue() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La quantité vendue ne peut pas être négative.");
        }

        // Générer la référence
        article.setReference(genererReferenceArticle(article.getNom()));

        // Calculer les points de fidélité
        article.calculerPointsFidelite();

        // Déterminer le statut
        article.setStatusAgri(getStatusByQuantity(article.getQuantiteDisponible(), article.getQuantiteInitiale()));

        // Définir la date d'ajout
        article.setDateAjout(LocalDate.now());

        // Associer l'article à l'utilisateur
        article.setUser(user); // Ou setUtilisateur si tu préfères ce nom

        // Ajuster le prix
        ajusterPrix(article);

        // Sauvegarder l'article
        return articleRepository.save(article);
    }

    @Transactional
    public Article genererPackSiNecessaireeee(Long idUtilisateur) {
        System.out.println("🚀 Début de la génération du pack...");

        // Vérifier si l'utilisateur existe
        User user = userRepository.findById(idUtilisateur)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        // Vérifier si l'utilisateur est un agriculteur
        if (user.getRole() == null || user.getRole().getRoleName() != RoleName.FARMER) {
            throw new RuntimeException("L'utilisateur n'est pas un agriculteur.");
        }

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
        StatusAgri statutPack = getStatusByQuantity(quantitePack, quantitePack);
        System.out.println("🏷️ Statut du pack : " + statutPack);

        // Création du pack
        Article pack = new Article();
        pack.setNom(nomPack);
        pack.setReference(referencePack);
        pack.setPrix(prixTotal);
        pack.setQuantiteDisponible(quantitePack);
        pack.setStatusAgri(statutPack);
        pack.setPack(true);
        pack.setQuantiteVendue(0);
        pack.setArticlesPack(articlesPackIds);
        pack.setDateAjout(LocalDate.now());

        // Calculer les points de fidélité
        pack.calculerPointsFidelite();

        // Associer le pack à l'utilisateur (agriculteur)
        pack.setUser(user); // Ou setUtilisateur si tu préfères ce nom

        // Sauvegarder le pack
        System.out.println("✅ Pack généré avec succès !");
        return articleRepository.save(pack);
    }

    // Méthode pour récupérer les articles avec un stock faible ou épuisé
    public List<Article> getArticlesLowStockAndOutOfStock() {
        return articleRepository.findByStatusAgriIn(List.of(StatusAgri.LowStock, StatusAgri.OutOfStock));
    }

    // Cette méthode récupère une page d'articles avec pagination et tri
    public Page<Article> getArticles(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return articleRepository.findAll(pageRequest);
    }
}

