package tn.esprit.pievent.Controllers;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.pievent.Entities.Client;
import tn.esprit.pievent.Entities.Event;
import tn.esprit.pievent.Entities.Review;

import tn.esprit.pievent.Repositories.ClientRepository;
import tn.esprit.pievent.Repositories.EventRepository;
import tn.esprit.pievent.Services.IReviewServices;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/review")
public class ReviewRestController {

    @Autowired
    IReviewServices reviewServices;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ClientRepository clientRepository;




    @PostMapping("/add")
    public Review addReview(@RequestBody Map<String, Object> payload) {
        Long idEvent = Long.valueOf(payload.get("idEvent").toString());
        Long idClient = Long.valueOf(payload.get("idClient").toString());
        Integer rating = (Integer) payload.get("rating");
        String comment = (String) payload.get("comment");

        // Vérifier que l'événement existe
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID : " + idEvent));

        // Vérifier que le client existe
        Client client = clientRepository.findById(idClient)
                .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + idClient));

        // Création de la review
        Review review = new Review();
        review.setEvent(event);
        review.setClient(client);
        review.setRating(rating);
        review.setComment(comment);

        return reviewServices.addReview(review);
    }


    // Récupérer les reviews d'un événement
    @GetMapping("/event/{idEvent}")
    public ResponseEntity<List<Review>> getReviewsByEvent(@PathVariable Long idEvent) {
        return ResponseEntity.ok(reviewServices.getReviewsByEvent(idEvent));
    }
}
