import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CommandesService {

  private apiUrl = 'http://localhost:8080/panier/commande/retrieveAllCommande';
  private Url =  'http://localhost:8080/panier/commande/retrieveCommande';

  constructor(private http: HttpClient) {}

  getCommandes(): Observable<any> {
    return this.http.get(this.apiUrl);
  }
  getCommandeById(id: number): Observable<any> {
    return this.http.get(`${this.Url}/${id}`);
  }
}
