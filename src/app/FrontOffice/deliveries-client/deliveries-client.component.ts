import { Component } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service'; 
import { Delivery } from 'src/app/services/deliveries.model';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-deliveries-client',
  templateUrl: './deliveries-client.component.html',
  styleUrls: ['./deliveries-client.component.css']
})
export class DeliveriesClientComponent {
 livraisons: any[] = [];
  statutFiltre: string = 'EN_COURS';
  transporteurId: number | undefined;
  delivery: any;
  constructor(
    private livraisonsService: DeliveriesService,
    private router: Router,
    private route: ActivatedRoute
  ) { }
  
  ngOnInit(): void {
    // Récupérer l'ID du transporteur à partir de l'URL
    this.route.parent?.paramMap.subscribe(params => {
      this.transporteurId = Number(params.get('id'));
      if (this.transporteurId) {
        this.livraisonsService.findByClientId(this.transporteurId).subscribe(
          (data) => {
            this.livraisons = data.map(livraison => ({
              id: livraison.id,
              nomClient: livraison.client.nom,
              prenomClient: livraison.client.prenom,
              adresseClient: livraison.client.adresse,
              delClient: livraison.client.delegation,
              dateLivraison: livraison.dateLivraison,
              prixTotal: livraison.prixTotal,
              statut: livraison.statut,
              dateincid: livraison.dateLivraison
            })).reverse();
          },
          (error) => {
            console.error('Erreur lors du chargement des livraisons', error);
          }
        );
      }
    });
  }
  
  showDetails(id: number): void {
    this.router.navigate(['/livraison-detail', id]);
  }
  
  statuts: string[] = ['TOUS', 'EN_ATTENTE', 'EN_COURS', 'RETARDE', 'LIVRE', 'ANNULE'];
  selectedStatut: string = 'TOUS'; // Valeur par défaut
  chargerLivraisons(): void {
    this.livraisonsService.getLivraisonsByStatut(this.selectedStatut).subscribe({
      next: (data) => this.livraisons = data,
      error: (err) => console.error('Erreur:', err)
    });
  }
  
  onStatutChange(): void {
    this.chargerLivraisons();
  }
  // Ajoutez ces propriétés
dateRange = {
  start: null as Date | null,
  end: null as Date | null
};

// Méthode pour formater la date au format YYYY-MM-DD
private formatDate(date: Date): string {
  return formatDate(date, 'yyyy-MM-dd', 'en-US');
}

// Méthode pour filtrer par date
filtrerParDate(): void {
  if (this.dateRange.start && this.dateRange.end) {
    const start = this.formatDate(this.dateRange.start);
    const end = this.formatDate(this.dateRange.end);
    
    this.livraisonsService.getLivraisonsParDate(start, end).subscribe({
      next: (data) => this.livraisons = data,
      error: (err) => console.error('Erreur:', err)
    });
  }
}
annulerLivraison(id: number): void {
  if (confirm('Êtes-vous sûr de vouloir annuler cette livraison?')) {
    this.livraisonsService.annulerLivraison(id).subscribe({
      next: () => {
        // Update the status locally
        const livraison = this.livraisons.find(l => l.id === id);
        if (livraison) {
          livraison.statut = 'ANNULE';
        }
        alert('Livraison annulée avec succès');
      },
      error: (err) => {
        console.error('Erreur lors de l\'annulation:', err);
        alert('Échec de l\'annulation');
      }
    });
  }
}
telephone: string = '';
rafraichirDonnees(): void {
  this.telephone = '';
  this.ngOnInit(); // ou une méthode spécifique comme this.chargerLivraisons();
}


searchText: string = '';         // toutes les livraisons
livraisonsFiltres: any[] = [];


estModifiable(statut: string): boolean {
  const statutsModifiables = ['EN_ATTENTE', 'EN_COURS', 'RETARDE'];
  return statutsModifiables.includes(statut);
}
searchTerm: string = '';
allLivraisons: Delivery[] = []; 

rechercherParNomOuTelephone(): void {
  const texte = this.searchText.toLowerCase().trim();

  this.livraisonsFiltres = this.livraisons.filter(l =>
    l.nomClient.toLowerCase().includes(texte) ||
    l.prenomClient.toLowerCase().includes(texte) ||
    l.telephoneClient?.toLowerCase().includes(texte)
  );
}

page: number = 1;
pageSize: number = 5;

get paginatedLivraisons() {
  const start = (this.page - 1) * this.pageSize;
  return this.livraisons.slice(start, start + this.pageSize);
}

get totalPages(): number {
  return Math.ceil(this.livraisons.length / this.pageSize);
}

}
