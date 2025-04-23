package tn.esprit.fallehiuser.Services;
import tn.esprit.fallehiuser.model.WaitingListEntry;

import java.util.List;

public interface IWaintinglistServices {


    String generateReference(WaitingListEntry entry);
    public List<WaitingListEntry> retrieveAllWaitinglist();

    List<WaitingListEntry> searchWaitinglistByReference(String reference);

}
