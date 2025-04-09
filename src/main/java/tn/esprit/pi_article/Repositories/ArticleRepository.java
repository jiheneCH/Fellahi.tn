package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.pi_article.Entities.Article;
import tn.esprit.pi_article.Entities.Status;
import tn.esprit.pi_article.Entities.TypeProduit;

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











}
