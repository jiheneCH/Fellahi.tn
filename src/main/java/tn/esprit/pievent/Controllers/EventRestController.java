package tn.esprit.pievent.Controllers;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.nio.file.Path;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Status;
import tn.esprit.pievent.Entities.TypeEvent;
import tn.esprit.pievent.Repositories.EventRepository;
import tn.esprit.pievent.Services.IEventServices;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/event")
public class EventRestController {
    @Autowired
    IEventServices iEventServices;


    @Autowired
    private EventRepository eventRepository;



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


    @GetMapping("/qr/{reference}")
    public ResponseEntity<byte[]> getQrCode(@PathVariable String reference) {
        try {
            // Supposons que tu récupères l'événement ici
            Event event = eventRepository.findByReferenceContainingIgnoreCase(reference).get(0);

            byte[] image = event.getQrCodeImage();

            if (image == null || image.length == 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_PNG);
            headers.setContentLength(image.length);

            return new ResponseEntity<>(image, headers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //ajout de l'event avec multiple file fil postman meaaha image
    @PostMapping(value = "/addEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> addEvent(
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
            // Vérification de la validité de la date
            if (dateEvent.isBefore(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "La date de l'événement doit être dans le futur ou aujourd'hui."));
            }

            // Vérification de l'extension du fichier
            String fileName = image.getOriginalFilename();
            if (fileName != null && !fileName.matches(".*\\.(jpg|jpeg|png|gif)$")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Invalid image file type. Only jpg, jpeg, png, and gif are allowed."));
            }

            // Vérification du type MIME pour s'assurer que c'est une image
            String contentType = image.getContentType();
            if (!contentType.startsWith("image/")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "Seuls les fichiers image sont autorisés."));
            }
            // Définir le répertoire de stockage pour l'image
            String uploadDir = "C:/uploads/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs(); // Créer le répertoire si nécessaire
            }

            // Sauvegarder l'image
            String originalFilename = image.getOriginalFilename();
            File filePath = new File(uploadDir + originalFilename);
            image.transferTo(filePath);

            // Créer l'événement
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
            event.setImage(originalFilename); // Stocker seulement le nom de l'image

            // Créer le PDF avant d'insérer l'événement dans la base de données
            String pdfFileName = iEventServices.createEventWithPdf(event); // Générer le PDF

            // Mettre à jour l'événement avec le nom du fichier PDF
            event.setPdf(pdfFileName);  // Affecter le nom du PDF à l'événement

            //reference
            String reference = iEventServices.generateReference(event);
            event.setReference(reference);

            // Générer le QR code à partir de la référence et l'affecter au champ qrCodeImage
            byte[] qrCodeBytes = iEventServices.generateQrCode(reference, 200, 200);
            event.setQrCodeImage(qrCodeBytes);


            // Ajouter l'événement dans la base de données
            iEventServices.addEvent(event);

            // Créer la réponse avec un message de succès
            Map<String, String> response = new HashMap<>();
            response.put("message", "The event has been successfully added!");

            // Forcer le retour de la réponse avec le bon encodage UTF-8
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(response);

        } catch (IllegalArgumentException e) {
            // Gérer les erreurs de type d'argument
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Paramètre invalide : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        } catch (Exception e) {
            // Gérer les erreurs générales
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error","An error occurred while adding the event : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }


    @PutMapping("/updateEvent/{idEvent}")
    public ResponseEntity<?> updateEvent(
            @PathVariable("idEvent") Long idEvent,   // L'ID de l'événement
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("dateEvent") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEvent,
            @RequestParam("heureEvent") @DateTimeFormat(pattern = "HH:mm") LocalTime heureEvent,
            @RequestParam("prix") Double prix,
            @RequestParam("nombrePlaces") Integer nombrePlaces,
            @RequestParam("location") String location,
            @RequestParam("typeEvent") String typeEvent,
            @RequestParam("status") String status,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            // Vérifier si la date de l'événement est valide
            if (dateEvent.isBefore(LocalDate.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Map.of("error", "La date de l'événement doit être dans le futur ou aujourd'hui."));
            }

            // Récupérer l'événement existant de la base de données
            Event event = iEventServices.retrieveEvent(idEvent);

            if (event == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("error", "L'événement avec cet ID n'a pas été trouvé."));
            }

            // Mettre à jour les propriétés de l'événement
            event.setTitle(title);
            event.setDescription(description);
            event.setDateEvent(dateEvent);
            event.setHeureEvent(heureEvent);
            event.setPrix(BigDecimal.valueOf(prix));
            event.setNombrePlaces(Long.valueOf(nombrePlaces));
            event.setLocation(location);
            event.setTypeEvent(TypeEvent.valueOf(typeEvent));
            event.setStatus(Status.valueOf(status));

            // Si une nouvelle image est fournie, traiter l'image
            if (image != null && !image.isEmpty()) {
                String uploadDir = "C:/uploads/";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs(); // Créer le répertoire si nécessaire
                }

                String originalFilename = image.getOriginalFilename();
                File filePath = new File(uploadDir + originalFilename);
                image.transferTo(filePath);

                event.setImage(originalFilename); // Mettre à jour l'image
            }

            // Créer le PDF avant de sauvegarder l'événement
            String pdfFileName = iEventServices.createEventWithPdf(event); // Si tu génères un PDF, fais-le ici
            event.setPdf(pdfFileName); // Ajouter le nom du PDF

            // Sauvegarder l'événement mis à jour
            Event updatedEvent = iEventServices.updateEvent(event); // Appelle la méthode de mise à jour dans ton service

            // Retourner une réponse avec l'événement mis à jour
            return ResponseEntity.ok(updatedEvent);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Paramètre invalide : " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating event: " + e.getMessage());
        }
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

    @GetMapping("/search")
    public List<Event> searchEventByReference(@RequestParam("reference") String reference) {
        return iEventServices.searchEventByReference(reference);  // Requête vers le service qui recherche par référence
    }


    private static final String PDF_DIRECTORY = "C:/events_pdfs/";

    @GetMapping("/pdf/{filename:.+}")
    public ResponseEntity<Resource> getPdf(@PathVariable String filename) {
        try {
            // Trouver le nom exact du fichier (insensible à la casse)
            String actualFilename = findActualFilename(PDF_DIRECTORY, filename);

            if (actualFilename == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = Paths.get(PDF_DIRECTORY).resolve(actualFilename);
            Resource resource = new FileSystemResource(filePath);

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + actualFilename + "\"")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String findActualFilename(String directory, String requestedFilename) {
        File folder = new File(directory);

        // Vérifier si le dossier existe
        if (!folder.exists() || !folder.isDirectory()) {
            return null;
        }

        // Parcourir les fichiers du dossier
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                // Comparaison insensible à la casse
                if (file.getName().equalsIgnoreCase(requestedFilename)) {
                    return file.getName();
                }
            }
        }

        // Si aucun fichier ne correspond, vérifier si le fichier existe avec le nom exact
        File exactFile = new File(directory + requestedFilename);
        if (exactFile.exists()) {
            return requestedFilename;
        }

        return null;
    }
}








