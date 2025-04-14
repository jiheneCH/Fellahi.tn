import { Component, OnInit } from '@angular/core';
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';
import { OnlineService } from 'src/app/online.service';
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

  // Définir l'objet payment avec les propriétés appropriées
  payment: any = {
    cardNumber: '',
    expiry: '', // Ajouter cette propriété
    cvv: ''     // Ajouter cette propriété
  };

  stripe: Stripe | null = null;
  elements: StripeElements | null = null;
  card: StripeCardElement | null = null;

  private stripePromise = loadStripe('pk_test_51RDRTFFQnlvscCidLdrzyinp5tiMvFyUsUCFNkTacb586pLnCPucDZzNgViW6hMXjiujXpNw0dM96Ny57x5o5GeS00B4Ag8lSr');

  constructor(private checkoutService: CheckoutService ,
    private paymentService: PaymentService
  ) {}

  async ngOnInit(): Promise<void> {
    this.stripe = await this.stripePromise;
    if (this.stripe) {
      this.elements = this.stripe.elements();
      this.card = this.elements.create('card');
      this.card.mount('#card-element'); // Monte le champ de carte dans le DOM
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
          this.commande = data;
          this.montant = data?.prixTotalCommande || 0;
        },
        error: (err) => console.error('Erreur lors de la récupération de la commande :', err)
      });
    } else {
      console.warn('Aucun clientId trouvé dans le localStorage');
    }
  }

  async payerEnLigne() {
    if (!this.stripe || !this.elements || !this.card) {
      console.error('Stripe ou l\'élément de carte n\'est pas chargé');
      return;
    }
  
    try {
      // Appel à createPaymentIntent et attente de la réponse
      const response = await this.paymentService.createPaymentIntent(this.montant).toPromise();
      
      // Vérifie si le clientSecret existe
      const clientSecret = response.clientSecret; // Récupère clientSecret dans la réponse
  
      if (!clientSecret) {
        console.error('Le client_secret est manquant dans la réponse');
        return;
      }
  
      const { error, paymentMethod } = await this.stripe.createPaymentMethod({
        type: 'card',
        card: this.card,
      });
  
      if (error) {
        console.error('Erreur de paiement:', error);
      } else {
        // Confirmation du paiement
        const paymentIntentConfirm = await this.stripe.confirmCardPayment(clientSecret, {
          payment_method: paymentMethod.id,
        });
  
        if (paymentIntentConfirm.error) {
          console.error('Erreur de confirmation de paiement:', paymentIntentConfirm.error);
        } else {
          if (paymentIntentConfirm.paymentIntent?.status === 'succeeded') {
            this.checkoutService.validerCommandeOnline(this.commande.idCommande).subscribe(() => {
              alert('✅ Paiement réussi et commande validée !');
            });
          } else {
            console.warn('Paiement non abouti:', paymentIntentConfirm.paymentIntent?.status);
          }
        }
      }
    } catch (error) {
      console.error('Erreur pendant le processus de paiement:', error);
    }
  }
  
}

