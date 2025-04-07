package tn.esprit.panier.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.panier.Entities.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByNomContainingIgnoreCase(String nom);
    List<Article> findByNomContainingIgnoreCaseOrderByPrixAsc(String nom);}
