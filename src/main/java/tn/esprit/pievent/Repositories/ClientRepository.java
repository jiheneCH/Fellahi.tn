package tn.esprit.pievent.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pievent.Entities.Client;

@Repository
    public interface ClientRepository extends JpaRepository<Client, Long> {
    }


