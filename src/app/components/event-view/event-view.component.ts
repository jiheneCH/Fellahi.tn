import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { EventsService } from 'src/app/events.service';
import { Event } from 'src/app/Event';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.css']
})
export class EventViewComponent implements OnInit {
   event!: Event; 

  constructor(
    private route: ActivatedRoute,
   private eventService: EventsService, 
   private http: HttpClient,  

  ) { }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.eventService.getEventById(+id).subscribe({
        next: (data) => {
          this.event = data;
        },
        error: (err) => {
          console.error('Error fetching event:', err);
        }
      });
    }
  }

  // Dans votre composant
imageError = false;

getImageUrl(imageName: string): string {
  if (!imageName || this.imageError) {
    return 'assets/images/default-event.jpg';
  }
  return `http://localhost:8080/piEvent/uploads/${imageName}`;
}

handleImageError() {
  this.imageError = true;
}
}
