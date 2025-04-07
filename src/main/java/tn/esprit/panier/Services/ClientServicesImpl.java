package tn.esprit.panier.Services;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.panier.Entities.Client;
import tn.esprit.panier.Repositories.ArticleRepository;
import tn.esprit.panier.Repositories.ClientRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServicesImpl implements IClientServices{

    @Autowired
    private ClientRepository clientRepository;
    @Override
    public List<Client> retrieveAllClient() {
        return clientRepository.findAll();
    }

    @Override
    public Client retrieveClient(long idClient) {
        return clientRepository.findById(idClient).get();
    }

    @Override
    public Client addClient(Client client) {
        return clientRepository.save(client);
    }
}
