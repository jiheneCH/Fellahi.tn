import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ArticleService } from 'src/app/service/article.service';
import { ActivatedRoute, Router } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar'; // Pour les messages de notification
import { ConfirmationDialogComponent } from '../confirmation-dialog/confirmation-dialog.component';
import { Article } from 'src/app/models/article.model';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBarModule } from '@angular/material/snack-bar';






@Component({
  selector: 'app-article-form',
  templateUrl: './article-form.component.html',
  styleUrls: ['./article-form.component.css']
})
export class ArticleFormComponent {
  articleForm!: FormGroup;
  typeProduit: string[] = ['legume', 'fruits','cereale','produitLaitier','viande','equipements', 'insec']; // Tableau des types de produits
  selectedImage: string | null = null; // L'image sera stockée en base64
  idAgriculteur: string | null = null; // Variable pour stocker l'ID de l'agriculteur

  constructor(
    private fb: FormBuilder,
    private articleService: ArticleService,
    private router: Router , // Injection du Router
    private dialog: MatDialog  ,// L'injection de MatDialog
    private snackBar: MatSnackBar, 
    private route: ActivatedRoute 

  ) {}

  ngOnInit(): void {
  // Extraire l'ID depuis l'URL
  this.route.paramMap.subscribe(params => {
    this.idAgriculteur = params.get('id'); // Assurez-vous que l'URL contient un paramètre 'id'
    console.log('ID de l\'agriculteur extrait de l\'URL:', this.idAgriculteur);
  });
    // Initialisation du formulaire
    this.articleForm = this.fb.group({
      nom: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9 ]+$')]], // Nom avec validation de non-espaces vides
      prix: ['', [Validators.required, Validators.min(0.1)]],
      quantiteInitiale: ['', [Validators.required, Validators.min(0)]],
      quantiteDisponible: ['', [Validators.required, Validators.min(0)]],
      quantiteVendue: ['', [Validators.required, Validators.min(0)]],
      typeProduit: ['', Validators.required], // Validation pour le champ typeProduit
      image: [''] ,
    });
  }
  get nom() { return this.articleForm.get('nom'); }
  get prix() { return this.articleForm.get('prix'); }
  get quantiteInitiale() { return this.articleForm.get('quantiteInitiale'); }
  get quantiteDisponible() { return this.articleForm.get('quantiteDisponible'); }
  get quantiteVendue() { return this.articleForm.get('quantiteVendue'); }
  confirmBeforeSubmit(): void {
    if (this.articleForm.valid) {
      const dialogRef = this.dialog.open(ConfirmationDialogComponent, {
        width: '300px',
        data: {
          message: 'Êtes-vous sûr de vouloir ajouter ce produit ?'
        }
      });

      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.onSubmit(); // Si l'utilisateur confirme, on soumet le formulaire
        }
      });
    } else {
      this.snackBar.open('Veuillez remplir correctement tous les champs.', 'Fermer', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });
    }
  }
  onSubmit(): void {
    if (this.articleForm.invalid) {
      return;
    }
  
    const newArticle: Article = {
      ...this.articleForm.value,
      image: this.selectedImage || '' // Si image est null, passer une chaîne vide
    };
  
    console.log('Article à enregistrer :', newArticle);
  
    // Récupère l'ID de l'agriculteur depuis l'URL et le convertit en number
    const idAgriculteur = Number(this.idAgriculteur);  // Convertit l'ID en number
  
    if (!isNaN(idAgriculteur)) {
      this.articleService.ajouterrArticle(idAgriculteur, newArticle).subscribe({
        next: () => {
          this.snackBar.open('Article ajouté avec succès !', 'Fermer', {
            duration: 3000,
            panelClass: ['success-snackbar']
          });
          this.retourListe();
        },
        error: (error) => {
          console.error('Erreur lors de l\'ajout de l\'article:', error);
          this.snackBar.open('Une erreur est survenue, veuillez réessayer.', 'Fermer', {
            duration: 3000,
            panelClass: ['error-snackbar']
          });
        }
      });
    } else {
      this.snackBar.open('ID invalide', 'Fermer', {
        duration: 3000,
        panelClass: ['error-snackbar']
      });
    }
  }
  

  retourListe() {
    this.router.navigate(['/articles']); // adapte ce chemin selon ta route réelle
  }
  onImageSelected(event: any): void {
    const file = event.target.files[0];
    if (file) {
      const reader = new FileReader();
      reader.onloadend = () => {
        this.selectedImage = reader.result as string; // Enregistrez l'image en base64
      };
      reader.readAsDataURL(file); // Convertit l'image en base64
    }
  }
  
}
