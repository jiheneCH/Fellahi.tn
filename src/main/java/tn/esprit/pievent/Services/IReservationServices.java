package tn.esprit.pievent.Services;

import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Reservation;
import tn.esprit.pievent.Entities.ReservationAnalyticsResponse;
import tn.esprit.pievent.Entities.WaitingListEntry;

import java.util.List;

public interface IReservationServices {
    public List<Reservation> retrieveAllReservation();
    List<Reservation> searchReservationByReference(String reference);


    public Reservation createReservation(long idEvent, long id, int numberOfPass);
    void cancelReservation(long idReservation);

    void handleWaitingList(Event event);

    public ReservationAnalyticsResponse calculateAnalytics();

   // public List<Reservation> getReservationsByClientId(Long userId);

    List<Reservation> getReservationsByClientUserId(Long userId);


}