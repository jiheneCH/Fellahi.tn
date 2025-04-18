import { Component, OnInit } from '@angular/core';
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';
import { PaymentService } from 'src/app/payment.service';
import { CheckoutService } from 'src/app/checkout.service';

@Component({
  selector: 'app-online',
  templateUrl: './online.component.html',
  styleUrls: ['./online.component.css']
})
export class OnlineComponent implements OnInit {
  client: any = {
    id: 0,
    name: '',
    email: '',
    address: '',
    phone: ''
  };

  commande: any;
  montant: number = 0;

  stripe: Stripe | null = null;
  elements: StripeElements | null = null;
  card: StripeCardElement | null = null;

  private stripePromise = loadStripe('pk_test_51REDLGERJZiSpbiZ8baTBXnN24HRhXMMJigC9PtWy4bf87szcLqaIeE5bkgrlcDhIQBqRycWvupxKHm1ROBnl20400Pg4b9jXz');

  constructor(
    private checkoutService: CheckoutService,
    private paymentService: PaymentService
  ) {}

  async ngOnInit(): Promise<void> {
    this.stripe = await this.stripePromise;
    if (this.stripe) {
      this.elements = this.stripe.elements();
      this.card = this.elements.create('card');
      this.card.mount('#card-element');
    }

    const storedClientId = localStorage.getItem('clientId');
    const clientId = storedClientId ? parseInt(storedClientId, 10) : null;

    if (clientId) {
      this.checkoutService.getClientById(clientId).subscribe({
        next: (data) => {
          this.client = data;
        },
        error: (err) => console.error('Erreur lors de la récupération du client :', err)
      });

      this.checkoutService.getCommandeEnCours(clientId).subscribe({
        next: (data) => {
          console.log('Commande récupérée :', data); // Debug
          this.commande = data;
          this.montant = data?.prixTotalCommande || 0;
          console.log('Montant détecté :', this.montant); // Debug
        },
        error: (err) => console.error('Erreur lors de la récupération de la commande :', err)
      });
    } else {
      console.warn('Aucun clientId trouvé dans le localStorage');
    }
  }

  async payerEnLigne() {
    if (!this.stripe || !this.card) {
      console.error('Stripe ou la carte n\'est pas prête.');
      return;
    }
  
    if (!this.commande || !this.commande.idCommande) {
      console.error('La commande n\'est pas encore chargée.');
      alert('Invalid Command');
      return;
    }
  
    try {
      this.checkoutService.validerCommandeOnline(this.commande.idCommande).subscribe({
        next: (response) => {
          alert('✅ Paiement réussi et commande validée !');
        },
        error: (err) => {
          console.error('Erreur pendant la validation de la commande :', err);
        }
      });
    } catch (error) {
      console.error('Erreur pendant le paiement :', error);
    }
  }
}

