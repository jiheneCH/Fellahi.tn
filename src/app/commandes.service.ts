import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommandesService {

  private apiUrl = 'http://localhost:8080/panier/commande/retrieveAllCommande';
  private Url =  'http://localhost:8080/panier/commande/retrieveCommande';
  private validerUrl = 'http://localhost:8080/panier/commande/validerCommande';  // URL pour valider une commande
  private annulerUrl = 'http://localhost:8080/panier/commande/annulerCommande';

  constructor(private http: HttpClient) {}

  getCommandes(): Observable<any> {
    return this.http.get(this.apiUrl);
  }

  getCommandeById(id: number): Observable<any> {
    return this.http.get(`${this.Url}/${id}`);
  }

  // Méthode pour valider une commande
  validerCommande(idCommande: number): Observable<any> {
    return this.http.put(`${this.validerUrl}/${idCommande}`, null);
  }

  // Méthode pour annuler une commande
  annulerCommande(idCommande: number): Observable<any> {
    return this.http.put(`${this.annulerUrl}/${idCommande}`, null);
  }
  getCommandeStats(): Observable<any> {
    return this.http.get<any>(`http://localhost:8080/panier/commande/commande-stats`);
  }
}
