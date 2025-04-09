package tn.esprit.pievent.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Reservation;
import tn.esprit.pievent.Entities.ReservationAnalyticsResponse;
import tn.esprit.pievent.Entities.WaitingListEntry;
import tn.esprit.pievent.Repositories.EventRepository;
import tn.esprit.pievent.Repositories.WaitingListEntryRepository;
import tn.esprit.pievent.Services.IReservationServices;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationRestController {


    @Autowired
    IReservationServices iReservationServices;

    @Autowired
    WaitingListEntryRepository waitingListEntryRepository;

    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/book")
    public Reservation bookEvent(@RequestParam long idEvent, @RequestParam long idClient, @RequestParam int numberOfPass) {
        return iReservationServices.createReservation(idEvent, idClient, numberOfPass);
    }

    @GetMapping("/waiting-list/{idEvent}")
    public List<WaitingListEntry> getWaitingList(@PathVariable long idEvent) {
        Event event = new Event();
        event.setIdEvent(idEvent); // Assure-toi que l'événement existe avant
        return waitingListEntryRepository.findByEventOrderByRequestTimeAsc(event);
    }

    @DeleteMapping("/cancel/{idReservation}")
    public ResponseEntity<String> cancelReservation(@PathVariable long idReservation) {
        try {
            iReservationServices.cancelReservation(idReservation);
            return ResponseEntity.ok("Réservation annulée avec succès");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @GetMapping("/analytics")
    public ReservationAnalyticsResponse getReservationAnalytics() {
        return iReservationServices.calculateAnalytics();
    }

}
