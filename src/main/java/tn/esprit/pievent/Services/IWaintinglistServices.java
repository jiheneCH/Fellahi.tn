package tn.esprit.pievent.Services;
import tn.esprit.pievent.Entities.Reservation;
import tn.esprit.pievent.Entities.WaitingListEntry;

import java.util.List;

public interface IWaintinglistServices {


    String generateReference(WaitingListEntry entry);
    public List<WaitingListEntry> retrieveAllWaitinglist();

    List<WaitingListEntry> searchWaitinglistByReference(String reference);

}
