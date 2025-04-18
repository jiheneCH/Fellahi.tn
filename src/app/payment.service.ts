import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { loadStripe, Stripe } from '@stripe/stripe-js';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  private stripePromise = loadStripe('pk_test_51REDLGERJZiSpbiZ8baTBXnN24HRhXMMJigC9PtWy4bf87szcLqaIeE5bkgrlcDhIQBqRycWvupxKHm1ROBnl20400Pg4b9jXz'); // 👉 Ta clé publique Stripe

  constructor(private http: HttpClient) {}

  // Créer un PaymentIntent
  createPaymentIntent(amount: number): Observable<any> {
    return this.http.post<any>('http://localhost:8080/panier/commande/create-payment-intent', { amount });
  }
  

  // Récupérer l'instance Stripe
  async getStripe(): Promise<Stripe | null> {
    return this.stripePromise;
  }
}
