import { Component } from '@angular/core';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-statistiques-admin-farmer',
  templateUrl: './statistiques-admin-farmer.component.html',
  styleUrls: ['./statistiques-admin-farmer.component.css']
})
export class StatistiquesAdminFarmerComponent {
  chiffreAffairesTotal: number = 0;
  quantiteTotaleVendue: number = 0;
  chiffreAffairesArticles: any = { articles: [], totalChiffreAffaires: 0 };
  ajoutsParJour: any[] = [];
  chiffreAffairesParCategorie: any[] = [];
  ajoutsParMois: any[] = [];

  constructor(private articleService: ArticleService) {}

  ngOnInit(): void {
    this.articleService.getChiffreAffairesTotal().subscribe(data => {
      this.chiffreAffairesTotal = data;
    });

    this.articleService.getQuantiteTotalVendue().subscribe(data => {
      this.quantiteTotaleVendue = data;
    });

    this.articleService.getChiffreAffairesParArticle().subscribe(data => {
      // Ici, nous récupérons la liste des articles et leur chiffre d'affaires
      this.chiffreAffairesArticles = data['articles'].map((item: { article: { nom: any; }; chiffreAffaires: any; }) => {
        return {
          nom: item.article.nom,
          chiffreAffaires: item.chiffreAffaires
        };
      });
    });
    this.articleService.getAjoutsParJour().subscribe(data => {
      this.ajoutsParJour = data;
    });

    this.articleService.getChiffreAffairesParCategorie().subscribe(data => {
      this.chiffreAffairesParCategorie = data;
    });

    this.articleService.getAjoutsParMois().subscribe(data => {
      this.ajoutsParMois = data;
    });
  }
}
