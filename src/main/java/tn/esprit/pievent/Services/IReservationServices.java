package tn.esprit.pievent.Services;

import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Reservation;
import tn.esprit.pievent.Entities.ReservationAnalyticsResponse;

public interface IReservationServices {


    public Reservation createReservation(long idEvent, long idClient, int numberOfPass);
    void cancelReservation(long idReservation);

    void handleWaitingList(Event event);

    public ReservationAnalyticsResponse calculateAnalytics();
}