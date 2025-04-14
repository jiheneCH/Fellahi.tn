import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable, Subject, tap } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PanierService {

  private baseUrl = 'http://localhost:8080/panier/commande'; // adapte si ton port est différent
  panierSubject = new Subject<any>();
  
  constructor(private http: HttpClient) { }

  // Récupérer la commande en cours du client
  getCommandeEnCours(clientId: number): Observable<any> {
    return this.http.get(`${this.baseUrl}/${clientId}/en-cours`);
  }

  // Méthode pour appliquer la réduction par fidélité
  appliquerReduction(commandeId: number, pointsFideliteUtilises: number): Observable<any> {
    return this.http.post(`${this.baseUrl}/reduction/${commandeId}?pointsFideliteUtilises=${pointsFideliteUtilises}`, {})
      .pipe(
        tap((updatedCommande: any) => {
          this.panierSubject.next(updatedCommande); // Mets à jour le panier avec la commande mise à jour
        })
      );
  }
  
updateQuantiteArticle(commandeId: number, articleId: number, nouvelleQuantite: number): Observable<any> {
  return this.http.put(`http://localhost:8080/panier/commande/updateArticleInCommande?idCommande=${commandeId}&idArticle=${articleId}&nouvelleQuantite=${nouvelleQuantite}`, {})
  
    .pipe(
      tap((updatedCommande: any) => {
        this.panierSubject.next(updatedCommande); // Mets à jour le panier avec la commande mise à jour
      })
    );
}
deleteArticleFromCommande(commandeId: number, articleId: number): Observable<any> {
  return this.http.delete(`${this.baseUrl}/deleteArticleInCommande?idCommande=${commandeId}&idArticle=${articleId}`);
}

}
