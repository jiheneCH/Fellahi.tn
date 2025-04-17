import { Component } from '@angular/core';
import { ArticleService } from 'src/app/service/article.service';
import { Article,  StatusAgri,  TypeProduit } from 'src/app/models/article.model';
import { Router } from '@angular/router';


@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent {
  articles: Article[] = [];
  filteredArticles: Article[] = [];
  statusEnum = StatusAgri; // Utilisation de StatusAgri
  packs: Article[] = [];
  nonPacks: Article[] = [];
  searchNom: string = '';
  selectedStatus: StatusAgri | 'all' | null = null; // Ajout de 'all' pour gérer le filtre "Tous les statuts"
selectedTypeProduit: TypeProduit | 'all' | null = null; // Ajout de 'all' pour gérer le filtre "Toutes les catégories"
selectedDate: Date | null = null; // Ou bien une plage de dates, si nécessaire

  
  statusList: StatusAgri[] = Object.values(StatusAgri); // Utilisation de StatusAgri
  typeProduitList: TypeProduit[] = Object.values(TypeProduit);

  constructor(private articleService: ArticleService, private router: Router) {}
  
  ngOnInit(): void {
    this.loadData();
    this.verifierArticlesCritiques();
  setInterval(() => {
    this.verifierArticlesCritiques();
  }, 60000);
  }

  // Méthode principale de chargement des données
  loadData(): void {
    this.getAllArticles();
  }

  // Charge tous les articles et met à jour les listes
  getAllArticles(): void {
    this.articleService.getAllArticles().subscribe(
      (data) => {
        this.processArticleData(data);
      },
      (error) => {
        console.error('Erreur lors du chargement des articles :', error);
      }
    );
  }

  // Traite les données reçues et met à jour les listes
  processArticleData(articles: Article[]): void {
    this.articles = articles.filter(article => !article.archived);
    this.updateFilteredLists();
    console.log('Données mises à jour:', {
      articles: this.articles,
      packs: this.packs,
      nonPacks: this.nonPacks
    });
  }

  // Met à jour les listes filtrées
  updateFilteredLists(): void {
    this.packs = this.articles.filter(article => article.nom.startsWith('Pack-'));
    this.nonPacks = this.articles.filter(article => !article.nom.startsWith('Pack-'));
    this.applyFilter();
  }

  // Gestion des filtres
  applyFilter(): void {
    this.filteredArticles = this.articles.filter(article => {
      // Filtre par nom
      const matchesSearch = !this.searchNom || 
        article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
      
        const matchesStatus = this.selectedStatus === null || this.selectedStatus === 'all' || 
        article.statusAgri === this.selectedStatus;
  
      
    // Filtre par type (prend en compte 'Toutes les catégories')
    const matchesType = this.selectedTypeProduit === null || this.selectedTypeProduit === 'all' || 
      article.typeProduit === this.selectedTypeProduit;
      const matchesDate = this.selectedDate === null || new Date(article.dateAjout).toDateString() === this.selectedDate?.toDateString();

      
      return matchesSearch && matchesStatus && matchesType && matchesDate;
    });
  
    // Mise à jour des packs et non-packs après filtrage
    this.packs = this.filteredArticles.filter(a => a.nom.startsWith('Pack-'));
    this.nonPacks = this.filteredArticles.filter(a => !a.nom.startsWith('Pack-'));
  }

  // Rafraîchit les données et réinitialise les filtres
  refreshArticles(): void {
    this.searchNom = '';
    this.selectedStatus = null;
    this.selectedTypeProduit = null;
    this.loadData();
  }

  // Navigation
  onEdit(articleId: number): void {
    this.router.navigate([`/farmer/edit-article/${articleId}`]);
  }

  onAddNew(): void {
    this.router.navigate(['/farmer/articles/create']);
  }

  onViewDetails(articleId: number): void {
    this.router.navigate([`/farmer/article-details/${articleId}`]);
  }

  // Gestion des articles
  onArchive(articleId: number): void {
    this.articleService.archiveArticle(articleId).subscribe(
      () => {
        this.loadData(); // Recharge les données après archivage
      },
      (error) => {
        console.error("Erreur lors de l'archivage:", error);
      }
    );
  }

  genererPack(): void {
    this.articleService.genererPack().subscribe(
      () => {
        this.loadData(); // Recharge les données après génération
      },
      (error) => {
        console.error('Erreur lors de la génération:', error);
      }
    );
  }

  // Utilitaires d'affichage
  getStatusClass(status: string): string {
    switch (status) {
      case 'InStock': return 'InStock';
      case 'LowStock': return 'LowStock';
      case 'OutOfStock': return 'OutOfStock';
      default: return '';
    }
  }

  getTypeDisplay(type: TypeProduit): string {
    switch(type) {
      case TypeProduit.legume: return 'legume';
      case TypeProduit.fruits: return 'fruits';
      case TypeProduit.cereale: return 'cereale';
      case TypeProduit.produitLaitier: return 'produitLaitier';
      case TypeProduit.equipements: return 'equipements';
      case TypeProduit.viande: return 'viande';
      case TypeProduit.insec: return 'insec';

      default: return type;
    }
  }

  // Gestion des listes filtrées
  getFilteredPacks(): Article[] {
    return this.filteredArticles.filter(article => article.nom.startsWith('Pack-'));
  }

  getFilteredNonPacks(): Article[] {
    return this.filteredArticles.filter(article => !article.nom.startsWith('Pack-'));
  }

  // Gestion des changements de recherche
  onSearchChange(): void {
    this.applyFilter();
  }



  articlesStockFaible: Article[] = [];
  verifierArticlesCritiques(): void {
    this.articleService.getArticlesStockFaible().subscribe(
      (data) => {
        this.articlesStockFaible = data;
      },
      (error) => {
        console.error("Erreur lors de la récupération des articles à stock faible :", error);
      }
    );
  }


  // Pour la sidebar
showSidebar = false;
scrollPaused = false;

// Ouvre/Ferme la sidebar
toggleSidebar(): void {
  this.showSidebar = !this.showSidebar;
}

// Met en pause l'animation quand souris dessus
pauseScroll(): void {
  this.scrollPaused = true;
}

resumeScroll(): void {
  this.scrollPaused = false;
}

// Redirige vers la page complète des alertes
onViewAllAlerts(): void {
  this.router.navigate(['/farmer/articles/alerts']);
}





}
