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

  constructor(private commandeClientService: CommandeClientService) {}

  ngOnInit(): void {
    const storedId = localStorage.getItem('clientId');
    if (storedId) {
      this.clientId = parseInt(storedId, 10);
      this.loadCommandes(); // Charger les commandes au démarrage
    } else {
      console.warn("Aucun ID de client trouvé dans le localStorage");
    }
  }

  loadCommandes(): void {
    if (this.clientId !== null) {
      this.commandeClientService.getCommandesByClientId(this.clientId).subscribe(
        (data: any[]) => {
          this.commandes = data;
          console.log("Commandes récupérées :", this.commandes);
        },
        (error: any) => {
          console.error("Erreur lors du chargement des commandes :", error);
        }
      );
    }
  }

  changerStatutCommande(idCommande: number): void {
    this.commandeClientService.changerStatutCommande(idCommande).subscribe(
      response => {
        console.log("Commande annulée avec succès :", response);
        this.loadCommandes();  // Rafraîchir la liste des commandes après le changement de statut
      },
      error => {
        console.error("Erreur lors du changement de statut de la commande :", error);
      }
    );
  }
}
