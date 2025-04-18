package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.WaitingListEntry;

import java.util.List;

public interface WaitingListEntryRepository extends JpaRepository<WaitingListEntry, Long> {



    List<WaitingListEntry> findTop5ByEventOrderByRequestTimeAsc(Event event);

    List<WaitingListEntry> findByEventOrderByRequestTimeAsc(Event event);

    List<WaitingListEntry> findByReferenceContainingIgnoreCase(String reference);

    @Query("SELECT w FROM WaitingListEntry w JOIN FETCH w.event JOIN FETCH w.user")
    List<WaitingListEntry> findAllWithClientAndEvent();
}

