import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog } from '@angular/material/dialog';
import { ArticleService } from 'src/app/service/article.service';
import { Article } from 'src/app/models/article.model';
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';

@Component({
  selector: 'app-article-edit',
  templateUrl: './article-edit.component.html',
  styleUrls: ['./article-edit.component.css']
})
export class ArticleEditComponent implements OnInit {
  articleForm!: FormGroup;
  typeProduit: string[] = ['legume', 'fruits', 'cereale', 'produitLaitier', 'viande', 'equipements', 'insec'];
  articleId!: number;
  isSubmitting = false;
  idAgriculteur: string | null = null;

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private snackBar: MatSnackBar,
    private dialog: MatDialog
  ) {}

  ngOnInit(): void {
    this.articleId = Number(this.activatedRoute.snapshot.paramMap.get('id'));
    this.initializeForm();
    this.getArticleById(this.articleId);
  
    this.articleId = Number(this.activatedRoute.snapshot.paramMap.get('id'));
  
    // Récupérer l'ID de l'agriculteur passé via 'state'
    const navigation = window.history.state;
    this.idAgriculteur = navigation ? navigation.idAgriculteur : null; // Si l'ID est dans 'state', l'extraire
  
    this.initializeForm();
    this.getArticleById(this.articleId);
  }
  

  initializeForm(): void {
    this.articleForm = this.fb.group({
      nom: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9 ]+$')]],
      prix: ['', [Validators.required, Validators.min(0.1)]],
      quantiteInitiale: ['', [Validators.required, Validators.min(0)]],
      quantiteDisponible: ['', [Validators.required, Validators.min(0)]],
      quantiteVendue: ['', [Validators.required, Validators.min(0)]],
      typeProduit: ['', Validators.required]
    });
  }

  getArticleById(id: number): void {
    this.articleService.getArticleById(id).subscribe(
      (data: Article) => {
        this.articleForm.patchValue(data);
      },
      (error) => {
        console.error('Erreur lors de la récupération de l\'article', error);
        this.showSnackbar('Erreur lors de la récupération de l\'article.', 'error');
      }
    );
  }

  get nom() { return this.articleForm.get('nom'); }
  get prix() { return this.articleForm.get('prix'); }
  get quantiteInitiale() { return this.articleForm.get('quantiteInitiale'); }
  get quantiteDisponible() { return this.articleForm.get('quantiteDisponible'); }
  get quantiteVendue() { return this.articleForm.get('quantiteVendue'); }
  get typeProduitControl() { return this.articleForm.get('typeProduit'); }

  confirmBeforeSubmit(): void {
    if (this.articleForm.invalid || this.isSubmitting) {
      return;
    }

    const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
      width: '400px',
      data: {
        title: 'Confirmer la modification',
        message: 'Êtes-vous sûr de vouloir modifier cet article?',
        productDetails: this.getProductDetailsForConfirmation()
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result === 'confirm') {
        this.onSubmit();
      }
    });
  }

  private getProductDetailsForConfirmation(): string {
    const formValue = this.articleForm.value;
    return `
      Nom: ${formValue.nom}
      Prix: ${formValue.prix} DT
      Type: ${formValue.typeProduit}
      Quantité Initiale: ${formValue.quantiteInitiale}
    `;
  }

  onSubmit(): void {
    if (this.articleForm.invalid || this.isSubmitting) {
      return;
    }
  
    this.isSubmitting = true;
  
    const articleData = {
      idArticle: this.articleId,
      ...this.articleForm.value
    };
  
    this.articleService.updateArticle(articleData).subscribe(
      (response) => {
        this.isSubmitting = false;
  
        if (this.idAgriculteur) {
          this.router.navigate(['/farmer/articles', this.idAgriculteur]);
          this.showSnackbar('L\'article a été modifié avec succès!', 'success');
        } else {
          console.error("idAgriculteur est null, impossible de rediriger.");
          this.showSnackbar('Modification réussie, mais idAgriculteur est manquant.', 'error');
        }
      },
      (error) => {
        this.isSubmitting = false;
        console.error('Erreur lors de la modification de l\'article', error);
        this.showSnackbar('Erreur lors de la modification de l\'article.', 'error');
      }
    );
  }
  

  private showSnackbar(message: string, type: 'success' | 'error'): void {
    this.snackBar.open(message, 'Fermer', {
      duration: 3000,
      panelClass: [`${type}-snackbar`]
    });
  }

  retourListe() {
    console.log('Redirection vers : /farmer/articles/' + this.idAgriculteur);
    if (this.idAgriculteur) {
      this.router.navigate(['/farmer/articles', this.idAgriculteur]);
    }
  }
}
