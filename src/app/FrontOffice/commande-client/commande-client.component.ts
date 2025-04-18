import { Component, OnInit } from '@angular/core';
import { CommandeClientService } from 'src/app/commande-client.service';

@Component({
  selector: 'app-commande-client',
  templateUrl: './commande-client.component.html',
  styleUrls: ['./commande-client.component.css']
})
export class CommandeClientComponent implements OnInit {
  commandes: any[] = [];
  clientId: number | null = null;
  isLoading: boolean = false; // 🆕 Ajout de la propriété isLoading

  constructor(private commandeClientService: CommandeClientService) {}

  ngOnInit(): void {
    const storedId = localStorage.getItem('clientId');
    if (storedId) {
      this.clientId = parseInt(storedId, 10);
      this.loadCommandes();
    } else {
      console.warn("Aucun ID de client trouvé dans le localStorage");
    }
  }

  loadCommandes(): void {
    if (this.clientId !== null) {
      this.isLoading = true; // ✅ Afficher le spinner

      this.commandeClientService.getCommandesByClientId(this.clientId).subscribe(
        (data: any[]) => {
          this.commandes = data;
          this.isLoading = false; // ✅ Masquer le spinner
          console.log("Commandes récupérées :", this.commandes);
        },
        (error: any) => {
          this.isLoading = false;
          console.error("Erreur lors du chargement des commandes :", error);
        }
      );
    }
  }

  changerStatutCommande(idCommande: number): void {
    this.commandeClientService.changerStatutCommande(idCommande).subscribe(
      response => {
        console.log("Commande annulée avec succès :", response);
        this.loadCommandes();
      },
      error => {
        console.error("Erreur lors du changement de statut de la commande :", error);
      }
    );
  }

  refreshCommandes(): void {
    this.loadCommandes();
    console.log('Commandes rafraîchies !');
  }

  goBack(): void {
    window.history.back();
  }
}
