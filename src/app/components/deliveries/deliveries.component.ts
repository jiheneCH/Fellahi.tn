import { Component } from '@angular/core';
import { DeliveriesService } from 'src/app/services/deliveries.service'; 
import { Delivery } from 'src/app/services/deliveries.model';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-deliveries',
  templateUrl: './deliveries.component.html',
  styleUrls: ['./deliveries.component.css']
})
export class DeliveriesComponent  {
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
        this.livraisonsService.getLivraisonsByTransporteurId(this.transporteurId).subscribe(
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
  marquerLivre(id: number): void {
    if (confirm('Marquer cette livraison comme livrée ?')) {
      this.livraisonsService.changerLivraison(id).subscribe({
        next: () => {
          const livraison = this.livraisons.find(l => l.id === id);
          if (livraison) {
            livraison.statut = 'LIVRE';
            livraison.dateLivraison = new Date().toISOString();
          }
        }
      });
    }
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
openInGoogleMaps(adresse: string | undefined, delegation: string | undefined): void {
  if (!adresse || !delegation) {
    alert('Adresse ou délégation du client introuvable');
    return;
  }

  const fullAddress = `${adresse}, ${delegation}`;
  const query = encodeURIComponent(fullAddress);
  const url = `https://www.google.com/maps/search/?api=1&query=${query}`;
  window.open(url, '_blank');
}


appliquerFiltres(): void {
  // Vérifie si l'ID du transporteur est bien défini et récupère les livraisons
  this.route.parent?.paramMap.subscribe(params => {
    const id = Number(params.get('id')); // Récupère l'ID du transporteur
    if (id) {
      this.livraisonsService.getLivraisonsByTransporteurId(id).subscribe(
        (data: any[]) => {
          // Transformation des données pour l'affichage
          this.livraisons = data.map(livraison => ({
            id: livraison.id,
            nomClient: livraison.client.nom,
            prenomClient: livraison.client.prenom,
            numClient: livraison.client.telephone,
            adresseClient: livraison.client.adresse,
            delClient: livraison.client.delegation,
            dateLivraison: livraison.dateLivraison,
            prixTotal: livraison.prixTotal,
            statut: livraison.statut,
            dateincid: livraison.dateLivraison
          }));

          // Appliquer le filtre par statut
          if (this.selectedStatut && this.selectedStatut !== 'TOUS') {
            this.livraisons = this.livraisons.filter(livraison => livraison.statut === this.selectedStatut);
          }

          // Appliquer le filtre par date si un intervalle de dates est défini
          if (this.dateRange.start && this.dateRange.end) {
            const start = new Date(this.dateRange.start).getTime();
            const end = new Date(this.dateRange.end).getTime();

            this.livraisons = this.livraisons.filter(livraison => {
              const livraisonDate = new Date(livraison.dateLivraison).getTime();
              return livraisonDate >= start && livraisonDate <= end;
            });
          }

          // Inverser l'ordre des livraisons
          this.livraisons = this.livraisons.reverse();
        },
        (error) => {
          console.error('Erreur lors de l\'application des filtres', error);
        }
      );
    }
  });
}

telephone: string = '';
rafraichirDonnees(): void {
  this.telephone = '';
  this.ngOnInit(); // ou une méthode spécifique comme this.chargerLivraisons();
}

rechercherParTelephone(): void {
  if (!this.telephone || !this.telephone.trim()) {
    console.warn('Numéro de téléphone requis');
    return;
  }

  this.livraisonsService.searchByTelephone(this.telephone.trim()).subscribe(
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
      console.error('Erreur de recherche :', error);
      this.livraisons = []; // Vide si aucune livraison
    }
  );
}
searchText: string = '';         // toutes les livraisons
livraisonsFiltres: any[] = [];
filtrerLivraisons(): void {
  const texte = this.searchText.toLowerCase().trim();
  this.livraisonsFiltres = this.livraisons.filter(livraison =>
    livraison.nomClient.toLowerCase().includes(texte) ||
    livraison.telephoneClient?.toLowerCase().includes(texte)
  );
}

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
