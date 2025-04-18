import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommandesService } from 'src/app/commandes.service';

export enum StatusPayment {
  PENDING = 'PENDING',
  ONSITE = 'ONSITE',
  ONLINE = 'ONLINE'
}

export enum StatusCommande {
  Processing = 'Processing',
  Pending = 'Pending',
  Confirmed = 'Confirmed',
  Cancelled = 'Cancelled'
}

interface User {
  id: number;
  username: string;
  phone: string;
  address: string;
  pointFidelityClient: number;
}

interface Article {
  idArticle: number;
  nom: string;
  reference: string;
  pointsFidelite: number;
  prix: number;
  statusAgri: string;
}

interface DetailsCommande {
  idDetailsCommande: number;
  quantite: number;
  prixTotalArticle: number;
  pointfidelityTotalArticle: number;
  article: Article;
}

interface Commande {
  idCommande: number;
  prixTotalCommande: number;
  pointfidelityTotalCommande: number;
  referenceCommande: string | null;
  statusCommande: StatusCommande;
  payment: StatusPayment;
  dateCommande: string;
  user: User;
  detailsCommande: DetailsCommande[];
}

@Component({
  selector: 'app-commandes-liste',
  templateUrl: './commandes-liste.component.html',
  styleUrls: ['./commandes-liste.component.css']
})
export class CommandesListeComponent implements OnInit {
  commandes: Commande[] = [];
  filteredCommandes: Commande[] = [];
  paginatedCommandes: Commande[] = [];
  recentCommandes: Commande[] = []; // Nouvelle propriété pour les commandes récentes
  scrollPaused: boolean = false; // Nouvelle propriété pour le défilement

  // Filtres
  searchCustomer: string = '';
  searchDate: string = '';
  searchReference: string = '';
  searchStatus: string = '';
  searchPayment: string = '';

  // Données de filtre
  statusList = Object.values(StatusCommande);
  paymentList = Object.values(StatusPayment);

  // Pagination
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;

  // Tri
  sortColumn: string = '';
  sortDirection: 'asc' | 'desc' = 'asc';

  constructor(private commandesService: CommandesService, private router: Router) {}

  ngOnInit(): void {
    this.loadCommandes();
  }

  loadCommandes(): void {
    this.commandesService.getCommandes().subscribe({
      next: (data: Commande[]) => {
        this.commandes = data.filter(cmd => cmd.statusCommande !== StatusCommande.Processing);
        this.filterCommandes();
        
        // Mettre à jour les commandes récentes
        this.recentCommandes = [...data]
          .sort((a, b) => new Date(b.dateCommande).getTime() - new Date(a.dateCommande).getTime())
          .slice(0, 5);
      },
      error: (err) => {
        console.error('Erreur lors du chargement des commandes', err);
      }
    });
  }

  // Méthodes pour gérer le défilement automatique
  pauseScroll(): void {
    this.scrollPaused = true;
  }

  resumeScroll(): void {
    this.scrollPaused = false;
  }

  // ... (le reste de vos méthodes existantes reste inchangé)
  onFilterChange(): void {
    this.filterCommandes();
  }

  filterCommandes(): void {
    const filtered = this.commandes.filter(commande => {
      const matchesCustomer = commande.user.username.toLowerCase().includes(this.searchCustomer.toLowerCase());
      const matchesDate = this.searchDate ? commande.dateCommande.includes(this.searchDate) : true;
      const matchesReference = this.searchReference
        ? commande.referenceCommande?.toLowerCase().includes(this.searchReference.toLowerCase()) || false
        : true;
      const matchesStatus = this.searchStatus
        ? commande.statusCommande.toLowerCase() === this.searchStatus.toLowerCase()
        : true;
      const matchesPayment = this.searchPayment
        ? commande.payment.toLowerCase() === this.searchPayment.toLowerCase()
        : true;

      return matchesCustomer && matchesDate && matchesReference && matchesStatus && matchesPayment;
    });

    if (this.sortColumn) {
      filtered.sort((a, b) => {
        const valA = this.getValueByPath(a, this.sortColumn);
        const valB = this.getValueByPath(b, this.sortColumn);
        if (valA < valB) {
          return this.sortDirection === 'asc' ? -1 : 1;
        }
        if (valA > valB) {
          return this.sortDirection === 'asc' ? 1 : -1;
        }
        return 0;
      });
    }

    this.filteredCommandes = filtered;
    this.totalPages = Math.max(1, Math.ceil(this.filteredCommandes.length / this.itemsPerPage));
    this.currentPage = 1;
    this.updatePagination();
  }

  updatePagination(): void {
    const start = (this.currentPage - 1) * this.itemsPerPage;
    const end = start + this.itemsPerPage;
    this.paginatedCommandes = this.filteredCommandes.slice(start, end);
  }

  changePage(direction: number): void {
    const newPage = this.currentPage + direction;
    if (newPage >= 1 && newPage <= this.totalPages) {
      this.currentPage = newPage;
      this.updatePagination();
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.updatePagination();
    }
  }

  getPageNumbers(): number[] {
    const pages: number[] = [];
    const maxVisiblePages = 5;
    let startPage = Math.max(1, this.currentPage - Math.floor(maxVisiblePages / 2));
    let endPage = startPage + maxVisiblePages - 1;
    
    if (endPage > this.totalPages) {
      endPage = this.totalPages;
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }
    
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    
    return pages;
  }

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    } else {
      this.sortColumn = column;
      this.sortDirection = 'asc';
    }
    this.filterCommandes();
  }

  getValueByPath(obj: any, path: string): any {
    return path.split('.').reduce((o, key) => o && o[key], obj);
  }

  validerCommande(idCommande: number): void {
    this.commandesService.validerCommande(idCommande).subscribe({
      next: () => this.loadCommandes(),
      error: err => console.error('Erreur lors de la validation de la commande', err)
    });
  }

  annulerCommande(idCommande: number): void {
    this.commandesService.annulerCommande(idCommande).subscribe({
      next: () => this.loadCommandes(),
      error: err => console.error('Erreur lors de l\'annulation de la commande', err)
    });
  }

  viewDetails(idCommande: number): void {
    this.router.navigate(['/commande-details', idCommande]);
  }

  refreshOrders(): void {
    this.loadCommandes();
  }

  getStatusIcon(status: string): string {
    switch(status.toLowerCase()) {
      case 'confirmed': return 'fas fa-check-circle';
      case 'cancelled': return 'fas fa-times-circle';
      case 'processing': return 'fas fa-cog';
      default: return 'fas fa-clock'; // Pour 'pending'
    }
  }
  
  getPaymentIcon(payment: string): string {
    switch(payment.toLowerCase()) {
      case 'online': return 'fas fa-credit-card';
      case 'onsite': return 'fas fa-money-bill-wave';
      default: return 'fas fa-wallet'; // Pour 'pending'
    }
  }

  getRandomPastelColor(): string {
    const hue = Math.floor(Math.random() * 360);
    return `hsl(${hue}, 70%, 80%)`;
  }
}