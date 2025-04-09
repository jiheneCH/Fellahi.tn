package tn.esprit.pievent.Controllers;

import java.math.BigDecimal;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Status;
import tn.esprit.pievent.Entities.TypeEvent;
import tn.esprit.pievent.Services.IEventServices;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
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

     //9bal me nzid image
    //@PostMapping("/addEvent")
    //public ResponseEntity<String> addEvent(@Valid @RequestBody Event event) {
     //   iEventServices.addEvent(event);
      //  return ResponseEntity.ok("Événement ajouté avec succès");
    //}

    //ajout de l'event avec multiple file fil postman meaaha image
    @PostMapping(value = "/addEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addEvent(
            @RequestParam("title") String title,
            @RequestParam("dateEvent") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEvent,
            @RequestParam("heureEvent") @DateTimeFormat(pattern = "HH:mm") LocalTime heureEvent,
            @RequestParam("location") String location,
            @RequestParam("description") String description,
            @RequestParam("typeEvent") String typeEvent,
            @RequestParam("status") String status,
            @RequestParam("nombrePlaces") int nombrePlaces,
            @RequestParam("prix") BigDecimal prix,
            @RequestParam("image") MultipartFile image) {
        try {
            // Vérifier si la date est valide
            if (dateEvent.isBefore(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("La date de l'événement doit être dans le futur ou aujourd'hui.");
            }

            // Définir le dossier de stockage pour l'image
            String uploadDir = "C:/uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Crée le dossier si inexistant
            }

            // Sauvegarder l'image
            String originalFilename = image.getOriginalFilename();
            File filePath = new File(uploadDir + originalFilename);
            image.transferTo(filePath);

            // Créer l'objet Event
            Event event = new Event();
            event.setTitle(title);
            event.setDateEvent(dateEvent);
            event.setHeureEvent(heureEvent);
            event.setLocation(location);
            event.setDescription(description);
            event.setTypeEvent(TypeEvent.valueOf(typeEvent));
            event.setStatus(Status.valueOf(status));
            event.setNombrePlaces((long) nombrePlaces);
            event.setPrix(prix);
            event.setImage(originalFilename); // Stocker uniquement le nom du fichier de l'image en base

            // Créer le PDF avant d'insérer l'événement dans la base de données
            String pdfFileName = iEventServices.createEventWithPdf(event); // Générer le PDF

            // Mettre à jour l'événement avec le nom du fichier PDF
            event.setPdf(pdfFileName);  // Affecter le nom du PDF à l'événement

            // Enregistrer l'événement avec le PDF dans la base de données
            iEventServices.addEvent(event);

            return ResponseEntity.ok("Événement ajouté avec succès !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors de l'ajout de l'événement : " + e.getMessage());
        }
    }


    @PutMapping("/updateEvent")
    public Event updateEvent(@RequestBody Event event) {
        return iEventServices.updateEvent(event);
    }


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

    //creation du pdf à partir des données de l'event

    //@PostMapping("/create")
   // public String createEvent(@RequestBody Event event) {
     //   return  iEventServices.createEventWithPdf(event);
    //}



//upload de l'image lors de l'ajout d'un event
    private static final String UPLOAD_DIR = "C:/uploads/";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            // Vérification si le fichier est vide
            if (file.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Fichier vide !");
            }

            // Nettoyer le nom du fichier pour éviter les caractères dangereux
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Vérifier si le fichier existe déjà et ajouter un suffixe numérique si nécessaire
            int count = 1;
            while (Files.exists(filePath)) {
                String nameWithoutExt = fileName.substring(0, fileName.lastIndexOf("."));
                String extension = fileName.substring(fileName.lastIndexOf("."));
                fileName = nameWithoutExt + "_" + count + extension;
                filePath = Paths.get(UPLOAD_DIR + fileName);
                count++;
            }

            // Créer le dossier s'il n'existe pas
            File uploadDir = new File(UPLOAD_DIR);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // Sauvegarder le fichier
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return ResponseEntity.ok("Image uploadée avec succès : " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors de l'upload !");
        }
    }

    //recherche avancée

    @GetMapping("/advanced-search")
    public List<Event> advancedSearch(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) String startDateStr) {
        return iEventServices.advancedSearch(title, location, startDateStr);
    }



}





