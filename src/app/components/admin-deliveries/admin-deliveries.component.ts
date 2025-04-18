import { Component } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service'; 
import { Delivery } from 'src/app/services/deliveries.model';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';
@Component({
  selector: 'app-admin-deliveries',
  templateUrl: './admin-deliveries.component.html',
  styleUrls: ['./admin-deliveries.component.css']
})
export class AdminDeliveriesComponent {
 livraisons: any[] = [];

  constructor(private livraisonsService: DeliveriesService,  private router: Router) {}

  ngOnInit(): void {
    // Appel du service pour récupérer les données des livraisons
    this.livraisonsService.getLivraisons().subscribe(
      (data) => {
        this.livraisons = data.map(livraison => ({
          id: livraison.id,
          nomClient: livraison.client.nom,
          prenomClient: livraison.client.prenom,
          adresseClient: livraison.client.adresse,
          delClient: livraison.client.delegation,
          dateLivraison: livraison.dateLivraison,
          prixTotal: livraison.prixTotal,
          statut: livraison.statut
        })).reverse(); // Inverser l'ordre
      },
      (error) => {
        console.error('Erreur lors du chargement des livraisons', error);
      }
    );
  }
  
  showDetails(id: number): void {
    this.router.navigate(['/livraison-detail', id]);
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
  archiverLivraison(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir archiver cette livraison?')) {
      this.livraisonsService.archiverLivraison(id).subscribe({
        next: () => {
          // Mettre à jour le statut localement
          const livraison = this.livraisons.find(l => l.id === id);
          if (livraison) {
            livraison.archived = true;
          }
          alert('Livraison archivée avec succès');
        },
        error: (err) => {
          console.error('Erreur lors de l\'archivage:', err);
          alert('Échec de l\'archivage');
        }
      });
    }
  }
  telephone: string = '';
rafraichirDonnees(): void {
  this.telephone = '';
  this.ngOnInit(); // ou une méthode spécifique comme this.chargerLivraisons();
}


   statuts: string[] = ['TOUS', 'EN_ATTENTE', 'EN_COURS', 'RETARDE', 'LIVRE', 'ANNULE'];
    selectedStatut: string = 'TOUS'; // Valeur par défaut
    delegation: string[] = ['TOUS', 'Ariana', 'Tunis', 'Mahdia'];
    selectedDel: string = 'TOUS';

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
  
  appliquerFiltres(): void {
    this.livraisonsService.getLivraisonsByStatut(this.selectedStatut).subscribe(
      (data) => {
      this.livraisons = data.map(livraison => ({
        id: livraison.id,
        nomClient: livraison.client.nom,
        prenomClient: livraison.client.prenom,
        adresseClient: livraison.client.adresse,
        delClient: livraison.client.delegation,
        dateLivraison: livraison.dateLivraison,
        prixTotal: livraison.prixTotal,
        statut: livraison.statut
      })).reverse(); // Inverser l'ordre
  
        // Si un filtre par date est sélectionné, l'appliquer sur le résultat
        if (this.dateRange.start && this.dateRange.end) {
          const start = new Date(this.dateRange.start).getTime();
          const end = new Date(this.dateRange.end).getTime();
  
          this.livraisons = this.livraisons.filter(livraison => {
            const livraisonDate = new Date(livraison.dateLivraison).getTime();
            return livraisonDate >= start && livraisonDate <= end;
          });
        }
      },
      (error) => {
        console.error('Erreur lors de l\'application des filtres', error);
      }
    );
  }
  getLivraisons(): void {
    this.livraisonsService.getLivraisons().subscribe({
      next: (data) => {
        this.livraisons = data;
      },
      error: (err) => {
        console.error('Erreur lors du chargement des livraisons :', err);
      }
    });
  }
  
  onReaffecter(id: number): void {
    if (confirm('Êtes-vous sûr de vouloir réaffecter cette livraison ?')) {
      this.livraisonsService.reaffecterLivraison(id).subscribe({
        next: (data) => {
          this.livraisons = (data as any[]).map((livraison: any) => ({
            id: livraison.id,
            nomClient: livraison.client.nom,
            prenomClient: livraison.client.prenom,
            adresseClient: livraison.client.adresse,
            delClient: livraison.client.delegation,
            dateLivraison: livraison.dateLivraison,
            prixTotal: livraison.prixTotal,
            statut: livraison.statut
          })).reverse();
          alert('Livraison réaffectée avec succès ✅');
        },
        
        error: (err) => {
          console.error('Erreur lors de la réaffectation :', err);
          alert('Erreur lors de la réaffectation ❌');
        }
      });
    }
  }
  searchTerm: string = '';
  page: number = 1;
  pageSize: number = 5;
  
  get paginatedLivraisons() {
    const start = (this.page - 1) * this.pageSize;
    return this.livraisons.slice(start, start + this.pageSize);
  }
  
  get totalPages(): number {
    return Math.ceil(this.livraisons.length / this.pageSize);
  }  
  searchLivraison() {
    const searchValue = this.searchTerm.trim();
  
    if (!searchValue) return;
  
    const searchNumber = String(searchValue); // Convertir en number
  
  
  
    const isPhone = /^[0-9]{8}$/.test(searchValue); // 8 chiffres, typique pour un numéro de téléphone
  
    if (isPhone) {
      this.livraisonsService.searchByTelephone(searchNumber).subscribe(
        (data: any[]) => {
          if (data && data.length > 0) {
            this.livraisons = data.map(liv => ({
              id: liv.id,
              nomClient: liv.client.nom,
              prenomClient: liv.client.prenom,
              adresseClient: liv.client.adresse,
              delClient: liv.client.delegation,
              dateLivraison: liv.dateLivraison,
              prixTotal: liv.prixTotal,
              statut: liv.statut
            }));
          } else {
            this.livraisons = [];
          }
        },
        (error) => {
          console.error('Erreur lors de la recherche par téléphone :', error);
          this.livraisons = [];
        }
      );
    } else {
      // Recherche par ID
      this.livraisonsService.getLivraisonByRef(searchNumber).subscribe(
        (data: any) => {
          if (data) {
            this.livraisons = [{
              id: data.id,
              nomClient: data.client.nom,
              prenomClient: data.client.prenom,
              adresseClient: data.client.adresse,
              delClient: data.client.delegation,
              dateLivraison: data.dateLivraison,
              prixTotal: data.prixTotal,
              statut: data.statut
            }];
          } else {
            this.livraisons = [];
          }
        },
        (error) => {
          console.error('Erreur lors de la recherche par ID :', error);
          this.livraisons = [];
        }
      );
    }
  }
  
}
