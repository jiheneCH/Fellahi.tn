package tn.esprit.pievent.Services;

import tn.esprit.pievent.Entities.Review;

import java.util.List;

public interface IReviewServices {
    Review addReview(Review review);

     List<Review> getReviewsByEvent(Long idEvent);
}
