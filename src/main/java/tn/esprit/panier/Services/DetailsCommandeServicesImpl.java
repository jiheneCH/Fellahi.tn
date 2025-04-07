package tn.esprit.panier.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.Commande;
import tn.esprit.panier.Entities.DetailsCommande;
import tn.esprit.panier.Repositories.ArticleRepository;
import tn.esprit.panier.Repositories.CommandeRepository;
import tn.esprit.panier.Repositories.DetailsCommandeRepository;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DetailsCommandeServicesImpl implements IDetailsCommandeServices {
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private DetailsCommandeRepository detailsCommandeRepository;

    @Override
    public List<DetailsCommande> retrieveAllDetailsCommande() {
        return detailsCommandeRepository.findAll();
    }



}

