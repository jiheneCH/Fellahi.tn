package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.repository.query.Param;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.Status;
import tn.esprit.pi_article.Entities.TypeProduit;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long > {
    List<Article> findByArchivedFalse();
    boolean existsByNom(String nom);
    List<Article> findTop3ByOrderByQuantiteVendueDesc();
    List<Article> findByStatusAndAlerteEnvoyeeFalse(Status status);
    List<Article> findTop5ByOrderByQuantiteVendueDesc();
    Optional<Article> findByArticlesPackIn(List<Long> articlesPackIds);




    // Recherche par nom (insensible à la casse)
    List<Article> findByNomContainingIgnoreCase(String nom);

    // Recherche par statut
    List<Article> findByStatus(Status status);

    // Recherche par typeProduit
    List<Article> findByTypeProduit(TypeProduit typeProduit);

    // Recherche par nom et statut
    List<Article> findByNomContainingIgnoreCaseAndStatus(String nom, Status status);

    // Recherche par nom et typeProduit
    List<Article> findByNomContainingIgnoreCaseAndTypeProduit(String nom, TypeProduit typeProduit);

    // Recherche par statut et typeProduit
    List<Article> findByStatusAndTypeProduit(Status status, TypeProduit typeProduit);

    // Recherche par nom, statut et typeProduit
    List<Article> findByNomContainingIgnoreCaseAndStatusAndTypeProduit(String nom, Status status, TypeProduit typeProduit);






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

    List<Article> findByUtilisateurId(Long utilisateurId);  // Trouver les articles par utilisateur
    List<Article> findTop3ByUtilisateurIdOrderByQuantiteVendueDesc(@Param("userId") Long userId);
    List<Article> findByUtilisateurIdAndArchivedFalse(Long utilisateurId);








}
