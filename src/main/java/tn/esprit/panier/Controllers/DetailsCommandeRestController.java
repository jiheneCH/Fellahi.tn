package tn.esprit.panier.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.DetailsCommande;
import tn.esprit.panier.Services.IArticleServices;
import tn.esprit.panier.Services.ICommandeServices;
import tn.esprit.panier.Services.IDetailsCommandeServices;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/detailscommande")
public class DetailsCommandeRestController {
    @Autowired
    IDetailsCommandeServices iDetailsCommandeServices;


    @GetMapping("/retrieveAllArticle")
    public List<DetailsCommande> afficherDetailsCommande(){
        return iDetailsCommandeServices.retrieveAllDetailsCommande();

    }


}
