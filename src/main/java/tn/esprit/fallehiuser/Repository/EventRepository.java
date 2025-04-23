package tn.esprit.fallehiuser.Repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.fallehiuser.model.Event;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>{

          List<Event> findByArchivedFalse();

    List<Event> findByTitleContaining(String title);

    //List<Event> idEvent(long idEvent);


   // @Query("SELECT e FROM Event e WHERE " +
      //      "(:title IS NULL OR LOWER(e.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
          //  "AND (:location IS NULL OR LOWER(e.location) LIKE LOWER(CONCAT('%', :location, '%'))) " +
          //  "AND (:dateEvent IS NULL OR e.dateEvent = :dateEvent)")
    //List<Event> advancedSearch(@Param("title") String title,
                         //      @Param("location") String location,
                           //    @Param("dateEvent") LocalDate dateEvent);






    List<Event> findByReferenceContainingIgnoreCase(String reference);

    Optional<Event> findByReference(String reference);



}
