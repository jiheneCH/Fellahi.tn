import { Component, OnInit } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service';

interface DeliveryStats {
  'Livraisons Annulées': number;
  'Livraisons En Cours': number;
  'Livraisons Livrées': number;
  'Livraisons En Attente': number;
  'Livraisons Retardé': number;
}
@Component({
  selector: 'app-admindashboard',
  templateUrl: './admindashboard.component.html',
  styleUrls: ['./admindashboard.component.css']
})
export class AdmindashboardComponent  implements OnInit {
  stats: DeliveryStats = {
    'Livraisons Annulées': 0,
    'Livraisons En Cours': 0,
    'Livraisons Livrées': 0,
    'Livraisons En Attente': 0,
    'Livraisons Retardé': 0
  };
  isLoading = true;

  constructor(private dashboardService: DeliveriesService) {}

  ngOnInit(): void {
    this.loadStats();
  }

  gouvernorats: string[] = ['Tous', 'Tunis', 'Ariana', 'Ben Arous', 'Manouba', 'Sousse', 'Nabeul']; // Ajout de 'Tous'
  selectedGovernorate: string = 'Tous';  // Par défaut, 'Tous' est sélectionné


  // Méthode pour charger les statistiques
  loadStats(): void {
    if (this.selectedGovernorate === 'Tous') {
      this.dashboardService.getDeliveryStats().subscribe({
        next: (data: DeliveryStats) => {
          this.stats = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.isLoading = false;
        }
      });
    } else {
      // Si un gouvernorat spécifique est sélectionné, on appelle getStatsByDelegation
      this.dashboardService.getStatsByDelegation(this.selectedGovernorate).subscribe({
        next: (data: DeliveryStats) => {
          this.stats = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.isLoading = false;
        }
      });
    }
  }

  // Méthode appelée lorsque le gouvernorat change
  onGovernorateChange(governorate: string): void {
    this.selectedGovernorate = governorate;
    this.loadStats();  // Charger les statistiques en fonction du gouvernorat sélectionné
  }


  getTotal(): number {
    return Object.values(this.stats).reduce((acc, val) => acc + val, 0);
  }

  getPercentage(value: number): string {
    return ((value / this.getTotal()) * 100).toFixed(1);
  }
}
