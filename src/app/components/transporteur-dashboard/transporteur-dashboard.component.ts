import { Component, OnInit } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service';
import { ActivatedRoute } from '@angular/router';

interface DeliveryStats {
  'Livraisons Annulées': number;
  'Livraisons En Cours': number;
  'Livraisons Livrées': number;
  'Livraisons En Attente': number;
  'Livraisons Retardé': number;
}

@Component({
  selector: 'app-transporteur-dashboard',
  templateUrl: './transporteur-dashboard.component.html',
  styleUrls: ['./transporteur-dashboard.component.css']
})
export class TransporteurDashboardComponent implements OnInit {
  stats: DeliveryStats = {
    'Livraisons Annulées': 0,
    'Livraisons En Cours': 0,
    'Livraisons Livrées': 0,
    'Livraisons En Attente': 0,
    'Livraisons Retardé': 0
  };
  isLoading = true;
  transporteurId!: number;

  // Liste des clés pour l'itération dans le template
  statKeys: Array<keyof DeliveryStats> = [
    'Livraisons Annulées',
    'Livraisons En Cours',
    'Livraisons Livrées',
    'Livraisons En Attente',
    'Livraisons Retardé'
  ];

  constructor(
    private livraisonService: DeliveriesService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.parent?.paramMap.subscribe(params => {
      const id = params.get('id');
      
      if (id) {
        this.transporteurId = Number(id);
        console.log('ID Transporteur récupéré:', this.transporteurId);
        this.loadStats();
      } else {
        console.error('ID Transporteur manquant');
      }
    });
  }

  loadStats(): void {
    this.livraisonService.getLivraisonsByTransporteurEtStatut(this.transporteurId)
      .subscribe({
        next: (data: DeliveryStats) => {
          console.log('Données reçues:', data);
          this.stats = data;
          this.isLoading = false;
        },
        error: (err) => {
          console.error('Erreur lors du chargement:', err);
          this.isLoading = false;
          // Réinitialiser les stats en cas d'erreur
          this.stats = {
            'Livraisons Annulées': 0,
            'Livraisons En Cours': 0,
            'Livraisons Livrées': 0,
            'Livraisons En Attente': 0,
            'Livraisons Retardé': 0
          };
        }
      });
  }

  getTotal(): number {
    return Object.values(this.stats).reduce((acc, val) => acc + val, 0);
  }

  getPercentage(value: number): string {
    const total = this.getTotal();
    return total > 0 ? ((value / total) * 100).toFixed(1) : '0';
  }
}