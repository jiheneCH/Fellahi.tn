import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthenticationService } from 'src/app/service/auth/auth.service';
import { Router } from '@angular/router';
import { catchError, switchMap } from 'rxjs/operators';
import { GoogleAuthService } from 'src/app/service/auth/google-auth.service';
import { ReCaptchaV3Service } from 'ng-recaptcha';
import { of } from 'rxjs';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  registerform: FormGroup = this.fb.group({});
  isloading = false;
  msgerror: string = '';
  showPassword: boolean = false;
  showRePassword: boolean = false;
  userData: any;

  constructor(
    private fb: FormBuilder,
    private authService: AuthenticationService,
    private googleAuthService: GoogleAuthService,
    private router: Router,
    private recaptchaV3Service: ReCaptchaV3Service
  ) {}

  ngOnInit(): void {
    this.registerform = this.fb.group({
      username: ['', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[$!%*?&])[A-Za-z\d$!%*?&]{8,20}$/)]],
      rePassword: ['', [Validators.required]],
      role: ['', Validators.required]
    }, { validators: this.passwordMatchValidator });
  }

  passwordMatchValidator(group: FormGroup): { [key: string]: boolean } | null {
    const password = group.get('password')?.value;
    const rePassword = group.get('rePassword')?.value;
    return password && rePassword && password !== rePassword ? { mismatch: true } : null;
  }

  onSubmit(): void {
    if (this.registerform.invalid) return;

    this.isloading = true;
    const user = this.registerform.value;

    // Execute reCAPTCHA
    this.recaptchaV3Service.execute('register')
      .pipe(
        switchMap((token: string) => {
          const userWithCaptcha = { ...user, recaptchaToken: token };
          return this.authService.register(userWithCaptcha);
        }),
        catchError(error => {
          this.isloading = false;
          this.msgerror = error.message || 'Something went wrong. Please try again.';
          return of(); // return empty observable to complete stream
        })
      )
      .subscribe(() => {
        this.isloading = false;
        this.router.navigate(['/activate-account']);
      });
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  toggleRePasswordVisibility(): void {
    this.showRePassword = !this.showRePassword;
  }

  onGoogleLogin(): void {
    this.googleAuthService.signInWithGoogle().subscribe(
      (googleUser) => {
        const { username, email, password } = googleUser;
        localStorage.setItem('userData', JSON.stringify({ username, email, password }));
        this.router.navigate(['/role']);
      },
      (error) => {
        if (error === 'popup_closed_by_user') {
          this.msgerror = 'The Google login popup was closed before completing the process. Please try again.';
        } else if (error.message) {
          this.msgerror = error.message;
        } else {
          this.msgerror = 'An error occurred during Google login. Please try again later.';
        }
        console.error('Google Login failed:', error);
      }
    );
  }
}
