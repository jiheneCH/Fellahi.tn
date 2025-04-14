import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { CommandesService } from 'src/app/commandes.service';

interface Commande {
  idCommande: number;
  prixTotalCommande: number;
  pointfidelityTotalCommande: number;
  referenceCommande: string | null;
  status: string;
  dateCommande: string;  // La date est retournée sous forme de chaîne ISO 8601
  client: {
    idClient: number;
    name: string;
    phone: string;
    address: string;
    pointFidelityClient: number;
  };
  detailsCommande: [
    {
      idDetailsCommande: number;
      quantite: number;
      prixTotalArticle: number;
      pointfidelityTotalArticle: number;
      article: {
        idArticle: number;
        nom: string;
        reference: string;
        pointsFidelite: number;
        statusArticle: string;
        prix: number;
      };
    }
  ];
}

@Component({
  selector: 'app-commandes-liste',
  templateUrl: './commandes-liste.component.html',
  styleUrls: ['./commandes-liste.component.css']
})
export class CommandesListeComponent implements OnInit {
  commandes: Commande[] = [];  // Liste complète des commandes
  filteredCommandes: Commande[] = [];  // Liste filtrée des commandes
  searchQuery: string = '';  // Valeur de recherche
  selectedCommande: Commande | null = null;  // Commande sélectionnée pour afficher ses détails

  private apiUrl = 'http://localhost:8080/panier/commande/retrieveAllCommande';  // URL de l'API

  constructor(private commandesService: CommandesService) {}

  ngOnInit(): void {
    this.loadCommandes();  // Charge les commandes au démarrage
  }

  loadCommandes(): void {
    this.commandesService.getCommandes().subscribe(
      (data) => {
        // Filtre les commandes pour ne garder que celles avec le statut 'valider'
        this.commandes = data.filter((commande: Commande) => commande.status === 'valider');
        this.filteredCommandes = this.commandes;  // Initialement, afficher toutes les commandes validées
      },
      (error) => {
        console.error('Erreur lors du chargement des commandes', error);
      }
    );
  }

  // Méthode de filtrage des commandes en fonction de la recherche
  filterCommandes(): void {
    const query = this.searchQuery.toLowerCase();  // Convertir la recherche en minuscule pour rendre la recherche insensible à la casse
    this.filteredCommandes = this.commandes.filter(commande =>
      // Vérifier si la recherche correspond au nom du client, à la date ou à la référence
      commande.client.name.toLowerCase().includes(query) ||
      commande.dateCommande.toLowerCase().includes(query) ||
      (commande.referenceCommande && commande.referenceCommande.toLowerCase().includes(query))
    );
  }

  // Méthode pour afficher ou masquer les détails d'une commande
  toggleDetails(commande: Commande): void {
    if (this.selectedCommande === commande) {
      this.selectedCommande = null;  // Si déjà sélectionnée, on la désélectionne
    } else {
      this.selectedCommande = commande;  // Sélectionne la commande et affiche ses détails
      this.fetchCommandeDetails(commande.idCommande);  // Récupère les détails de la commande
    }
  }

  // Méthode pour récupérer les détails d'une commande
  fetchCommandeDetails(id: number): void {
    this.commandesService.getCommandeById(id).subscribe(
      (data) => {
        this.selectedCommande = data;  // Mettre à jour la commande sélectionnée avec les détails
      },
      (error) => {
        console.error('Erreur lors de la récupération des détails de la commande', error);
      }
    );
  }
}