import { Component } from '@angular/core';
import { CalendarOptions } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import multiMonthPlugin from '@fullcalendar/multimonth';
@Component({
  selector: 'app-schedular',
  templateUrl: './schedular.component.html',
  styleUrls: ['./schedular.component.css']
})
export class SchedularComponent {

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
    events: [
      {
        id: '1',
        title: 'Send pictures/brochures of new products',
        start: '2025-02-11T10:00:00',
        end: '2025-02-11T13:00:00',
        backgroundColor: '#99e699'
      },
      {
        id: '2',
        title: 'Follow up and discuss the offer',
        start: '2025-02-12T08:30:00',
        end: '2025-02-12T11:30:00',
        backgroundColor: '#ff9999'
      },
      {
        id: '3',
        title: 'Obtain CEO contact information',
        start: '2025-02-13T07:00:00',
        end: '2025-02-13T10:00:00',
        backgroundColor: '#ffcc99'
      }
    ]
  };

  }