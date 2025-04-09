package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tn.esprit.pievent.Entities.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByClient_IdClient(Long idClient);  // Accède à client.idClient
    List<Reservation> findByClient_IdClient(Long idClient);  // Accède à client.idClie

    List<Reservation> findByReservationDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

}
