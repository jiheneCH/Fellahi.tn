import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthenticationService } from 'src/app/service/auth/auth.service';
import { jwtDecode } from 'jwt-decode';

interface LoginFormControls {
  [key: string]: AbstractControl;
  emailOrUsername: AbstractControl;
  password: AbstractControl;
}

interface JwtPayload {
  sub: string;
  role: string;
  exp: number;
  // Add more fields if needed
}

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  errorMessage = '';
  isLoading = false;
  showPassword = false;
  loginForm: FormGroup;

  constructor(
    private router: Router,
    private fb: FormBuilder,
    private authService: AuthenticationService
  ) {
    this.loginForm = this.fb.group({
      emailOrUsername: ['', Validators.required],
      password: ['', [
        Validators.required,
        Validators.pattern(/^(?=.*[A-Z])(?=.*[a-z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,20}$/)
      ]]
    });
  }

  get f(): LoginFormControls {
    return {
      emailOrUsername: this.loginForm.get('emailOrUsername') as AbstractControl,
      password: this.loginForm.get('password') as AbstractControl
    };
  }

  togglePasswordVisibility(): void {
    this.showPassword = !this.showPassword;
  }

  handleLogin(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.isLoading = true;
    this.errorMessage = '';

    const { emailOrUsername, password } = this.loginForm.value;

    const loginData: any = {
      password: password.trim()
    };

    if (emailOrUsername?.trim()) {
      const isEmail = emailOrUsername.includes('@');
      if (isEmail) {
        loginData.email = emailOrUsername.trim();
      } else {
        loginData.username = emailOrUsername.trim();
      }
    }

    this.authService.login(loginData).subscribe({
      next: (response) => {
        const token = response.token;
        let role = '';
        let username = '';

        try {
          const decoded = jwtDecode<JwtPayload>(token);
          role = decoded.role;
          username = decoded.sub;

          // Store in localStorage
          localStorage.setItem('userRole', role);
          localStorage.setItem('username', username);

          if (role === 'ADMIN') {
            localStorage.setItem('adminToken', token);
          }
        } catch (decodeError) {
          console.error('Error decoding token:', decodeError);
          this.errorMessage = 'Invalid token received from server.';
          this.isLoading = false;
          return;
        }

        setTimeout(() => {
          switch (role) {
            case 'ADMIN':
              this.router.navigate(['/admin/template']);
              break;
            case 'CLIENT':
              this.router.navigate(['/reclamation']);
              break;
            case 'FARMER':
              this.router.navigate(['/reclamation']);
              break;
            case 'TRANSPORTER':
              this.router.navigate(['/reclamation']);
              break;
            default:
              this.router.navigate(['/']);
              break;
          }
          this.isLoading = false;
        }, 300);
      },
      error: (err) => {
        this.isLoading = false;
        this.handleError(err);
      }
    });
  }

  private handleError(err: any): void {
    this.errorMessage = err.message || 'Login failed. Please try again.';

    if (err.status === 403) {
      this.errorMessage = 'Account not activated. Please check your email.';
    } else if (err.status === 401) {
      this.errorMessage = 'Invalid credentials. Please check your email/username and password.';
    } else if (err.status === 0) {
      this.errorMessage = 'Network error. Please check your internet connection.';
    }
  }
}
