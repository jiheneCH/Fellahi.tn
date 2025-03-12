package tn.esprit.pi_article.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.pi_article.Entities.Article;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long > {
    List<Article> findByArchivedFalse();
    boolean existsByNom(String nom);

}
