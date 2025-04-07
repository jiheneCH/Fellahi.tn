package tn.esprit.panier.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.Client;
import tn.esprit.panier.Services.IArticleServices;
import tn.esprit.panier.Services.IClientServices;

import java.util.List;
@RestController
@AllArgsConstructor
@RequestMapping("/client")
@Tag(name= "Gestion des clients")
public class ClientRestController {

    @Autowired
    IClientServices iClientServices;
    @Operation(description = "affichage de toutes les Clients")

    @GetMapping("/retrieveAllClient")
    public List<Client> afficherClient(){
        return iClientServices.retrieveAllClient();

    }
    @Operation(description = "affichage du Client par ID")
    @GetMapping("/retrieveClient/{idClient}")
    public Client afficherClient(@PathVariable("idClient") long idClient){
        return iClientServices.retrieveClient(idClient);
    }
    @Operation(description = "ajouter Client")

    @PostMapping("/addClient")
    public Client ajouterClient(@RequestBody Client client){
        return iClientServices.addClient(client);
    }
}
