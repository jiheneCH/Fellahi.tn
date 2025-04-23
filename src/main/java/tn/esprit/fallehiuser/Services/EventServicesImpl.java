package tn.esprit.fallehiuser.Services;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
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
import tn.esprit.fallehiuser.model.Event;


import tn.esprit.fallehiuser.model.StatusEvent;
import tn.esprit.pievent.Entities.Status;
import tn.esprit.fallehiuser.Repository.EventRepository;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Service
public class EventServicesImpl implements IEventServices {

    @Autowired
     EventRepository eventRepository;




    public EventServicesImpl() {
        File directory = new File(PDF_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdirs(); // Crée le dossier s'il n'existe pas
        }
    }
    //@Override
    //public List<Event> retrieveAllEvent() {
       // return  eventRepository.findByArchivedFalse();
   // }

   // @Override
   // public List<Event> retrieveAllEvent() {
      //  return eventRepository.findAll();
   // }


    @Override
    public List<Event> retrieveAllEvent() {
        List<Event> events = eventRepository.findAll();

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        for (Event event : events) {
            // Vérifie si l'événement est dans le passé (date < aujourd'hui ou même date + heure passée)
            boolean isPastEvent = event.getDateEvent().isBefore(currentDate) ||
                    (event.getDateEvent().isEqual(currentDate) && event.getHeureEvent().isBefore(currentTime));

            if (!event.isArchived() && isPastEvent) {
                archiverEvent(event.getIdEvent()); // Appel à ta méthode d'archivage
            }
        }

        return eventRepository.findAll();
    }




    @Override
    public Event retrieveEvent(long idEvent) {
         Event event = eventRepository.findById(idEvent)
                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Event not found"));
        // if (event.getStatus() == Status.archive) {
           // throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acces to this event is forbidden");
        // }
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
    public  String generateReference(Event event) {
        // Générer un UUID aléatoire et formater sous forme de chaîne
        UUID uuid = UUID.randomUUID();
        String reference = "E-" + uuid.toString().substring(0, 8) + "-" +
                uuid.toString().substring(9, 13) + "-" +
                uuid.toString().substring(14, 18) + "-" +
                uuid.toString().substring(19, 23); // Format désiré
        return reference;
    }
@Override
public byte[] generateQrCode(String text, int width, int height) throws Exception {
    QRCodeWriter qrCodeWriter = new QRCodeWriter();
    HashMap<EncodeHintType, Object> hints = new HashMap<>();
    hints.put(EncodeHintType.MARGIN, 1);

    BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, width, height, hints);
    ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
    MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);
    return pngOutputStream.toByteArray();
}




    // @Override
   // public Event updateEvent(Event event) {
     //   return eventRepository.save(event);
   // }

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
        event.setStatusEvent(StatusEvent.archive);

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



    @Override
    public List<Event> searchEventByReference(String reference) {
       System.out.println("Searching in database for reference: " + reference);
        // Assurez-vous que votre méthode de recherche fonctionne correctement
       return eventRepository.findByReferenceContainingIgnoreCase(reference);
    }





    @Override
    public Event updateEvent(Event event) {
        if (event.getImage() == null || event.getImage().isEmpty()) {
            throw new IllegalArgumentException("L'image est obligatoire !");
        }

        // Sauvegarder l'événement après l'avoir mis à jour (si nécessaire)
        return eventRepository.save(event);
    }





}



    

