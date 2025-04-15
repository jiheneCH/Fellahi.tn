import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DeliveriesService } from 'src/app/services/deliveries.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-reclamation-transporteur',
  templateUrl: './reclamation-transporteur.component.html'
})
export class ReclamationTransporteurComponent {
  typeIncident: string = '';
  descriptionIncident: string = '';
  livraisonId: number = 0;
  message: string = '';
  urgent: boolean = false; // Ajoutez cette ligne
  constructor(
    private livraisonService: DeliveriesService,
    private route: ActivatedRoute,
    private router: Router
  ) {}
  

  ngOnInit(): void {
    this.livraisonId = Number(this.route.snapshot.paramMap.get('id'));
  }
annuler(): void {
    this.router.navigate(['/transporteur/deliveries']);
  }
  signalerIncident() {
    this.livraisonService.signalerIncident(this.livraisonId, this.typeIncident, this.descriptionIncident)
      .subscribe({
        next: (res) => {
          this.message = res;
          // ✅ Redirection après succès
          this.router.navigate(['/transporteur/deliveries']);
        },
        error: (err) => {
          this.message = "Une erreur s'est produite.";
        }
      });
  }
  
}
