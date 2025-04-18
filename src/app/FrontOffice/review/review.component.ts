import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ReviewService } from 'src/app/review.service';

@Component({
  selector: 'app-review',
  templateUrl: './review.component.html',
  styleUrls: ['./review.component.css']
})
export class ReviewComponent implements OnInit {
  idEvent!: number;
  userId!: number;
  hoveredRating = 0;
  stars = [1, 2, 3, 4, 5];

  review = {
    rating: 0,
    comment: '',
    user: { id: 0 }, // Id du client, tu peux le changer dynamiquement si besoin
    event: { id: 0 },
  };
 

  constructor(
    private route: ActivatedRoute,
    private reviewService: ReviewService,
     private router: Router,
        
  ) {}

  ngOnInit(): void {
    this.idEvent = +this.route.snapshot.paramMap.get('idEvent')!;
    
    this.userId = +this.route.snapshot.paramMap.get('userId')!;
  
   
    this.review.user.id = this.userId;

    this.review.event.id = this.idEvent;
  }

  setRating(rating: number): void {
    this.review.rating = rating;
  }

  hoverRating(rating: number): void {
    this.hoveredRating = rating;
  }

  submitReview(): void {
    if (this.review.rating < 1) {
      alert("Veuillez sélectionner une note avant de soumettre.");
      return;
    }
  
   
    const payload = {
      idEvent: this.review.event.id,
      userId: this.review.user.id,  
      
      rating: this.review.rating,
      comment: this.review.comment
    };
  
    this.reviewService.addReview(payload).subscribe({
      next: () => {
        alert('Merci pour votre avis !');
         window.location.reload();
      },
      error: (err: any) => {
        console.error('Erreur lors de l’ajout du review :', err);
        alert("Une erreur s'est produite.");
      }
    });
  }
  
  
}



