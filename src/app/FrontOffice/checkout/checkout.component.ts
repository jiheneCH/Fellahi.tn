import { Component, OnInit, OnDestroy } from '@angular/core';
import { CheckoutService } from 'src/app/checkout.service';
import { Router } from '@angular/router'; // ✅ Ajout
import { loadStripe, Stripe, StripeElements, StripeCardElement } from '@stripe/stripe-js';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.css']
})
export class CheckoutComponent implements OnInit, OnDestroy {
  client: any = {
    id: 0,
    name: '',
    email: '',
    address: '',
    phone: ''
  };
  commande: any;

  selectedPaymentMethod: string = 'onSite'; // ✅ Ajout d'une variable de sélection de méthode

  constructor(
    private checkoutService: CheckoutService,
    private router: Router // ✅ Injection du router
  ) {}

  ngOnInit(): void {
    const storedClientId = localStorage.getItem('clientId');
    const clientId = storedClientId ? parseInt(storedClientId, 10) : null;

    if (clientId) {
      this.checkoutService.getClientById(clientId).subscribe({
        next: (data) => {
          this.client = data;
        },
        error: (err) => console.error('Erreur client:', err)
      });

      this.checkoutService.getCommandeEnCours(clientId).subscribe({
        next: (data) => {
          this.commande = data;
        },
        error: (err) => console.error('Erreur commande:', err)
      });
    } else {
      console.warn('Client non trouvé dans le localStorage');
    }
  }

  ngOnDestroy(): void {
    // Aucun nettoyage nécessaire maintenant
  }

  placeOrder(): void {
    if (this.selectedPaymentMethod === 'online') {
      // ✅ Redirection vers le composant "online" avec des données
      this.router.navigate(['/online'], {
        queryParams: {
          idCommande: this.commande?.idCommande,
          total: this.commande?.prixTotalCommande
        }
      });
    } else {
      this.confirmOnSitePayment();
    }
  }

  private confirmOnSitePayment(): void {
    if (!this.commande?.idCommande) {
      console.error('Commande introuvable ou ID manquant.');
      return;
    }

    this.checkoutService.validerCommandeOnSite(this.commande.idCommande).subscribe({
      next: () => {
        alert("Commande validée avec succès en mode 'onSite' !");
      },
      error: (err) => {
        console.error('Erreur lors de la validation de la commande :', err);
        alert("Erreur lors de la validation de la commande.");
      }
    });
  }
}
