import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DeliveriesService } from 'src/app/services/deliveries.service';
import JsBarcode from 'jsbarcode';
@Component({
  selector: 'app-detail-livraison',
  templateUrl: './detail-livraison.component.html',
  styleUrls: ['./detail-livraison.component.css']
})
export class DetailLivraisonComponent implements OnInit {
  delivery: any; 

  constructor(
    private route: ActivatedRoute,
    private deliveryService: DeliveriesService
  ) {}

  ngOnInit(): void {
    const id = +this.route.snapshot.paramMap.get('id')!; // Récupère l'ID depuis l'URL
    this.loadDeliveryDetails(id);
  }
  ngAfterViewInit(): void {
    if (this.delivery) {
      this.generateBarcodeFromReference();
    }
  }
  

  loadDeliveryDetails(id: number): void {
    this.deliveryService.getDeliveryDetails(id).subscribe({
      next: (data) => {
        this.delivery = data;
        setTimeout(() => this.generateBarcodeFromReference(), 0);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des détails:', err);
      }
    });
  }

  generateBarcodeFromReference(): void {
    const svg = document.getElementById('barcode');
    console.log('SVG:', svg);
    console.log('Référence:', this.delivery?.refLivraison);
  
    if (svg && this.delivery?.refLivraison) {
      JsBarcode(svg, this.delivery.refLivraison, {
        format: 'CODE128',
        lineColor: '#000',
        width: 2,
        height: 60,
        displayValue: false,
      });
    } else {
      console.warn('SVG introuvable ou référence de livraison manquante.');
    }
  }
  
  

  imprimer(): void {
    window.print();
  }
  
  

}

 
