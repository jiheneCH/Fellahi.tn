import { Component, OnInit } from '@angular/core';
import { EventsService } from 'src/app/events.service';
import { Event } from 'src/app/Event';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router'; 

@Component({
  selector: 'app-events-list',
  templateUrl: './events-list.component.html',
  styleUrls: ['./events-list.component.css']
})
export class EventsListComponent implements OnInit {

  events: Event[] = [];
  searchReference: string = '';

  constructor(
    private eventService: EventsService,
    private http: HttpClient,         
    private router: Router            
  ) {}

  ngOnInit(): void {
    this.fetchEvents();
  }

  fetchEvents(): void {
    this.eventService.getAllEvents().subscribe({
      next: (data) => {
        this.events = data;
        console.log('Événements reçus :', data);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des événements :', err);
      }
    });
  }

  archiverEvent(idEvent: number): void {
    if (confirm("Voulez-vous vraiment archiver cet événement ?")) {
      this.http.put(`http://localhost:8080/piEvent/event/archiver/${idEvent}`, null).subscribe(
        (response: any) => {
          alert(response.message); // cas de succès
          this.router.navigate(['/admin/events']);
        },
        (error: any) => {
          // Affiche le message d'erreur retourné par le backend s'il existe
          const errorMessage = error.error?.error || 'Une erreur est survenue lors de l’archivage de l’événement.';
          alert(errorMessage);
          console.error('Erreur lors de l’archivage :', error);
        }
      );
    }
  }

  searchEventByReference() {
    if (this.searchReference && this.searchReference.trim() !== "") {
      // Vérifier que la référence est correcte avant d'envoyer la requête
      console.log("Searching for events with reference:", this.searchReference);
  
      // Appeler le service pour rechercher l'événement par référence
      this.eventService.searchEventByReference(this.searchReference).subscribe((events) => {
        this.events = events;  // Stocker les événements trouvés
        console.log("Events found:", events);  // Afficher les événements trouvés dans la console
      }, (error: any) => {
        console.error('Error fetching events:', error);
      });
    } else {
      // Si la référence est vide, recharger la liste complète des événements
      console.log("No reference, loading all events.");
  
      // Appeler le service pour récupérer tous les événements
      this.eventService.getAllEvents().subscribe((events) => {
        this.events = events;  // Mettre à jour la liste des événements
        console.log("All events loaded:", events);  // Afficher les événements dans la console
      }, (error: any) => {
        console.error('Error fetching all events:', error);
      });
    }
  }
  
}


