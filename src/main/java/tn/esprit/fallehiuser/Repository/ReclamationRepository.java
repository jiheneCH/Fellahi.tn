package tn.esprit.fallehiuser.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.fallehiuser.model.Reclamation;
import tn.esprit.fallehiuser.model.User;
import tn.esprit.fallehiuser.model.ReclamationStatus;

import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {

    List<Reclamation> findByStatus(ReclamationStatus status);
    List<Reclamation> findByUser(User user);
    List<Reclamation> findBySubject(String subject);

    // Method to find reclamations ordered by 'createdAt' in descending order (latest first)
    List<Reclamation> findAllByOrderByCreatedAtDesc();

    // Method to find reclamations ordered by 'createdAt' in ascending order (oldest first)
    List<Reclamation> findAllByOrderByCreatedAtAsc();
}
