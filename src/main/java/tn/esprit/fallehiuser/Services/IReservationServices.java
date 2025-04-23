package tn.esprit.fallehiuser.Services;

import tn.esprit.fallehiuser.model.Event;
import tn.esprit.fallehiuser.model.Reservation;
import tn.esprit.fallehiuser.model.ReservationAnalyticsResponse;

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