import { Component, HostListener, OnInit } from '@angular/core';
import { Article } from 'src/app/models/article.model';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-shop',
  templateUrl: './shop.component.html',
  styleUrls: ['./shop.component.css']
})
export class ShopComponent implements OnInit {
  articles: Article[] = [];
  loading = true;
  articleQRCodes: {[id: number]: string} = {};
  favoriteStatus: {[id: number]: boolean} = {};

  // Pour l'animation de compteur
  counters: {[id: number]: {value: number, target: number}} = {};
  quantityInputs: {[id: number]: HTMLInputElement | null} = {};

  constructor(
    private articleService: ArticleService,
  ) {}

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.articleService.getAllArticles().subscribe({
      next: (articles) => {
        this.articles = articles;
        articles.forEach(article => {
          if (article.idArticle) {
            this.counters[article.idArticle] = {value: 0, target: 0};
          }
        });
        this.generateAllQRCodes(articles);
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

  addToCart(article: Article): void {
    if (!article.idArticle) return;
    
    const quantity = this.counters[article.idArticle]?.target || 1;
    // Implémentez ici la logique pour ajouter au panier
    console.log(`Ajout de ${quantity} ${article.nom} au panier`);
  }

  // Animation de compteur
  updateQuantity(articleId: number, value: number): void {
    if (!this.counters[articleId]) return;
    
    this.counters[articleId].target = Math.max(0, value);
    this.animateCounter(articleId);
  }

  animateCounter(articleId: number): void {
    if (!this.counters[articleId]) return;
    
    const counter = this.counters[articleId];
    const duration = 500; // ms
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

  // Animation au scroll
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
}