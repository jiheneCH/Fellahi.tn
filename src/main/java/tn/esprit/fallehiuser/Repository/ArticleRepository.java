package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tn.esprit.fallehiuser.model.Article;
import tn.esprit.fallehiuser.model.StatusAgri;
import tn.esprit.fallehiuser.model.TypeProduit;
import tn.esprit.fallehiuser.model.User;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long > {

    List<Article> findByArchivedFalse();
    boolean existsByNom(String nom);
    List<Article> findTop3ByOrderByQuantiteVendueDesc();
    List<Article> findByStatusAgriAndAlerteEnvoyeeFalse(StatusAgri statusAgri);
    List<Article> findTop5ByOrderByQuantiteVendueDesc();
    Optional<Article> findByArticlesPackIn(List<Long> articlesPackIds);




    // Recherche par nom (insensible à la casse)
    List<Article> findByNomContainingIgnoreCase(String nom);

    // Recherche par statut
    List<Article> findByStatusAgri(StatusAgri statusAgri);

    // Recherche par typeProduit
    List<Article> findByTypeProduit(TypeProduit typeProduit);

    // Recherche par nom et statut
    List<Article> findByNomContainingIgnoreCaseAndStatusAgri(String nom, StatusAgri statusAgri);

    // Recherche par nom et typeProduit
    List<Article> findByNomContainingIgnoreCaseAndTypeProduit(String nom, TypeProduit typeProduit);

    // Recherche par statut et typeProduit
    List<Article> findByStatusAgriAndTypeProduit(StatusAgri statusAgri, TypeProduit typeProduit);

    // Recherche par nom, statut et typeProduit
    List<Article> findByNomContainingIgnoreCaseAndStatusAgriAndTypeProduit(String nom, StatusAgri statusAgri, TypeProduit typeProduit);






    @Query("SELECT SUM(a.prix * a.quantiteVendue) FROM Article a")
    Double getChiffreAffairesTotal();
    @Query("SELECT DATE(a.dateAjout), COUNT(a) FROM Article a GROUP BY DATE(a.dateAjout)")
    List<Object[]> countAjoutsParJour();

    @Query("SELECT a, (a.prix * a.quantiteVendue) FROM Article a")
    List<Object[]> getChiffreAffairesParArticle();

    @Query("SELECT SUM(a.quantiteVendue) FROM Article a")
    Double getQuantiteTotalVendue();
    @Query("SELECT a.typeProduit, SUM(a.prix * a.quantiteVendue) FROM Article a GROUP BY a.typeProduit")
    List<Object[]> getChiffreAffairesParCategorie();
    @Query("SELECT FUNCTION('MONTH', a.dateAjout), COUNT(a) " +
            "FROM Article a GROUP BY FUNCTION('MONTH', a.dateAjout) ORDER BY FUNCTION('MONTH', a.dateAjout)")
    List<Object[]> getAjoutsParMois();

   // List<Article> findByUtilisateurId(Long utilisateurId);  // Trouver les articles par utilisateur
   // List<Article> findTop3ByUtilisateurIdOrderByQuantiteVendueDesc(@Param("userId") Long userId);
    // List<Article> findByUtilisateurIdAndArchivedFalse(Long utilisateurId);



    List<Article> findByUser(User user);
    List<Article> findAll();
    List<Article> findByUserAndArchivedFalse(User user);

    List<Article> findByStatusAgriIn(List<StatusAgri> statuses);







}
