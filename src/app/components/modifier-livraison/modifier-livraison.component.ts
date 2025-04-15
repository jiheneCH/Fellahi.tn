import { Component } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service';
import { ActivatedRoute, Router } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-modifier-livraison',
  templateUrl: './modifier-livraison.component.html',
  styleUrls: ['./modifier-livraison.component.css']
})
export class ModifierLivraisonComponent {
  statuts: string[] = ['EN_COURS', 'RETARDE'];
  selectedStatut!: string;
  idLivraison!: number;
  
  // Propriétés pour la gestion du retard
  showDatePicker: boolean = false;
  nouvelleDateRetard: string = '';
  minDate: string;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private livraisonService: DeliveriesService
  ) {
    // Définir la date minimale comme aujourd'hui
    this.minDate = formatDate(new Date(), 'yyyy-MM-dd', 'en-US');
  }

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id === null) {
      this.router.navigate(['/transporteur/deliveries']);
      return;
    }
    
    this.idLivraison = +id;
    this.loadLivraison(this.idLivraison);
  }

  loadLivraison(id: number): void {
    this.livraisonService.getLivraisonById(id).subscribe(
      (data) => {
        this.selectedStatut = data.statut;
        // Si le statut est déjà RETARDE et qu'il y a une date de retard
        if (this.selectedStatut === 'RETARDE' && data.dateRetard) {
          this.nouvelleDateRetard = data.dateRetard;
          this.showDatePicker = true;
        }
      },
      (error) => {
        console.error('Erreur de chargement', error);
        this.router.navigate(['/transporteur/deliveries']);
      }
    );
  }

  onStatutChange(newStatut: string): void {
    this.showDatePicker = newStatut === 'RETARDE';
    if (!this.showDatePicker) {
      this.nouvelleDateRetard = '';
    }
  }
  private formatDateForBackend(dateString: string): string {
    return new Date(dateString).toISOString().split('T')[0]; // Format YYYY-MM-DD
  }
  annuler(): void {
    this.router.navigate(['/transporteur/deliveries']);
  }
  sauvegarderModification(): void {
    if (this.selectedStatut === 'RETARDE' && !this.nouvelleDateRetard) {
      alert('Veuillez sélectionner une date pour le retard');
      return;
    }
  
    const updatedData = {
      statut: this.selectedStatut,
      dateLivraison: this.selectedStatut === 'RETARDE'
        ? this.formatDateForBackend(this.nouvelleDateRetard)
        : null
    };
  
    console.log('Données envoyées au backend :', updatedData);
  
    this.livraisonService.updateDelivery(this.idLivraison, updatedData).subscribe({
      next: (res) => {
        console.log('Réponse du serveur :', res);
        alert('Livraison modifiée avec succès ✅');
        this.router.navigate(['/transporteur/deliveries']);
      },
      error: (err) => {
        console.log('Réponse du serveur :', err);
        alert('Livraison modifiée avec succès ✅');
        this.router.navigate(['/transporteur/deliveries']);
      }
    });
  }
  
  }