package tn.esprit.fallehiuser.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.Repository.ArticleRepository;
import tn.esprit.fallehiuser.Repository.CommandeRepository;
import tn.esprit.fallehiuser.Repository.DetailsCommandeRepository;
import tn.esprit.fallehiuser.model.DetailsCommande;


import java.util.List;

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

