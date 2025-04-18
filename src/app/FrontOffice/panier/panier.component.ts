import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { PanierService } from 'src/app/panier.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-panier',
  templateUrl: './panier.component.html',
  styleUrls: ['./panier.component.css']
})
export class PanierComponent implements OnInit {

  commande: any;
  clientId: number | null = null;
  commandeId: number | undefined;
  loyaltyPointsToApply: number = 0;
  clientPoints: number = 100;
  errorMessage: any;
  isLoading: boolean = false; // Ajout d'un état de chargement

  constructor(
    private panierService: PanierService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const storedId = localStorage.getItem('clientId');
    if (storedId) {
      this.clientId = parseInt(storedId, 10);
      this.loadCommandeEnCours();
    } else {
      console.warn("Aucun ID de client trouvé dans le localStorage");
    }
  }

  private loadCommandeEnCours(): void {
    if (this.clientId === null) return;

    this.isLoading = true; // Début du chargement
    this.panierService.getCommandeEnCours(this.clientId).subscribe({
      next: (data) => {
        this.commande = data;
        this.commandeId = data.idCommande;
        console.log('Commande en cours:', this.commande);
        this.isLoading = false; // Fin du chargement
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de la commande', err);
        this.isLoading = false; // Fin du chargement même en cas d'erreur
      }
    });
  }

  applyLoyaltyPoints() {
    if (!this.commandeId) {
      this.errorMessage = "ID de commande non disponible.";
      return;
    }

    this.panierService.appliquerReduction(this.commandeId, this.loyaltyPointsToApply)
      .subscribe({
        next: (response) => {
          console.log('Réduction appliquée avec succès', response);
          this.loadCommandeEnCours();
        },
        error: (err) => {
          console.error('Erreur lors de l\'application des points fidélité', err);
          this.errorMessage = err.error?.message || "Une erreur inconnue est survenue.";
        }
      });
  }

  updateArticle(detail: any) {
    if (!this.commandeId) return;

    const nouvelleQuantite = detail.quantite;
    this.isLoading = true; // Début du chargement
    this.panierService.updateQuantiteArticle(this.commandeId, detail.article.idArticle, nouvelleQuantite)
      .subscribe({
        next: () => {
          this.loadCommandeEnCours(); // Rafraîchir après mise à jour
          this.isLoading = false; // Fin du chargement
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de la quantité', err);
          this.isLoading = false; // Fin du chargement même en cas d'erreur
          this.errorMessage = 'Erreur lors de la mise à jour de la quantité.';
        }
      });
  }

  updateCart() {
    if (!this.commandeId || !this.commande?.detailsCommande) return;

    this.isLoading = true; // Début du chargement
    const updateRequests = this.commande.detailsCommande.map((detail: any) =>
      this.panierService.updateQuantiteArticle(this.commandeId!, detail.article.idArticle, detail.quantite)
    );

    forkJoin(updateRequests).subscribe({
      next: () => {
        this.loadCommandeEnCours(); // Rafraîchir après mise à jour
        this.isLoading = false; // Fin du chargement
      },
      error: (err) => {
        console.error('Erreur lors de la mise à jour des quantités', err);
        this.isLoading = false; // Fin du chargement même en cas d'erreur
        this.errorMessage = 'Erreur lors de la mise à jour des quantités.';
      }
    });
  }

  deleteArticle(detail: any): void {
    const commandeId = this.commandeId;
    const articleId = detail.article.idArticle;

    if (!commandeId || !articleId) return;

    this.isLoading = true; // Début du chargement
    this.panierService.deleteArticleFromCommande(commandeId, articleId).subscribe(
      () => {
        this.loadCommandeEnCours(); // Rafraîchir après suppression
        this.isLoading = false; // Fin du chargement
      },
      (error) => {
        console.error('Erreur lors de la suppression de l\'article:', error);
        this.isLoading = false; // Fin du chargement même en cas d'erreur
      }
    );
  }

  refreshOrders(): void {
    this.loadCommandeEnCours(); // Rafraîchir la commande
    console.log('Commande rafraîchie!');
  }

  goBack(): void {
    window.history.back(); // Revenir à la page précédente
  }
}
