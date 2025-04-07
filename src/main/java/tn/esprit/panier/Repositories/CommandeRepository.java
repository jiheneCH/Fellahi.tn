package tn.esprit.panier.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import tn.esprit.panier.Entities.Client;
import tn.esprit.panier.Entities.Commande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByClient(Client client);

}
