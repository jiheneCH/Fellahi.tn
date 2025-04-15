import { Component } from '@angular/core';
import { WaitinglistService } from 'src/app/waitinglist.service';
import { WaitingListEntry} from 'src/app/WaitingListEntry';

@Component({
  selector: 'app-waiting-list',
  templateUrl: './waiting-list.component.html',
  styleUrls: ['./waiting-list.component.css']
})
export class WaitingListComponent {
  waiting:  WaitingListEntry[] = [];
  searchReference: string = '';




    constructor(private waitinglistservice: WaitinglistService) {}
  
    ngOnInit(): void {
      this.fetchWaitinglists();
    }
  
    fetchWaitinglists(): void {
      this.waitinglistservice.getAllWaitinglists().subscribe({
        next: (data) => {
          console.log('Réponse JSON brute :', data);
          // Traite la réponse ici si nécessaire
          this.waiting = data;
        },
        error: (err) => {
          console.error('Erreur lors de la récupération des réservations :', err);
        }
      });
    }



    
  searchWaitinglistByReference() {
    if (this.searchReference && this.searchReference.trim() !== "") {
      // Vérifier que la référence est correcte avant d'envoyer la requête
      console.log("Searching for bookings with reference:", this.searchReference);
  
      // Appeler le service pour rechercher l'événement par référence
      this.waitinglistservice.searchWaitinglistByReference(this.searchReference).subscribe((waiting) => {
        this.waiting = waiting;  // Stocker les événements trouvés
        console.log("waiting list found:", waiting);  // Afficher les événements trouvés dans la console
      }, (error: any) => {
        console.error('Error fetching waiting list:', error);
      });
    } else {
      // Si la référence est vide, recharger la liste complète des événements
      console.log("No reference, loading all waiting list.");
  
      // Appeler le service pour récupérer tous les événements
      this.waitinglistservice.getAllWaitinglists().subscribe((waiting) => {
        this.waiting = waiting;  // Mettre à jour la liste des événements
        console.log("All waiting list loaded:", waiting);  // Afficher les événements dans la console
      }, (error: any) => {
        console.error('Error fetching all waitings:', error);
      });
    }
  }
  

  
  
}
