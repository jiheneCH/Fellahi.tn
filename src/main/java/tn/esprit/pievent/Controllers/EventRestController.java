package tn.esprit.pievent.Controllers;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Services.IEventServices;


import java.util.List;


@RestController
@AllArgsConstructor
@RequestMapping("/event")
public class EventRestController {
    @Autowired
    IEventServices iEventServices;


    @GetMapping("/retrieveAllEvent")
    public List<Event> afficherEvent(){
        return iEventServices.retrieveAllEvent();

    }
    @GetMapping("/retrieveEvent/{idEvent}")
    public Event afficherEvent(@PathVariable("idEvent") long idEvent){
        return iEventServices.retrieveEvent(idEvent);
    }

    //@PostMapping("/addEvent")
   // public Event ajouterEvent(@RequestBody Event event){
     //   return iEventServices.addEvent(event);
    //}

   // @PostMapping("/addEvent")
   // public ResponseEntity<Map<String, String>> addEvent(@Valid @RequestBody Event event, BindingResult result) {
      //  if (result.hasErrors()) {
          //  Map<String, String> errors = new HashMap<>();
           // for (FieldError error : result.getFieldErrors()) {
           //     errors.put(error.getField(), error.getDefaultMessage());
           // }
           // return ResponseEntity.badRequest().body(errors);
        //}
       // Event savedEvent = iEventServices.addEvent(event);
       // return ResponseEntity.ok(Collections.singletonMap("message", "Événement ajouté avec succès"));
    //}

    @PostMapping("/addEvent")
    public ResponseEntity<String> addEvent(@Valid @RequestBody Event event) {
        iEventServices.addEvent(event);
        return ResponseEntity.ok("Événement ajouté avec succès");
    }


    @PutMapping("/updateEvent")
    public ResponseEntity<String> updateEvent(@Valid @RequestBody Event event) {
        iEventServices.updateEvent(event);
        return ResponseEntity.ok("Événement mis à jour avec succès");
    }



    //@PutMapping("/updateEvent")
    //public Event updateEvent(@RequestBody Event event){
      //  return iEventServices.updateEvent(event);
    //}

    //@PutMapping("/archiver/{idEvent}")
    //public ResponseEntity<String> archiverEvent(@PathVariable("idEvent") Long idEvent) {
      //  iEventServices.archiverEvent(idEvent);
       // return ResponseEntity.ok("Livraison " + idEvent + " archivée avec succès.");
    //}

    @PutMapping("/archiver/{idEvent}")
    public ResponseEntity<?> archiverEvent(@PathVariable("idEvent") Long idEvent) {
        try {
            iEventServices.archiverEvent(idEvent);
            return ResponseEntity.ok("L'event " + idEvent + " a été archivé avec succès.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Une erreur interne est survenue.");
        }
    }





}
