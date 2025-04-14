import { Component } from '@angular/core';
import { Article } from 'src/app/models/article.model';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-tendance-article',
  templateUrl: './tendance-article.component.html',
  styleUrls: ['./tendance-article.component.css']
})
export class TendanceArticleComponent {
  articles: any[] = [];
  filteredArticles: any[] = [];
  loading = false;
  error: string | null = null;

  constructor(private articleService: ArticleService) { }

  ngOnInit(): void {
    this.loadArticles();
  }

  loadArticles(): void {
    this.loading = true;
    this.error = null;
    
    this.articleService.getTendanceArticles().subscribe({
      next: (articles) => {
        this.articles = articles;
        // Filtrer les articles archivés
        this.filteredArticles = articles.filter(article => !article.archived);
        this.loading = false;
      },
      error: (err) => {
        this.error = 'Erreur lors du chargement des articles';
        this.loading = false;
      }
    });
  }

  getStatusLabel(status: string): string {
    const statusLabels: {[key: string]: string} = {
      'DISPONIBLE': 'En stock',
      'RUPTURE': 'Rupture',
      'EN_COMMANDE': 'Pré-commande'
    };
    return statusLabels[status] || status;
  }
  
  getSalesPercentage(article: any): number {
    if (!this.filteredArticles.length) return 0;
    const totalSales = this.filteredArticles.reduce((sum, a) => sum + (a.quantiteVendue || 0), 0);
    return Math.round(((article.quantiteVendue || 0) / totalSales) * 100);
  }
 
}
