import { Component } from '@angular/core';
import { ArticleService } from 'src/app/service/article.service';
import { Article, Status, TypeProduit } from '../../models/article.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-article-list',
  templateUrl: './article-list.component.html',
  styleUrls: ['./article-list.component.css']
})
export class ArticleListComponent {
  articles: Article[] = [];
  filteredArticles: Article[] = [];
  statusEnum = Status;
  packs: Article[] = [];
  nonPacks: Article[] = [];
  searchNom: string = '';
  selectedStatus: Status | null = null;
  selectedTypeProduit: TypeProduit | null = null;
  
  statusList: Status[] = Object.values(Status);
  typeProduitList: TypeProduit[] = Object.values(TypeProduit);

  constructor(private articleService: ArticleService, private router: Router) {}
  
  ngOnInit(): void {
    this.loadData();
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
      
      // Filtre par statut (prend en compte 'Tous les statuts')
      const matchesStatus = this.selectedStatus === null || 
        article.status === this.selectedStatus;
      
      // Filtre par type (prend en compte 'Tous les types')
      const matchesType = this.selectedTypeProduit === null || 
        article.typeProduit === this.selectedTypeProduit;
      
      return matchesSearch && matchesStatus && matchesType;
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
    this.router.navigate([`/edit-article/${articleId}`]);
  }

  onAddNew(): void {
    this.router.navigate(['/articles/create']);
  }

  onViewDetails(articleId: number): void {
    this.router.navigate([`/article-details/${articleId}`]);
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
      case 'InStock': return 'status-instock';
      case 'LowStock': return 'status-lowstock';
      case 'OutOfStock': return 'status-outofstock';
      default: return '';
    }
  }

  getTypeDisplay(type: TypeProduit): string {
    switch(type) {
      case TypeProduit.legume: return 'legume';
      case TypeProduit.fruit: return 'fruit';
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

  
}