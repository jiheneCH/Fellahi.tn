package tn.esprit.fallehiuser.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.model.WaitingListEntry;
import tn.esprit.fallehiuser.Repository.WaitingListEntryRepository;

import java.util.List;
import java.util.UUID;
@Service
public class WaintinglistServicesImpl implements IWaintinglistServices {

    @Autowired
    private WaitingListEntryRepository waitingListEntryRepository;


    @Override
    public List<WaitingListEntry> searchWaitinglistByReference(String reference) {
        System.out.println("Searching in database for reference: " + reference);
        // Assurez-vous que votre méthode de recherche fonctionne correctement
        return waitingListEntryRepository.findByReferenceContainingIgnoreCase(reference);
    }


    @Override
    public String generateReference(WaitingListEntry entry) {
        // Générer un UUID aléatoire et formater sous forme de chaîne
        UUID uuid = UUID.randomUUID();
        String reference = "E-" + uuid.toString().substring(0, 8) + "-" +
                uuid.toString().substring(9, 13) + "-" +
                uuid.toString().substring(14, 18) + "-" +
                uuid.toString().substring(19, 23); // Format désiré
        return reference;
    }

   // @Override
 //   public List<WaitingListEntry> retrieveAllWaitinglist() {
     //   return waitingListEntryRepository.findAll();
   // }


    @Override
    public List<WaitingListEntry>  retrieveAllWaitinglist()  {
        return waitingListEntryRepository.findAllWithClientAndEvent();
    }



}



