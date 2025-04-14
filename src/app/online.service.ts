import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class OnlineService {

  private apiUrl = 'http://localhost:8080/panier/commande';  // Remplace par l'URL correcte de ton API backend

  constructor(private http: HttpClient) {}

  // Récupérer les infos de commande
  getCommandeEnCours(clientId: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/en-cours/${clientId}`);
  }

  // Valider la commande après le paiement
  validerCommandeOnline(commandeId: number): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/valider-online/${commandeId}`, {});
  }
}
