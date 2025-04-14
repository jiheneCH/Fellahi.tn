import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
export interface Client {
  id: number;
  name: string;
  email: string;
  address: string;
  phone: string;
}
@Injectable({
  providedIn: 'root'
})
export class CheckoutService {
  processPayment(paymentData: { commandeId: any; paymentMethod: "onSite" | "online"; clientId: any; }) {
    throw new Error('Method not implemented.');
  }

  private apiUrl = 'http://localhost:8080/panier/commande';  // Assurez-vous que l'URL est correcte

  constructor(private http: HttpClient) {}
 // Récupérer la commande en cours du client
 getCommandeEnCours(clientId: number): Observable<any> {
  return this.http.get(`${this.apiUrl}/${clientId}/en-cours`);
}
  getClientById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }
  validerCommandeOnSite(commandeId: number): Observable<any> {
    const url = `${this.apiUrl}/valider-onsite/${commandeId}`;
    return this.http.put(url, null, { responseType: 'text' }); // ou 'json' selon ton retour
  }
  createPaymentIntent(data: any) {
    return this.http.post<any>(`${this.apiUrl}/create-payment-intent`, data);
  }
  
  validerCommandeOnline(commandeId: number) {
    return this.http.put(`${this.apiUrl}/valider-online/${commandeId}`, {});
  }
  
  
}
