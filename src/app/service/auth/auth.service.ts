import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, map, retry, tap } from 'rxjs/operators';
import { jwtDecode } from 'jwt-decode';

// Interface Definitions
export interface SignUpRequest {
  username: string;
  email: string;
  password: string;
  role: string;
  recaptchaToken: string; // ✅ Add this line
}

interface JwtPayload {
  sub: string;
  role: string;
  exp: number;
  // Add more fields if needed
}


export interface SignInRequest {
  email?: string;
  username?: string;
  password: string;
}

interface AuthResponse {
  token: string;
  claims: UserClaims;
}

interface UserClaims {
  sub: string;
  exp: number;
  iat: number;
  userId: number;
  role: string;
  [key: string]: any;
}

interface ApiError {
  status: number;
  message: string;
  timestamp?: string;
  path?: string;
}

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
  private baseUrl = 'http://localhost:8080/Fallehi/auth';
  private authState = new BehaviorSubject<boolean>(this.isAuthenticated());

  constructor(private http: HttpClient) { }

  // Public Observable for authentication state
  get authState$() {
    return this.authState.asObservable();
  }

  // Registration
 // auth.service.ts
register(userData: any): Observable<any> {
  return this.http.post<any>(`${this.baseUrl}/auth/register`, userData).pipe(
    catchError((error: HttpErrorResponse) => {
      return throwError(() => error);
    })
  );
}


  // Authentication
  login(credentials: SignInRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/authenticate`, credentials).pipe(
      tap(response => this.storeAuthData(response)),
      catchError(this.handleError)
    );
  }

  // Account Activation
  activateAccount(token: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/activate-account`, { token }).pipe(
      tap(() => this.authState.next(true)),
      catchError(this.handleError)
    );
  }

  // Password Recovery
  forgotPassword(email: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/forgot-password`, { email }).pipe(
      retry(2),
      catchError(this.handleError)
    );
  }

  // Password Reset
  resetPassword(token: string, newPassword: string): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/reset-password`, { token, newPassword }).pipe(
      tap(() => this.clearAuthData()),
      catchError(this.handleError)
    );
  }

  // Token Management
  private storeAuthData(response: AuthResponse): void {
    localStorage.setItem('token', response.token);
    localStorage.setItem('userClaims', JSON.stringify(response.claims));
    this.authState.next(true);
    
  }
// Google registration first step 
registerWithGoogle(username: string, email: string): Observable<any> {
  return this.http.post(`${this.baseUrl}/google-signup`, { username, email });
}


  clearAuthData(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('userClaims');
    this.authState.next(false);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  getCurrentUser(): UserClaims | null {
    const claims = localStorage.getItem('userClaims');
    return claims ? JSON.parse(claims) : null;
  }

  isAuthenticated(): boolean {
    const token = this.getToken();
    if (!token) return false;

    try {
      const decoded = jwtDecode<UserClaims>(token);
      return decoded.exp > Date.now() / 1000;
    } catch (error) {
      console.error('Invalid token:', error);
      this.clearAuthData();
      return false;
    }
  }

  // Error Handling
  private handleError = (error: HttpErrorResponse): Observable<never> => {
    const apiError: ApiError = {
      status: error.status,
      message: this.getUserFriendlyMessage(error)
    };

    console.error('Auth Error:', {
      Status: error.status,
      Message: apiError.message,
      Path: error.url,
      Timestamp: new Date().toISOString()
    });

    return throwError(() => apiError);
  }

  private getUserFriendlyMessage(error: HttpErrorResponse): string {
    const serverMessage = error.error?.error || error.error?.message;
    
    if (serverMessage) {
      return serverMessage;
    }

    switch (error.status) {
      case 0: return 'Network error - Please check your internet connection';
      case 400: return 'Invalid request format';
      case 401: return 'Invalid credentials';
      case 403: return 'Account not activated';
      case 404: return 'Resource not found';
      case 409: return 'User already exists';
      case 423: return 'Account locked';
      case 429: return 'Too many attempts';
      case 500: return 'Server error';
      default: return 'Unexpected error occurred';
    }
  }
  logout(): void {
    localStorage.removeItem('token');
     // Or however you're storing auth
  }
  
}