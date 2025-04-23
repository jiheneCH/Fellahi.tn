package tn.esprit.fallehiuser.Services;

import tn.esprit.fallehiuser.model.Review;

import java.util.List;

public interface IReviewServices {
    Review addReview(Review review);

     List<Review> getReviewsByEvent(Long idEvent);


}
