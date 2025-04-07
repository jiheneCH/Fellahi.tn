package tn.esprit.panier.Services;

import tn.esprit.panier.Entities.Article;
import tn.esprit.panier.Entities.Client;

import java.util.List;

public interface IClientServices {
    public List<Client> retrieveAllClient();
    public Client retrieveClient(long idClient);
    public Client addClient(Client client);
}
