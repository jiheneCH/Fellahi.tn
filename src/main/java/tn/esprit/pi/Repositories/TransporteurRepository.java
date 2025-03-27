package tn.esprit.pi.Repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.pi.Entities.Transporteur;

@Repository
public interface TransporteurRepository extends JpaRepository<Transporteur, Long> {

}
