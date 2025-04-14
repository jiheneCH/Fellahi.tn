import { HttpErrorResponse } from '@angular/common/http';
import { Component } from '@angular/core';
import { FormControl, FormControlOptions, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/auth.service';
import { ReCaptchaV3Service } from 'ng-recaptcha';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  msgerror: string = '';
  isloading: boolean = false;

  constructor(
    private _AuthService: AuthenticationService,
    private _Router: Router,
    private recaptchaV3Service: ReCaptchaV3Service // Inject reCAPTCHA
  ) {}

  // Register form with validation
  registerform: FormGroup = new FormGroup({
    name: new FormControl('', [Validators.required, Validators.minLength(3), Validators.maxLength(20)]),
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/)
    ]),
    rePassword: new FormControl(''),
    phone: new FormControl('', [Validators.required, Validators.pattern(/^01[0125][0-9]{8}$/)])
  }, { validators: [this.confirmpassword] } as FormControlOptions);

  confirmpassword(group: FormGroup): void {
    let password = group.get('password');
    let rePassword = group.get('rePassword');

    if (rePassword?.value == '') {
      rePassword.setErrors({ required: true });
    } else if (rePassword?.value != password?.value) {
      rePassword?.setErrors({ mismatch: true });
    }
  }

  submitting(): void {
    if (this.registerform.valid) {
      this.isloading = true;

      // 🔐 Step 1: Run reCAPTCHA before sending data
      this.recaptchaV3Service.execute('register').subscribe({
        next: (token: string) => {
          const formData = {
            ...this.registerform.value,
            recaptchaToken: token // 👈 Send token with form data
          };

          // Step 2: Submit to backend with reCAPTCHA token
          this._AuthService.register(formData).subscribe({
            next: (response) => {
              this.isloading = false;
              if (response.message === 'success') {
                this._Router.navigate(['login']);
              }
            },
            error: (err: HttpErrorResponse) => {
              this.isloading = false;
              this.msgerror = err.error?.message;
              console.log(err);
            }
          });
        },
        error: (err) => {
          this.isloading = false;
          console.error('reCAPTCHA error:', err);
          this.msgerror = 'Captcha verification failed';
        }
      });
    }
  }
}
