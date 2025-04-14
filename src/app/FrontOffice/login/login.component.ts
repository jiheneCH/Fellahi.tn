import { HttpClient } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMSG: string = '';
  isloading: boolean = false;
  clientName: string = '';  // Le nom du client à rechercher
  clientId: number | null = null; // L'ID récupéré du client

  loginform: FormGroup = this._FormBuilder.group({
    name: ['', [Validators.required]],  // Nom du client (champ obligatoire)
    
  });

  constructor(private _Router: Router, private _FormBuilder: FormBuilder, private httpClient: HttpClient) {}

  // Méthode appelée pour récupérer l'ID du client basé sur son nom
  fetchClientId() {
    if (!this.clientName.trim()) {
      console.warn("Nom du client vide");
      return;
    }

    this.httpClient.get<number>('http://localhost:8080/panier/commande/client/id', {
      params: { name: this.clientName }
    }).subscribe({
      next: (id) => {
        this.clientId = id;
        localStorage.setItem('clientId', id.toString()); // 🧠 stock clientId
        console.log(`ID récupéré pour ${this.clientName} : ${id}`);
        // Vous pouvez aussi ajouter la logique pour rediriger ou charger l'autre page ici
        this._Router.navigate(['/shop']); // Redirection vers la page shop après la connexion
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de l’ID :', err);
        this.clientId = null;
      }
    });
  }

  // Fonction pour soumettre le formulaire de connexion
  onLogin() {
    if (this.loginform.valid) {
      this.clientName = this.loginform.value.name;  // Récupère le nom du client depuis le formulaire
      this.fetchClientId();  // Recherche l'ID du client

      // Vous pouvez ajouter la logique pour gérer le mot de passe et la redirection ici
      console.log('Formulaire de connexion valide.');
    } else {
      console.log('Formulaire invalide');
    }
  }
}
