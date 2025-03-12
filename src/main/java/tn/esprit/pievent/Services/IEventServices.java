package tn.esprit.pievent.Services;

import tn.esprit.pievent.Entities.Event;

import java.util.List;

public interface IEventServices {
    public List<Event> retrieveAllEvent();
    public Event retrieveEvent (long idEvent);
    public Event addEvent (Event event);
    public Event updateEvent(Event event);
    public void archiverEvent(Long idEvent);
}
