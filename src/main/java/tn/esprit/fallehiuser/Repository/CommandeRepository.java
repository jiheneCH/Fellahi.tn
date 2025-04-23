package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fallehiuser.model.Commande;
import tn.esprit.fallehiuser.model.User;


import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    List<Commande> findByUser(User user);

}
