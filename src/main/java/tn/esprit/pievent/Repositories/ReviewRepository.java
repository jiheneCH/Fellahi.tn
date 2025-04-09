package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Review;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByEvent(Event event);
}
