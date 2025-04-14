import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, OnInit } from '@angular/core';
import { PanierService } from 'src/app/panier.service';
import { ShopService } from 'src/app/shop.service';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  products = [
    {
      id: 1,
      name: 'Fresh Apple',
      description: 'Delicious red apples.',
      price: 3.50,
      image: 'src/assets/images/Apple.webp',
      quantity: 1
    },
    {
      id: 2,
      name: 'Banana',
      description: 'Ripe and sweet bananas.',
      price: 2.00,
      image: 'src/assets/images/favicon.png',
      quantity: 1
    }
  ];

  clientName: string = '';
  clientId: number | null = null;

  constructor(private httpClient: HttpClient) {}

  ngOnInit(): void {}

  increaseQuantity(product: any) {
    product.quantity++;
  }

  decreaseQuantity(product: any) {
    if (product.quantity > 1) {
      product.quantity--;
    }
  }

 
  fetchClientId() {
    if (!this.clientName.trim()) {
      console.warn("Nom du client vide");
      return;
    }

    this.httpClient.get<number>('http://localhost:8080/panier/commande/client/id', {
      params: { name: this.clientName }
    }).subscribe({
      next: (id) => {
        this.clientId = id;
        localStorage.setItem('clientId', id.toString()); // 🧠 stock clientId
        console.log(`ID récupéré pour ${this.clientName} : ${id}`);
      },
      error: (err) => {
        console.error('Erreur lors de la récupération de l’ID :', err);
        this.clientId = null;
      }
    });
  }

  addToCart(product: any): void {
    const storedId = localStorage.getItem('clientId');
    const clientId = storedId ? parseInt(storedId, 10) : null;

    if (clientId === null) {
      console.warn("Veuillez d'abord chercher le client.");
      return;
    }

    const params = new HttpParams()
      .set('idClient', clientId)
      .set('idArticle', product.id)
      .set('quantite', product.quantity);

    this.httpClient.post('http://localhost:8080/panier/commande/addArticleToCommande', null, { params })
      .subscribe({
        next: () => {
          console.log(`Produit ${product.name} ajouté au panier avec succès !`);
        },
        error: (err) => {
          console.error('Erreur lors de l\'ajout au panier :', err);
        }
      });
  }
}
