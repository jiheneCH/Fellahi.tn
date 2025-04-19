import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-chatbot',
  templateUrl: './chatbot.component.html',
  styleUrls: ['./chatbot.component.css']
})
export class ChatbotComponent {
  repas: string = ''; // Variable liée à l'input utilisateur
  suggestions: string[] = []; // Liste des suggestions

  constructor(private http: HttpClient) {}

  // Méthode qui va chercher les suggestions pour un repas
  getSuggestions() {
    const url = `http://localhost:8080/panier/commande/suggest?repas=${this.repas}`;

    // Effectuer une requête GET pour récupérer les suggestions depuis le back-end
    this.http.get<string[]>(url).subscribe({
      next: (data) => {
        this.suggestions = data; // Si la requête réussit, mettre à jour la liste
        console.log('Suggestions reçues:', data); // Pour vérifier si la réponse est correcte
      },
      error: (err) => {
        console.error('Erreur lors de la récupération des suggestions:', err); // Gestion des erreurs
      }
    });
  }

  ajouterAuPanier(ingredient: string) {
    console.log('🛒 Ajouter au panier :', ingredient); // Message de log pour l'ajout au panier
    // Tu peux ici ajouter la logique pour ajouter l'ingrédient au panier
  }
}
