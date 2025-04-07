package tn.esprit.panier.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Repositories.ArticleRepository;


import java.util.List;

@Service
@AllArgsConstructor

public class ArticleServicesImpl implements IArticleServices{

    @Autowired
    private ArticleRepository articleRepository;
    @Override
    public List<Article> retrieveAllArticle() {
        return articleRepository.findAll();
    }

    @Override
    public Article retrieveArticle(long idArticle) {
        return articleRepository.findById(idArticle).get();
    }

    @Override
    public Article addArticle(Article article) {
        return articleRepository.save(article);
    }
    public List<Article> searchByNom(String nom) {
        return articleRepository.findByNomContainingIgnoreCase(nom);
    }
    @Override
    public List<Article> searchByNomAndSortByPrixAsc(String nom) {
        return articleRepository.findByNomContainingIgnoreCaseOrderByPrixAsc(nom);
    }
}
