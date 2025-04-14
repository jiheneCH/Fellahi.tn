import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface User {
  id: number;
  username: string;
  email: string;
  roleName: string; // ✅ changed from role
  status: string;
  createdDate: string;
}




@Injectable({
  providedIn: 'root'
})



export class UserService {
  private baseUrl = 'http://localhost:8080/Fallehi/users'; // ✅ Replace with your backend URL

  constructor(private http: HttpClient) {}

  getAllUsers(): Observable<User[]> {
    const token = localStorage.getItem('adminToken');
    if (!token) {
      throw new Error('Admin token not found');
    }
  
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);
    return this.http.get<User[]>(this.baseUrl, { headers });
  }
  
  
  getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.baseUrl}/role/${id}`);
  }

  getUsersByRole(role: string): Observable<User[] | string> {
    const upperRole = role.toUpperCase();
    return this.http.get<User[]>(`http://localhost:8080/Fallehi/users/role/${role.toUpperCase()}`);

  }
  
  
  
}
