import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Article, Status, TypeProduit } from 'src/app/models/article.model';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-farmer-dashboard',
  templateUrl: './farmer-dashboard.component.html',
  styleUrls: ['./farmer-dashboard.component.css']
})
export class FarmerDashboardComponent {
  nom: string = '';
  articles: any[] = [];
  message: string = '';         // Message pour afficher le résultat de l'API
  selectedTypeProduit: TypeProduit | null = null;
  selectedStatus: Status | null = null;
  idAgriculteur: number = 0;  // ID de l'agriculteur


  constructor(private route: ActivatedRoute, private articleService: ArticleService, private router: Router) {}

  ngOnInit(): void {
    this.route.params.subscribe(params => {
      this.idAgriculteur = +params['id'];  // Assure-toi que l'ID est un nombre
      this.loadArticles();  // Charger les articles de cet agriculteur
    });
  }

// Méthode pour charger les articles de l'agriculteur
loadArticles(): void {
  this.articleService.getArticlesParAgriculteurrr(this.idAgriculteur).subscribe(
    (data: Article[]) => {
      this.articles = data;  // Assigner les articles récupérés à la variable
    },
    (error) => {
      console.error('Erreur lors de la récupération des articles:', error);
    }
  );
}





  onArchive(articleId: number): void {
    this.articleService.archiveArticle(articleId).subscribe(
      () => {
        this.loadArticles(); // Recharge les données après archivage
      },
      (error) => {
        console.error("Erreur lors de l'archivage:", error);
      }
    );
  }
  onEdit(articleId: number): void {
    this.router.navigate([`/edit-article/${articleId}`]);
  }
 
  onViewDetails(articleId: number): void {
    this.router.navigate([`/article-details/${articleId}`]);
  }
  onAddNew(): void {
    this.router.navigate([`/ajouter-article/${this.idAgriculteur}`]);
  }
 // Méthode pour appeler l'API de génération de pack
 genererPack() {
  if (this.nom) {
    this.articleService.genererPackParNomAgriculteur(this.nom).subscribe(
      response => {
        // Ici, tu peux afficher un message de succès ou d'erreur
        this.message = `Pack généré avec succès pour ${this.nom}!`;
      },
      error => {
        // Gérer l'erreur en cas de problème
        this.message = `Erreur lors de la génération du pack: ${error.message}`;
      }
    );
  } else {
    this.message = "Veuillez entrer un nom d'agriculteur.";
  }
}
getStatusClass(status: string): string {
  switch (status) {
    case 'InStock': return 'status-instock';
    case 'LowStock': return 'status-lowstock';
    case 'OutOfStock': return 'status-outofstock';
    default: return '';
  }
} 
searchNom: string = '';

getFilteredArticles(): Article[] {
  return this.articles.filter(article => {
    const matchesNom = !this.searchNom || article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
    const matchesStatus = !this.selectedStatus || article.status === this.selectedStatus;
    const matchesType = !this.selectedTypeProduit || article.typeProduit === this.selectedTypeProduit;
    return matchesNom && matchesStatus && matchesType;
  }); 
}
 // Rafraîchit les données et réinitialise les filtres
 refreshArticles(): void {
  this.searchNom = '';
  this.selectedStatus = null;
  this.selectedTypeProduit = null;
  this.loadArticles();}
  getFilteredPacks(): Article[] {
    return this.articles.filter(article => 
      article.nom.toLowerCase().startsWith('pack') &&
      (!this.searchNom || article.nom.toLowerCase().includes(this.searchNom.toLowerCase())) &&
      (!this.selectedStatus || article.status === this.selectedStatus) &&
      (!this.selectedTypeProduit || article.typeProduit === this.selectedTypeProduit)
    );
  }
  
  getFilteredArticlesSansPack(): Article[] {
    return this.articles.filter(article => 
      !article.nom.toLowerCase().startsWith('pack') &&
      (!this.searchNom || article.nom.toLowerCase().includes(this.searchNom.toLowerCase())) &&
      (!this.selectedStatus || article.status === this.selectedStatus) &&
      (!this.selectedTypeProduit || article.typeProduit === this.selectedTypeProduit)
    );
  }
  getFilteredNormalArticles(): Article[] {
    return this.articles.filter(article => {
      const isNotPack = !article.nom.toLowerCase().startsWith('pack');
      const matchesNom = !this.searchNom || article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
      const matchesStatus = !this.selectedStatus || article.status === this.selectedStatus;
      const matchesType = !this.selectedTypeProduit || article.typeProduit === this.selectedTypeProduit;
      return isNotPack && matchesNom && matchesStatus && matchesType;
    });
  }
  
  getFilteredPackArticles(): Article[] {
    return this.articles.filter(article => {
      const isPack = article.nom.toLowerCase().startsWith('pack');
      const matchesNom = !this.searchNom || article.nom.toLowerCase().includes(this.searchNom.toLowerCase());
      const matchesStatus = !this.selectedStatus || article.status === this.selectedStatus;
      const matchesType = !this.selectedTypeProduit || article.typeProduit === this.selectedTypeProduit;
      return isPack && matchesNom && matchesStatus && matchesType;
    });
  }
    
}
