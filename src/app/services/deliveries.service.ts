import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Delivery } from './deliveries.model'; 
@Injectable({
  providedIn: 'root'
})
export class DeliveriesService {
  private apiUrl = 'http://localhost:8080/PIDistribution/livraisons';

  constructor(private http: HttpClient) {}

  getLivraisons(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
  getLivraisonById(id: number): Observable<any> {
    return this.http.get(`http://localhost:8080/PIDistribution/livraisons/${id}`);
  }

  getLivraisonByRef(ref: String): Observable<any> {
    return this.http.get(`http://localhost:8080/PIDistribution/livraisons/ref/${ref}`);
  }
  openInGoogleMaps(adresse: string): void {
    const query = encodeURIComponent(adresse); // encodage pour les espaces, accents...
    window.open(`https://www.google.com/maps/search/?api=1&query=${query}`, '_blank');
  }
  
  getDeliveryDetails(id: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/${id}`);
  }
  // Method to cancel a delivery
  annulerLivraison(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/annuler/${id}`, {});
  }
  archiverLivraison(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/archiver/${id}`, {});
  }
  changerLivraison(id: number): Observable<any> {
    return this.http.put(`${this.apiUrl}/changer/${id}`, {});
  }
  searchByTelephone(telephone: String): Observable<any[]> {
    return this.http.post<any[]>(`${this.apiUrl}/search-by-telephone`, telephone, {
      headers: { 'Content-Type': 'application/json' }
    });
  }
  
  getLivraisonsByStatut(statut: string): Observable<any[]> {
    if (statut === 'TOUS') {
      return this.getLivraisons();
    }
    return this.http.get<any[]>(`${this.apiUrl}/byStatut?statut=${statut}`);
  }

  getLivraisonsByDelegation(delegation: string): Observable<any[]> {
    if (delegation === 'TOUS') {
      return this.getLivraisons();
    }
    return this.http.get<any[]>(`${this.apiUrl}/byDelegation?delegation=${delegation}`);
  }
  getLivraisonsParDate(startDate: string, endDate: string): Observable<any[]> {
    return this.http.get<any[]>(
      `${this.apiUrl}/livraisonEntreDate?startDate=${startDate}&endDate=${endDate}`
    );
  }
 
  getLivraisonsByTransporteur(transporteurId: number): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/transporteur/${transporteurId}`);
  }
  updateDelivery(id: number, data: any): Observable<any> {
    return this.http.put(`http://localhost:8080/PIDistribution/livraisons/update/${id}`, data);
  }
  
  reaffecterLivraison(id: number) {
    const url = `http://localhost:8080/PIDistribution/livraisons/reaffecter/${id}`;
    return this.http.put(url, null); // Pas de corps (body), donc on passe null
  }
  private baseUrl = 'http://localhost:8080/PIDistribution/livraisons/statut';

  getLivraisonsByTransporteurEtStatut(id: number): Observable<any> {
    const url = `http://localhost:8080/PIDistribution/livraisons/statutTransporteur/${id}`;
    return this.http.get(url);
  }
  

  getDeliveryStats(): Observable<any> {
    return this.http.get(this.baseUrl);
  }
  getStatsByDelegation(delegation: string): Observable<any> {
    return this.http.get(`${this.apiUrl}/delegation?delegation=${delegation}`);
  }
  signalerIncident(id: number, typeIncident: string, descriptionIncident: string) {
    const body = {
      typeIncident,
      descriptionIncident
    };
    return this.http.post(`${this.apiUrl}/${id}/incident`, body, { responseType: 'text' });
  }
  getLivraisonsByTransporteurId(id: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/PIDistribution/livraisons/transporteur/${id}`);
  }
  private Url = 'http://localhost:8080/PIDistribution/livraisons/transporteur/';
  // Méthode pour récupérer les statistiques de livraison par transporteur
  getDeliveryStatsTransporteur(id: number): Observable<any> {
    return this.http.get(`${this.Url}${id}/statut`);
  }
  findByClientId(id: number): Observable<any[]> {
    return this.http.get<any[]>(`http://localhost:8080/PIDistribution/livraisons/client/${id}`);
  }
}

