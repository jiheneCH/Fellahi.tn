import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Article } from './models/article';

@Injectable({
  providedIn: 'root'
})
export class ShopService {

  selectedImageDataUrl: string | null = null;
  articles: Article[] = [];
  nomAgriculteur: string = '';
  private apiUrl = 'http://localhost:8080/panier/commande/addArticleToCommande'; // Assurez-vous que cette URL est correcte

  constructor(private http: HttpClient) {}

  addArticleToCommande(clientId: number, articleId: number, quantite: number): Observable<any> {
    return this.http.post(this.apiUrl, {
      idClient: clientId,
      idArticle: articleId,
      quantite: quantite
    });
  }
  
}
