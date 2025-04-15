import { Component, OnInit } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service';

interface DeliveryStats {
  [key: string]: number;
}
@Component({
  selector: 'app-admindashboard',
  templateUrl: './admindashboard.component.html',
  styleUrls: ['./admindashboard.component.css']
})
export class AdmindashboardComponent  implements OnInit {
  stats: DeliveryStats = {};
  isLoading = true;

  constructor(private dashboardService: DeliveriesService) { }

  ngOnInit(): void {
    this.loadStats();
  }

  loadStats(): void {
    this.dashboardService.getDeliveryStats().subscribe(
      (data: DeliveryStats) => {
        this.stats = data;
        this.isLoading = false;
      },
      (error) => {
        console.error('Erreur lors du chargement des statistiques', error);
        this.isLoading = false;
      }
    );
  }

  getTotalDeliveries(): number {
    return Object.values(this.stats).reduce((acc: number, val: unknown) => {
      // Vérification du type avant l'addition
      if (typeof val === 'number') {
        return acc + val;
      }
      return acc;
    }, 0);
  }
}
