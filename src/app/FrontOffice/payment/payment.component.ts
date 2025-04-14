import { HttpClient } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { loadStripe } from '@stripe/stripe-js';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit{
  stripePromise = loadStripe('pk_test_XXXXXXXXXXXXXXXXXXXX'); // clé publique
  clientSecret: string = '';

  constructor(private http: HttpClient) {}

  async ngOnInit() {
    const paymentIntentData = {
      amount: 2000, // en cents (20.00 USD)
      currency: 'usd',
      payment_method_types: ['card']
    };

    this.http.post<any>('http://localhost:8080/api/payment/create-payment-intent', paymentIntentData)
      .subscribe(async (response) => {
        this.clientSecret = response.clientSecret;
        const stripe = await this.stripePromise;
        const elements = stripe!.elements();

        const cardElement = elements.create('card');
        cardElement.mount('#card-element');

        const form = document.getElementById('payment-form');
        form?.addEventListener('submit', async (event) => {
          event.preventDefault();
          const { paymentIntent, error } = await stripe!.confirmCardPayment(this.clientSecret, {
            payment_method: {
              card: cardElement,
              billing_details: {
                name: 'Test User',
              },
            }
          });

          if (error) {
            alert(error.message);
          } else if (paymentIntent?.status === 'succeeded') {
            alert('Paiement réussi ! 🎉');
          }
        });
      });
  }

}
