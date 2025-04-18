import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { EventsService } from 'src/app/events.service';
import { Event } from 'src/app/Event';

@Component({
  selector: 'app-event-front',
  templateUrl: './event-front.component.html',
  styleUrls: ['./event-front.component.css']
})
export class EventFrontComponent implements OnInit {

  events: Event[] = [];
 

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

  get availableEvents(): Event[] {
    return this.events.filter(e => e.status === 'disponible' || e.status === 'complete');
  }
  

}
