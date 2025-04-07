package tn.esprit.panier.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.panier.Entities.Client;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
