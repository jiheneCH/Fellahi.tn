import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Reservation } from 'src/app/Reservation';

import { ReservationService } from 'src/app/reservation.service';

@Component({
  selector: 'app-reservation-client',
  templateUrl: './reservation-client.component.html',
  styleUrls: ['./reservation-client.component.css']
})
export class ReservationClientComponent implements OnInit {

  bookings: Reservation[] = [];
  searchReference: string = '';
  userId!: number;
 
  constructor(
    private reservationService: ReservationService,
    private route: ActivatedRoute,

  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id']; // Récupère l'id depuis l'URL
      this.fetchReservations(this.userId);
    });
  }

  fetchReservations(userId: number): void {
    this.reservationService.getAllReservationsClient(this.userId).subscribe({
      next: (data) => {
        console.log('Réponse JSON brute :', data);
        // Traite la réponse ici si nécessaire
        this.bookings = data;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des réservations :', err);
      }
    });
  }
 

  searchReservationByReference() {
      if (this.searchReference && this.searchReference.trim() !== "") {
        // Vérifier que la référence est correcte avant d'envoyer la requête
        console.log("Searching for bookings with reference:", this.searchReference);
    
        // Appeler le service pour rechercher l'événement par référence
        this.reservationService.searchReservationByReference(this.searchReference).subscribe((bookings) => {
          this.bookings = bookings;  // Stocker les événements trouvés
          console.log("bookings found:", bookings);  // Afficher les événements trouvés dans la console
        }, (error: any) => {
          console.error('Error fetching bookings:', error);
        });
      } else {
        // Si la référence est vide, recharger la liste complète des événements
        console.log("No reference, loading all bookings.");
    
        // Appeler le service pour récupérer tous les événements
        this.reservationService.getAllReservations().subscribe((bookings) => {
          this.bookings = bookings;  // Mettre à jour la liste des événements
          console.log("All bookings loaded:", bookings);  // Afficher les événements dans la console
        }, (error: any) => {
          console.error('Error fetching all bookings:', error);
        });
      }
    }
  
  
    onCancelReservation(idReservation: number) {
      const confirmation = window.confirm("Voulez-vous vraiment annuler cette réservation ?");
      if (confirmation) {
        this.reservationService.cancelReservation(idReservation).subscribe({
          next: (response: any) => {
            alert("Réservation annulée avec succès !");
            window.location.reload();
          },
          error: (error: HttpErrorResponse) => {
            alert("Erreur lors de l'annulation : " + error.error);
          }
        });
      }
    }

}
