package tn.esprit.pievent.Repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.pievent.Entities.Event;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

          List<Event> findByArchivedFalse();

    List<Event> findByTitleContaining(String title);

    //List<Event> idEvent(long idEvent);


    @Query("SELECT e FROM Event e WHERE " +
            "(:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
            "AND (:dateEvent IS NULL OR e.dateEvent = :dateEvent)")
    List<Event> advancedSearch(@Param("title") String title,
                               @Param("location") String location,
                               @Param("dateEvent") LocalDate dateEvent);

}
