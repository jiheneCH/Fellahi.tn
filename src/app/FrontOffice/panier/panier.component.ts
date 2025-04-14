import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { forkJoin, Observable } from 'rxjs';
import { PanierService } from 'src/app/panier.service';

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

  constructor(private panierService: PanierService) {}

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

    this.panierService.getCommandeEnCours(this.clientId).subscribe({
      next: (data) => {
        this.commande = data;
        this.commandeId = data.idCommande;
        console.log('Commande en cours:', this.commande);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de la commande', err);
      }
    });
  }

  onSubmit() {
    this.applyLoyaltyPoints();
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
    this.panierService.updateQuantiteArticle(this.commandeId, detail.article.idArticle, nouvelleQuantite)
      .subscribe({
        next: () => this.loadCommandeEnCours(),
        error: (err) => {
          console.error('Erreur lors de la mise à jour de la quantité', err);
          this.errorMessage = 'Erreur lors de la mise à jour de la quantité.';
        }
      });
  }

  updateCart() {
    if (!this.commandeId || !this.commande?.detailsCommande) return;

    const updateRequests = this.commande.detailsCommande.map((detail: any) =>
      this.panierService.updateQuantiteArticle(this.commandeId!, detail.article.idArticle, detail.quantite)
    );

    forkJoin(updateRequests).subscribe({
      next: () => this.loadCommandeEnCours(),
      error: (err) => {
        console.error('Erreur lors de la mise à jour des quantités', err);
        this.errorMessage = 'Erreur lors de la mise à jour des quantités.';
      }
    });
  }

  deleteArticle(detail: any): void {
    const commandeId = this.commandeId;
    const articleId = detail.article.idArticle;

    if (!commandeId || !articleId) return;

    this.panierService.deleteArticleFromCommande(commandeId, articleId).subscribe(
      () => this.loadCommandeEnCours(),
      (error) => {
        console.error('Erreur lors de la suppression de l\'article:', error);
      }
    );
  }

}