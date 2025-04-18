package tn.esprit.pievent.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tn.esprit.pievent.Entities.Reservation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    int countByUser_Id(Long id);  // si ton User a un champ 'id' ou 'idUser'
    List<Reservation> findByUser_Id(Long id);


    List<Reservation> findByReservationDateBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<Reservation> findByReferenceContainingIgnoreCase(String reference);




    @Query("SELECT r FROM Reservation r JOIN FETCH r.event JOIN FETCH r.user")
    List<Reservation> findAllWithClientAndEvent();

   // @Query("SELECT r FROM Reservation r JOIN FETCH r.event JOIN FETCH r.user u WHERE u.id = :userId AND u.role = 'CLIENT'")
  //  List<Reservation> findReservationsByUser_Id(@Param("userId") Long userId);

    @Query("SELECT r FROM Reservation r WHERE r.user.id = :userId AND r.user.role.roleName = 'CLIENT'")
    List<Reservation> findReservationsByClientUserId(@Param("userId") Long userId);




}
