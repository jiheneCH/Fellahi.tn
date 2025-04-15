import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReclamationService {
  private baseUrl = 'http://localhost:8080/Fallehi/reclamations';

  constructor(private http: HttpClient) {}

  addReclamation(reclamation: any): Observable<string> {
    return this.http.post(`${this.baseUrl}/add`, reclamation, { responseType: 'text' });
  }
  

  getAllReclamations(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/myReclamations`);
  }

  
}
