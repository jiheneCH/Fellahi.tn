package tn.esprit.pievent.Services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pievent.Entities.Event;


import tn.esprit.pievent.Entities.Status;
import tn.esprit.pievent.Repositories.EventRepository;

import java.util.List;
@Service
public class EventServicesImpl implements IEventServices{
    @Autowired
     EventRepository eventRepository;
    @Override
    public List<Event> retrieveAllEvent() {
        return  eventRepository.findByArchivedFalse();
    }

    @Override
    public Event retrieveEvent(long idEvent) {
         Event event = eventRepository.findById(idEvent)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Article not found"));
         if (event.getStatus() == Status.archive) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acces to this event is forbidden");
         }
         return event;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    //@Override
    //public void archiverEvent(Long idEvent) {
        //    Event event = eventRepository.findById(idEvent).get();
            //event.setArchived(true);
           // event.setStatus(Status.archive);
           // eventRepository.save(event);
       // }

    @Override
    @Transactional
    public void archiverEvent(Long idEvent) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("L'event avec l'ID " + idEvent + " n'existe pas."));

        if (event.isArchived()) {
            throw new RuntimeException("L'event avec l'ID " + idEvent+ " est déjà archivé.");
        }

        event.setArchived(true);
        event.setStatus(Status.archive);

        try {
            eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'archivage de l'event : " + e.getMessage());
        }
    }
    }

