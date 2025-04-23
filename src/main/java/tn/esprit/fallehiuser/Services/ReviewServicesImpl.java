package tn.esprit.fallehiuser.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tn.esprit.fallehiuser.model.Event;
import tn.esprit.fallehiuser.model.Review;
import tn.esprit.fallehiuser.Repository.EventRepository;
import tn.esprit.fallehiuser.Repository.ReviewRepository;

import java.util.List;

@Service
public class ReviewServicesImpl implements IReviewServices {


    @Autowired
    private EventRepository eventRepository;



    @Autowired
    private ReviewRepository reviewRepository;

    @Override
    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }

    @Override
    public List<Review> getReviewsByEvent(Long idEvent) {
        Event event = eventRepository.findById(idEvent)
                .orElseThrow(() -> new RuntimeException("Événement non trouvé"));
        return reviewRepository.findByEvent(event);
    }
}


