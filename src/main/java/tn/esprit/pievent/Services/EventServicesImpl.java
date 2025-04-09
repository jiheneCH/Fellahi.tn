package tn.esprit.pievent.Services;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.Document;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import tn.esprit.pievent.Entities.Event;


import tn.esprit.pievent.Entities.Status;
import tn.esprit.pievent.Entities.TypeEvent;
import tn.esprit.pievent.Repositories.EventRepository;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class EventServicesImpl implements IEventServices{
    @Autowired
     EventRepository eventRepository;


    public EventServicesImpl() {
        File directory = new File(PDF_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // Crée le dossier s'il n'existe pas
        }
    }
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
    //@Override
   // public Event addEvent(Event event) {
       // return eventRepository.save(event);
    //}

    @Override
    public Event addEvent(Event event) {
        if (event.getImage() == null || event.getImage().isEmpty()) {
            throw new IllegalArgumentException("L'image est obligatoire !");
        }
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

    @Transactional
    @Override
    public void archiverEvent(long idEvent) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("L'event avec l'ID " + idEvent + " n'existe pas."));

        if (event.isArchived()) {
            throw new RuntimeException("L'event avec l'ID " + idEvent + " est déjà archivé.");
        }

        event.setArchived(true);
        event.setStatus(Status.archive);

        try {
            eventRepository.save(event);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'archivage de l'event : " + e.getMessage());
        }
    }
//creation du pdf à partir des données
private static final String PDF_DIRECTORY = "C:/events_pdfs/"; // Dossier où stocker les PDF
    @Override
    public String createEventWithPdf(Event event) {
        // Utiliser un UUID pour générer un nom de fichier unique
        String pdfFileName = "event_" + UUID.randomUUID().toString() + ".pdf"; // UUID pour garantir l'unicité
        String pdfPath = PDF_DIRECTORY + pdfFileName;

        // Vérifier que le dossier existe
        File directory = new File(PDF_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // Crée le dossier si inexistant
        }

        try (FileOutputStream fos = new FileOutputStream(pdfPath)) {
            PdfWriter writer = new PdfWriter(fos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Ajouter des informations à votre PDF
            document.add(new Paragraph("Événement : " + event.getTitle()));
            document.add(new Paragraph("Date : " + event.getDateEvent()));
            document.add(new Paragraph("Heure : " + event.getHeureEvent()));
            document.add(new Paragraph("Lieu : " + event.getLocation()));
            document.add(new Paragraph("Description : " + event.getDescription()));

            document.close();

            // Retourner le nom du fichier PDF pour le stocker dans la base de données
            return pdfFileName;  // Renvoie un nom unique pour chaque PDF
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la génération du PDF", e);
        }
    }

    @Override
    public ResponseEntity<Resource> getEventPdf(int idPDF) {
        String pdfPath = PDF_DIRECTORY + "event_" + idPDF + ".pdf";
        File file = new File(pdfPath);

        if (!file.exists()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        Resource resource = new FileSystemResource(file);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName())
                .contentType(MediaType.APPLICATION_PDF)
                .body(resource);
    }
//upload des images lors de l'ajout
    ;
    // Dossier de stockage
    private static final String UPLOAD_DIR = "C:/uploads/";
    // Extensions d'images autorisées
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("png", "jpg", "jpeg", "gif", "bmp", "webp");
    @Override
    public String uploadImage(MultipartFile file) throws IOException {
        // Vérifier si le fichier est vide
        if (file.isEmpty()) {
            throw new IllegalStateException("Le fichier est vide !");
        }

        // Extraire l'extension du fichier
        String originalFilename = file.getOriginalFilename();
        assert originalFilename != null;
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();

        // Vérifier si l'extension est autorisée
        if (!ALLOWED_EXTENSIONS.contains(fileExtension)) {
            throw new IllegalStateException("Type de fichier non supporté ! Formats autorisés : " + ALLOWED_EXTENSIONS);
        }
        // Générer un nom de fichier unique
        String newFileName = UUID.randomUUID() + "." + fileExtension;

        // Sauvegarder le fichier sur le serveur
        File uploadPath = new File(UPLOAD_DIR);
        if (!uploadPath.exists()) {
            uploadPath.mkdirs(); // Créer le dossier si inexistant
        }

        File destinationFile = new File(UPLOAD_DIR + newFileName);
        file.transferTo(destinationFile);

        return "Fichier uploadé avec succès : " + newFileName;
    }

//recherche avancée

    public List<Event> advancedSearch(String title, String location, String startDateStr) {
        // Conversion de la date si elle est fournie
        LocalDate dateEvent = null;
        if (startDateStr != null && !startDateStr.trim().isEmpty()) {
            dateEvent = LocalDate.parse(startDateStr);
        }

        // Nettoyage des paramètres : si vides, on passe null pour ignorer le filtre
        title = (title != null && !title.trim().isEmpty()) ? title.trim() : null;
        location = (location != null && !location.trim().isEmpty()) ? location.trim() : null;

        // Appel de la méthode du repository
        return eventRepository.advancedSearch(title, location, dateEvent);
    }



}



    

