import { Component } from '@angular/core';
import { ReservationService } from 'src/app/reservation.service';
import { ReservationAnalyticsResponse } from 'src/app/ReservationAnalyticsResponse';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {


  // dashboard.component.ts
analytics: ReservationAnalyticsResponse | null = null;
isLoading = true;
 

constructor(private reservationService: ReservationService) {}
ngOnInit(): void {
  this.reservationService.getReservationAnalytics().subscribe({
    next: (data) => {
      this.analytics = data;
      this.isLoading = false;
    },
    error: (err) => {
      console.error("Erreur lors du chargement des stats :", err);
      this.isLoading = false;
    }
  });
}


}
