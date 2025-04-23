package tn.esprit.fallehiuser.Controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import tn.esprit.fallehiuser.Repository.UserRepository;
import tn.esprit.fallehiuser.model.Event;
import tn.esprit.fallehiuser.model.Review;

import tn.esprit.fallehiuser.Repository.EventRepository;
import tn.esprit.fallehiuser.model.User;
import tn.esprit.fallehiuser.Services.IReviewServices;
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
    private UserRepository userRepository;




    @PostMapping("/add")
    public Review addReview(@RequestBody Map<String, Object> payload) {
        try {
            Long idEvent = Long.valueOf(payload.get("idEvent").toString());
            Long id = Long.valueOf(payload.get("userId").toString());
            Integer rating = (Integer) payload.get("rating");
            String comment = (String) payload.get("comment");

            // Vérifier que l'événement existe
            Event event = eventRepository.findById(idEvent)
                    .orElseThrow(() -> new RuntimeException("Événement non trouvé avec l'ID : " + idEvent));

            // Vérifier que le client existe
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Client non trouvé avec l'ID : " + id));

            // Création de la review
            Review review = new Review();
            review.setEvent(event);
            review.setUser(user);
            review.setRating(rating);
            review.setComment(comment);

            return reviewServices.addReview(review);

        } catch (Exception e) {
            e.printStackTrace();  // Log l'exception dans la console
            throw new RuntimeException("Erreur lors de l'ajout du review", e);
        }
    }

    // Récupérer les reviews d'un événement
    @GetMapping("/event/{idEvent}")
    public ResponseEntity<List<Review>> getReviewsByEvent(@PathVariable Long idEvent) {
        return ResponseEntity.ok(reviewServices.getReviewsByEvent(idEvent));
    }
}
