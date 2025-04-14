import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/auth.service';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrls: ['./activate-account.component.css']
})
export class ActivateAccountComponent {
  activationForm: FormGroup;
  isActivating = false;
  activationMessage = '';
  activationSuccess = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.activationForm = this.fb.group({
      activationCode: ['', [
        Validators.required,
        Validators.minLength(6),
        Validators.maxLength(6),
        Validators.pattern(/^[0-9]*$/)
      ]]
    });

    // Vérifie si le token est passé dans l'URL
    this.route.queryParams.subscribe(params => {
      if (params['token']) {
        this.activationForm.patchValue({ activationCode: params['token'] });
        this.activateAccount();
      }
    });
  }

  activateAccount() {
    if (this.activationForm.invalid || this.isActivating) return;
  
    this.isActivating = true;
    const activationCode = this.activationForm.value.activationCode;
  
    this.authService.activateAccount(activationCode).subscribe({
      next: (res: any) => {
        this.activationSuccess = true;
        this.activationMessage = res.message || 'Account activated successfully! Redirecting...';
        setTimeout(() => this.router.navigate(['/login']), 1000);
      },
      error: (err: HttpErrorResponse) => {
        this.isActivating = false;
        this.activationSuccess = false;
        
        // Gestion améliorée des erreurs
        if (err.error && err.error.error) {
          this.activationMessage = err.error.error;
          
          // Si le token a expiré et un nouveau a été envoyé
          if (err.error.error.includes('new token has been sent')) {
            this.activationForm.reset();
          }
        } else {
          this.activationMessage = 'Activation failed. Please try again.';
        }
      },
      complete: () => this.isActivating = false
    });
  }
  }
