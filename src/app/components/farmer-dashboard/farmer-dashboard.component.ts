import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Article, StatusAgri, TypeProduit } from 'src/app/models/article.model';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-farmer-dashboard',
  templateUrl: './farmer-dashboard.component.html',
  styleUrls: ['./farmer-dashboard.component.css']
})
export class FarmerDashboardComponent {
  nom: string = '';
  articles: Article[] = [];
  message: string = '';
  selectedStatus: StatusAgri | 'all' | null = null; // Ajout de 'all' pour gérer le filtre "Tous les statuts"
selectedTypeProduit: TypeProduit | 'all' | null = null; 
  idAgriculteur: number = 0;
  searchNom: string = '';
  totalItems: number = 0;
  currentPage: number = 0;
  pageSize: number = 10;

  constructor(
    private route: ActivatedRoute, 
    private articleService: ArticleService, 
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.idAgriculteur = +params['id'];
      this.loadArticles();
      
    });
  }

  loadArticles(): void {
    this.articleService.getArticlesParAgriculteurrr(this.idAgriculteur).subscribe(
      (data: Article[]) => {
        this.articles = data;
      },
      (error) => {
        console.error('Erreur lors de la récupération des articles:', error);
      }
    );
  }

  // Modifiez les méthodes de filtrage comme suit :
getFilteredNormalArticles(): Article[] {
  return this.articles.filter(article => {
    const isNotPack = !article.nom.toLowerCase().startsWith('pack');
    const matchesSearch = !this.searchNom || 
      article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
    const matchesStatus = this.selectedStatus === null || 
      article.statusAgri === this.selectedStatus.toString();
    const matchesType = this.selectedTypeProduit === null || 
      article.typeProduit === this.selectedTypeProduit.toString();
    
    return isNotPack && matchesSearch && matchesStatus && matchesType;
  });
}

getFilteredPackArticles(): Article[] {
  return this.articles.filter(article => {
    const isPack = article.nom.toLowerCase().startsWith('pack');
    const matchesSearch = !this.searchNom || 
      article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
    const matchesStatus = this.selectedStatus === null || 
      article.statusAgri === this.selectedStatus.toString();
    const matchesType = this.selectedTypeProduit === null || 
      article.typeProduit === this.selectedTypeProduit.toString();
    
    return isPack && matchesSearch && matchesStatus && matchesType;
  });
}

  // Méthodes utilitaires
  getStatusClass(status: StatusAgri): string {
    return status ? status.toString() : '';
  }

  getStatusDisplay(status: StatusAgri): string {
    switch(status) {
      case StatusAgri.InStock: return 'In Stock';
      case StatusAgri.LowStock: return 'Low Stock';
      case StatusAgri.OutOfStock: return 'Out of Stock';
      default: return '';
    }
  }

  getTypeDisplay(type: TypeProduit): string {
    switch(type) {
      case TypeProduit.fruits: return 'Fruits';
      case TypeProduit.legume: return 'Vegetables';
      case TypeProduit.cereale: return 'Cereals';
      case TypeProduit.produitLaitier: return 'Dairy Products';
      case TypeProduit.equipements: return 'Equipment';
      case TypeProduit.viande: return 'Meat';
      case TypeProduit.insec: return 'Insecticides';
      default: return '';
    }
  }

  // Méthodes de navigation
  onAddNew(): void {
    this.router.navigate([`/farmer/ajouter-article/${this.idAgriculteur}`]);
  }

  onEdit(articleId: number): void {
    this.router.navigate([`/farmer/edit-article/${articleId}`], { state: { idAgriculteur: this.idAgriculteur } });
  }

  onViewDetails(articleId: number): void {
    this.router.navigate([`/farmer/article-details/${articleId}`]);
  }

  onArchive(articleId: number): void {
    this.articleService.archiveArticle(articleId).subscribe(
      () => {
        this.loadArticles();
      },
      (error) => {
        console.error("Erreur lors de l'archivage:", error);
      }
    );
  }

  genererPack() {
    if (this.nom) {
      this.articleService.genererPack().subscribe(
        response => {
          this.message = `Pack généré avec succès pour ${this.nom}!`;
          this.loadArticles();
        },
        error => {
          this.message = `Erreur lors de la génération du pack: ${error.message}`;
        }
      );
    } else {
      this.message = "Veuillez entrer un nom d'agriculteur.";
    }
  }

  refreshArticles(): void {
    this.searchNom = '';
    this.selectedStatus = null;
    this.selectedTypeProduit = null;
    this.loadArticles();
  }
  applyFilters() {
    // Cette méthode vide est juste pour déclencher le changement
    // Le filtrage est géré par les getters
  }




  genererPackPourUtilisateur(): void {
    this.articleService.genererPackk(this.idAgriculteur).subscribe({
      next: (pack) => {
        console.log("✅ Pack généré :", pack);
        // Tu peux ajouter une notification ou actualiser la liste ici
      },
      error: (err) => {
        console.error("❌ Erreur lors de la génération du pack :", err);
      }
    });
  }
}
