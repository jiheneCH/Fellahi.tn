import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private baseUrl = 'http://localhost:8089/internshipApp/api/v1/auth'; // Replace with your actual base URL

  constructor(private http: HttpClient) { }

  register(user: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/register`, user);
  }

  refreshToken(token: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/refresh-token`, token);
  }

  authenticate(credentials: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/authenticate`, credentials);
  }

  public isAuthenticated(): boolean {
    const token = localStorage.getItem('token');
    if (token) {
      const payload = JSON.parse(atob(token.split('.')[1]));
      return payload.exp > Date.now() / 1000;
    }
    return !!token;
  }

}
