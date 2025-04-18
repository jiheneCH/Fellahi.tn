import { HttpClient, HttpParams } from '@angular/common/http';
import { Component, HostListener, OnInit } from '@angular/core';
import { ArticleService } from 'src/app/article.service';
import { Article } from 'src/app/models/article';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  articles: (Article & { quantity?: number })[] = [];
  loading = true;
  articleQRCodes: { [id: number]: string } = {};
  favoriteStatus: { [id: number]: boolean } = {};
  counters: { [id: number]: { value: number, target: number } } = {};
  quantityInputs: { [id: number]: HTMLInputElement | null } = {};
  clientName: string = '';
  clientId: number | null = null;

  constructor(private httpClient: HttpClient, private articleService: ArticleService) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.articleService.getAllArticles().subscribe({
      next: (articles) => {
        this.articles = articles.map(article => ({
          ...article,
          quantity: 1
        }));

        this.articles.forEach(article => {
          if (article.idArticle) {
            this.counters[article.idArticle] = { value: 0, target: 1 };
          }
        });

        this.generateAllQRCodes(this.articles);
        this.loading = false;
        setTimeout(() => this.initScrollAnimations(), 100);
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.loading = false;
      }
    });
  }

  generateAllQRCodes(articles: Article[]): void {
    articles.forEach(article => {
      if (article.idArticle) {
        this.articleService.getArticleQrCode(article.idArticle).subscribe({
          next: (blob: Blob) => {
            this.createImageFromBlob(blob, article.idArticle!);
          },
          error: (err) => {
            console.error(`Erreur QR Code pour l'article ${article.idArticle}:`, err);
          }
        });
      }
    });
  }

  private createImageFromBlob(blob: Blob, articleId: number): void {
    const reader = new FileReader();
    reader.onload = () => {
      this.articleQRCodes[articleId] = reader.result as string;
    };
    reader.readAsDataURL(blob);
  }

  increaseQuantity(product: any): void {
    product.quantity = (product.quantity || 1) + 1;
    this.updateQuantity(product.idArticle!, product.quantity);
  }

  decreaseQuantity(product: any): void {
    if (product.quantity > 1) {
      product.quantity--;
      this.updateQuantity(product.idArticle!, product.quantity);
    }
  }

  fetchClientId(): void {
    if (!this.clientName.trim()) {
      console.warn("Nom du client vide");
      return;
    }

    this.httpClient.get<number>('http://localhost:8080/panier/commande/client/id', {
      params: { name: this.clientName }
    }).subscribe({
      next: (id) => {
        this.clientId = id;
        localStorage.setItem('clientId', id.toString());
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

    const idArticle = product.idArticle;
    const quantite = product.quantity ?? 1;

    const params = new HttpParams()
      .set('idClient', clientId.toString())
      .set('idArticle', idArticle.toString())
      .set('quantite', quantite.toString());

    this.httpClient.post('http://localhost:8080/panier/commande/addArticleToCommande', null, { params })
      .subscribe({
        next: () => {
          console.log(`Produit ${product.nom} ajouté au panier avec succès !`);
        },
        error: (err) => {
          console.error('Erreur lors de l\'ajout au panier :', err);
        }
      });
  }

  updateQuantity(articleId: number, value: number): void {
    if (!this.counters[articleId]) return;
    this.counters[articleId].target = Math.max(1, value);
    this.animateCounter(articleId);
  }

  animateCounter(articleId: number): void {
    const counter = this.counters[articleId];
    const duration = 500;
    const startTime = performance.now();
    const startValue = counter.value;
    const change = counter.target - startValue;

    const animate = (currentTime: number) => {
      const elapsedTime = currentTime - startTime;
      const progress = Math.min(elapsedTime / duration, 1);
      counter.value = Math.floor(startValue + change * progress);

      if (progress < 1) {
        requestAnimationFrame(animate);
      }
    };

    requestAnimationFrame(animate);
  }

  initScrollAnimations(): void {
    this.checkScroll();
  }

  @HostListener('window:scroll', ['$event'])
  checkScroll(): void {
    const elements = document.querySelectorAll('.reveal');
    elements.forEach(element => {
      const elementTop = element.getBoundingClientRect().top;
      const windowHeight = window.innerHeight;

      if (elementTop < windowHeight - 100) {
        element.classList.add('active');
      }
    });
  }

  // Nouvelle méthode pour gérer le favori
  toggleFavorite(idArticle: number): void {
    this.favoriteStatus[idArticle] = !this.favoriteStatus[idArticle];
  }
}
