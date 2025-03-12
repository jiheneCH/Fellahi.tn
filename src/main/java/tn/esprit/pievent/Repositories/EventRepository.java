package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pievent.Entities.Event;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

          List<Event> findByArchivedFalse();

    }
