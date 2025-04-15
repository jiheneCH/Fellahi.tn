import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DeliveriesService } from 'src/app/services/deliveries.service';
import { Observable } from 'rxjs';
import { Delivery } from 'src/app/services/deliveries.model';
@Component({
  selector: 'app-detail-livraison',
  templateUrl: './detail-livraison.component.html',
  styleUrls: ['./detail-livraison.component.css']
})
export class DetailLivraisonComponent implements OnInit {
  delivery: any; // Remplacez "any" par votre interface/model si possible

  constructor(
    private route: ActivatedRoute,
    private deliveryService: DeliveriesService
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!; // Récupère l'ID depuis l'URL
    this.loadDeliveryDetails(id);
  }

  loadDeliveryDetails(id: number): void {
    this.deliveryService.getDeliveryDetails(id).subscribe({
      next: (data) => {
        this.delivery = data; // Stocke les détails de la livraison
      },
      error: (err) => {
        console.error('Erreur lors du chargement des détails:', err);
      }
    });
  }

}

 
