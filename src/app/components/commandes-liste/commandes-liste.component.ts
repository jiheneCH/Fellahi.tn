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

interface Commande {
  idCommande: number;
  prixTotalCommande: number;
  pointfidelityTotalCommande: number;
  referenceCommande: string | null;
  statusCommande: StatusCommande;
  payment: StatusPayment;
  dateCommande: string;
  user: {
    id: number;
    username: string;
    phone: string;
    address: string;
    pointFidelityClient: number;
  };
  detailsCommande: {
    idDetailsCommande: number;
    quantite: number;
    prixTotalArticle: number;
    pointfidelityTotalArticle: number;
    article: {
      idArticle: number;
      nom: string;
      reference: string;
      pointsFidelite: number;
      prix: number;
      statusAgri: string;
    };
  }[];
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
  searchCustomer: string = '';
  searchDate: string = '';
  searchReference: string = '';
  searchStatus: string = '';
  selectedCommande: Commande | null = null;
  currentPage: number = 1;
  itemsPerPage: number = 10;
  totalPages: number = 1;
  sortColumn: string = '';
  sortAsc: boolean = true;

  constructor(private commandesService: CommandesService, private router: Router) {}

  ngOnInit(): void {
    this.loadCommandes();
  }

  loadCommandes(): void {
    this.commandesService.getCommandes().subscribe(
      (data: Commande[]) => {
        this.commandes = data;
        this.totalPages = Math.ceil(this.commandes.length / this.itemsPerPage);
        this.filterCommandes();
      },
      (error) => {
        console.error('Erreur lors du chargement des commandes', error);
      }
    );
  }

  filterCommandes(): void {
    let filtered = this.commandes.filter(commande => {
      const matchesCustomer = commande.user.username.toLowerCase().includes(this.searchCustomer.toLowerCase());
      const matchesDate = this.searchDate ? commande.dateCommande.includes(this.searchDate) : true;
      const matchesReference = commande.referenceCommande?.toLowerCase().includes(this.searchReference.toLowerCase()) || false;
      const matchesStatus = this.searchStatus ? commande.statusCommande.toLowerCase().includes(this.searchStatus.toLowerCase()) : true;

      return matchesCustomer && matchesDate && matchesReference && matchesStatus;
    });

    if (this.sortColumn) {
      filtered.sort((a, b) => {
        let valA = this.getValueByPath(a, this.sortColumn);
        let valB = this.getValueByPath(b, this.sortColumn);
        return this.sortAsc
          ? (valA > valB ? 1 : -1)
          : (valA < valB ? 1 : -1);
      });
    }

    this.filteredCommandes = filtered;
    this.updatePagination();
  }

  updatePagination(): void {
    this.totalPages = Math.ceil(this.filteredCommandes.length / this.itemsPerPage);
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

  sortBy(column: string): void {
    if (this.sortColumn === column) {
      this.sortAsc = !this.sortAsc;
    } else {
      this.sortColumn = column;
      this.sortAsc = true;
    }
    this.filterCommandes();
  }

  getValueByPath(obj: any, path: string): any {
    return path.split('.').reduce((o, key) => o && o[key], obj);
  }

  validerCommande(idCommande: number): void {
    this.commandesService.validerCommande(idCommande).subscribe(
      () => this.loadCommandes(),
      (error) => console.error('Erreur lors de la validation', error)
    );
  }

  annulerCommande(idCommande: number): void {
    this.commandesService.annulerCommande(idCommande).subscribe(
      () => this.loadCommandes(),
      (error) => console.error('Erreur lors de l\'annulation', error)
    );
  }

  viewDetails(idCommande: number): void {
    this.router.navigate(['/commande-details', idCommande]);
  }

  refreshOrders(): void {
    this.loadCommandes();
  }
}
