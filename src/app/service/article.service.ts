import { Injectable } from '@angular/core';
import { HttpClient ,HttpHeaders, HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { Article } from '../models/article.model';

@Injectable({
  providedIn: 'root'
})
export class ArticleService {
  selectedImageDataUrl: string | null = null;
  articles: Article[] = [];
  nomAgriculteur: string = '';

  private apiUrl = 'http://localhost:8080/PI_article/PI_article'
  constructor(private http: HttpClient) {}

  getAllArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/retriveAllArticle`);
  }

  addArticle(article: Article, nomAgriculteur: string): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}/addArticle`, article);
  }

  getArticleById(id: number): Observable<Article> {
    return this.http.get<Article>(`${this.apiUrl}/retrieveArticle/${id}`);
  }

  // Mettre à jour un article
  updateArticle(article: Article): Observable<Article> {
    return this.http.put<Article>(`${this.apiUrl}/updateArticle`, article);
  }
  

  // Archiver un article
  archiveArticle(articleId: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/archiverArticle/${articleId}`, {});
  }
  genererPack(): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}/genererPack`, {});
  }
  getTendanceArticles(): Observable<Article[]> {
    return this.http.get<Article[]>(`${this.apiUrl}/tendanceeee`);
  }
  searchArticles(nom: string, status: string, typeProduit: string): Observable<Article[]> {
    const params = {
      nom,
      status,
      typeProduit,
    };
    return this.http.get<Article[]>(`${this.apiUrl}/rechercherArticles`, { params });
  } 
  getArticleQrCode(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/articles/${id}/qrcode`, {
      responseType: 'blob' // Important pour recevoir une image
    });
  }
  

  ajouterArticle(nom: string, article: Article): Observable<Article> {
    return this.http.post<Article>(`${this.apiUrl}/ajouter/${nom}`, article);
  }
  
 // Méthode pour générer le pack en fonction du nom de l'agriculteur
 genererPackParNomAgriculteurrr(nomUtilisateur: string): Observable<any> {
  return this.http.post(`${this.apiUrl}/generer-pack/${nomUtilisateur}`, {});
}
genererPackk(idUtilisateur: number): Observable<Article> {
  return this.http.post<Article>(`${this.apiUrl}/generer-pack/${idUtilisateur}`, {});
}
  
  


// Récupérer les articles d'un agriculteur
getArticlesParAgriculteurrr(id: number): Observable<Article[]> {
  return this.http.get<Article[]>(`${this.apiUrl}/afficherproduitagriculteur/${id}`);
}
ajouterrArticle(id: number, article: Article): Observable<Article> {
  console.log('Article envoyé:', article); // Log l'article envoyé
  return this.http.post<Article>(`${this.apiUrl}/ajouterproduitagriculteur/${id}`, article).pipe(
    catchError((error) => {
      console.error('Erreur lors de l\'ajout de l\'article:', error);  // Capture et affiche l'erreur
      if (error.status === 400) {
        console.error('Erreur 400 - Mauvaise requête. Vérifiez les données envoyées.');
      }
      throw error;  // Renvoyer l'erreur pour un traitement ultérieur
    })
  );
}
















getAjoutsParJour() {
  return this.http.get<any[]>(`${this.apiUrl}/ajouts-par-jour`);
}

getChiffreAffairesTotal() {
  return this.http.get<number>(`${this.apiUrl}/chiffre-affaires`);
}

getChiffreAffairesParArticle() {
  return this.http.get<{ [key: string]: any }>(`${this.apiUrl}/chiffre-affaires-article`);
}

getQuantiteTotalVendue() {
  return this.http.get<number>(`${this.apiUrl}/quantite-total-vendue`);
}

getChiffreAffairesParCategorie() {
  return this.http.get<any[]>(`${this.apiUrl}/chiffre-affaires-par-categorie`);
}

getAjoutsParMois() {
  return this.http.get<any[]>(`${this.apiUrl}/ajouts-par-mois`);
}




getArticlesStockFaible(): Observable<Article[]> {
  return this.http.get<Article[]>(`${this.apiUrl}/3-lowstock`);
}

 // Méthode pour récupérer les articles paginés
 getArticles(page: number, size: number): Observable<any> {
  const params = new HttpParams()
    .set('page', page.toString())
    .set('size', size.toString());

  return this.http.get<any>(`${this.apiUrl}/articles`, { params });
}
}
