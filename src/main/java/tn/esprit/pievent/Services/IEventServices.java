package tn.esprit.pievent.Services;

import jakarta.transaction.Transactional;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pievent.Entities.Event;

import java.io.IOException;
import java.util.List;

public interface IEventServices {
    public List<Event> retrieveAllEvent();
    public Event retrieveEvent (long idEvent);
    public Event addEvent (Event event);
    public Event updateEvent(Event event);



    @Transactional
    void archiverEvent(long idEvent);

    String createEventWithPdf(Event event);

    ResponseEntity<Resource> getEventPdf(int id);

    String uploadImage(MultipartFile file) throws IOException;

    List<Event> advancedSearch(String title, String location, String startDateStr);




}
