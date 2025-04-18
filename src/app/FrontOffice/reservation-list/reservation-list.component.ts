import { HttpErrorResponse } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Reservation } from 'src/app/Reservation';
import { ReservationService } from 'src/app/reservation.service';

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.css']
})
export class ReservationListComponent implements OnInit{
  bookings: Reservation[] = [];
  searchReference: string = '';
  

  constructor(private reservationService: ReservationService) {}

  ngOnInit(): void {
    this.fetchReservations();
  }

  fetchReservations(): void {
    this.reservationService.getAllReservations().subscribe({
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


  logClick() {
    console.log('clicked');
  }
  

  onCancelReservation(idReservation: number) {
    const confirmation = window.confirm("Voulez-vous vraiment annuler cette réservation ?");
    if (confirmation) {
      this.reservationService.cancelReservation(idReservation).subscribe({
        next: (response) => {
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
