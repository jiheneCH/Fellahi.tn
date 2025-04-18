import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommandeClientService {

  private baseUrl = 'http://localhost:8080/panier/commande/client/'; // Changez ce lien si votre API a un autre port

  constructor(private http: HttpClient) {}

  getCommandesByClientId(clientId: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/panier/commande/client/${clientId}`);
  }
  
  changerStatutCommande(idCommande: number): Observable<any> {
    const url = `http://localhost:8080/panier/commande/commandes/status/${idCommande}`;
    console.log('URL de la requête PUT:', url);  // Affiche l'URL pour le débogage
    return this.http.put<any>(url, {});
  }
  
  
}
