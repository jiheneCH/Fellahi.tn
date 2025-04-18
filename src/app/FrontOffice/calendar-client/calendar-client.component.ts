import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { CalendarOptions } from '@fullcalendar/core';
import { Reservation } from 'src/app/Reservation';
import { ReservationService } from 'src/app/reservation.service';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import multiMonthPlugin from '@fullcalendar/multimonth';

@Component({
  selector: 'app-calendar-client',
  templateUrl: './calendar-client.component.html',
  styleUrls: ['./calendar-client.component.css']
})
export class CalendarClientComponent implements OnInit{

  bookings: Reservation[] = [];
  userId!: number;
  

  calendarOptions: CalendarOptions = {
    initialView: 'timeGridWeek',
    editable: true,
    droppable: true,
    selectable: true,
    eventResizableFromStart: true,
    eventDurationEditable: true,
    plugins: [dayGridPlugin, timeGridPlugin, interactionPlugin, multiMonthPlugin],
    headerToolbar: {
      left: 'prev,next today',
      center: 'title',
      right: 'timeGridDay,timeGridWeek,dayGridMonth,multiMonthYear'
    },
    views: {
      multiMonthYear: {
        type: 'multiMonth',
        duration: { months: 12 },
        buttonText: 'Year'
      }
    },
    events: []
  };

  constructor(
    private reservationService: ReservationService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.userId = +params['id']; // récupère l'ID depuis l'URL
      this.fetchReservations(this.userId);
    });
  }
  fetchReservations(userId: number): void {
    this.reservationService.getAllReservationsClient(userId).subscribe({
      next: (data) => {
        console.log('Réservations récupérées :', data);
        this.bookings = data;

        // Transforme les réservations pour FullCalendar
        const formattedEvents = this.bookings.map(res => ({
          id: res.idReservation.toString(), // Convertir l'ID en string
          title: `${res.event.title}`,
          start: `${res.event.dateEvent}T${res.event.heureEvent}`, // Format ISO
          backgroundColor: '#3b82f6',  // Bleu plus vif (tailwind blue-500)
          borderColor: '#1d4ed8',      // Bordure bleu foncé
          textColor: '#ffffff',        // Texte blanc
          display: 'block',           // Affichage en bloc
          className: 'custom-event'    // Classe CSS personnalisée
        }));

        // Injecte dans le calendrier
        this.calendarOptions.events = formattedEvents;
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des réservations :', err);
      }
    });
  }



}
