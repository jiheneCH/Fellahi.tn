import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ArticleService } from 'src/app/service/article.service';

@Component({
  selector: 'app-article-details',
  templateUrl: './article-details.component.html',
  styleUrls: ['./article-details.component.css']
})
export class ArticleDetailsComponent {
  article: any;
  image: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private articleService: ArticleService,
    private router: Router
  ) {}

  ngOnInit(): void {
    const articleId = Number(this.route.snapshot.paramMap.get('id'));

    if (articleId) {
      this.articleService.getArticleById(articleId).subscribe((articleData) => {
        this.article = articleData;
        this.image = this.article?.image;
        console.log('Article reçu :', this.article); // Debug
      });
    }
  }
 

  // Retour à la liste
  goToList() {
    this.router.navigate(['/articles']);
  }
}
