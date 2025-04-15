import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Event } from 'src/app/Event';
import { EventsService } from 'src/app/events.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-event-details',
  templateUrl: './event-details.component.html',
  styleUrls: ['./event-details.component.css']
})
export class EventDetailsComponent implements OnInit {
  event!: Event; // Le ! signifie que cette propriété sera initialisée après
  userId!: number;
  numberOfPass!: number;
  showReservationForm: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private eventService: EventsService,
    private http: HttpClient,
    private snackBar: MatSnackBar
   
  ) {}

  ngOnInit(): void {
    this.getEventById();
  }

  getEventById(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEventById(+id).subscribe({
        next: (data) => {
          this.event = data;
          console.log('Event loaded:', this.event);
        },
        error: (err) => {
          console.error('Error fetching event:', err);
        }
      });
    }
  }


  makeReservation() {
    const idEvent = this.event.idEvent;

    if (!this.userId || !this.numberOfPass) {
      this.snackBar.open('Please enter User ID and Number of Passes', 'Close', {
        duration: 3000,
        panelClass: ['snackbar-error']
      });
      return;
    }

    const params = new HttpParams()
      .set('idEvent', idEvent.toString())
      .set('id', this.userId.toString())
      .set('numberOfPass', this.numberOfPass.toString());

    this.http.post('http://localhost:8080/piEvent/reservation/book', null, { params })
      .subscribe({
        next: (response) => {
          this.snackBar.open('Reservation successful!', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-success']
          });
          window.location.reload();
        },
        error: (err) => {
          this.snackBar.open('Reservation failed.', 'Close', {
            duration: 3000,
            panelClass: ['snackbar-error']
          });
        }
      });
  }
  

}
