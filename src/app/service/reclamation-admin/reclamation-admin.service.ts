import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Reclamation {
  id: number;
  description: string;
  subject: string;
  createdAt: string;
  status: 'PENDING' | 'IN_PROGRESS' | 'RESOLVED' | 'REJECTED';
  user: {
    id: number;
    username: string;
    email: string;
    roleName: string;
  } | null;
}
export interface ReclamationResponseDTO {
  reclamationId: number;
  adminUsername: string;
  responseMessage: string;
  updatedStatus: 'IN_PROGRESS' | 'RESOLVED';
}


@Injectable({
  providedIn: 'root'
})
export class ReclamationAdminService {

  private baseUrl = 'http://localhost:8080/Fallehi/reclamations'; // ✅ Adjust if needed

  constructor(private http: HttpClient) {}

  getReclamationsByStatus(status: string): Observable<Reclamation[]> {
    const params = new HttpParams().set('status', status);
    return this.http.get<Reclamation[]>(`${this.baseUrl}/bystatus`, { params });
  }

  // ✅ New method to fetch ALL reclamations (admin only)
  getAllReclamations(): Observable<Reclamation[]> {
    return this.http.get<Reclamation[]>(`${this.baseUrl}/all`);
  }
  respondToReclamation(reclamationId: number, response: ReclamationResponseDTO): Observable<string> {
    return this.http.post(
      `${this.baseUrl}/treat/${reclamationId}`,
      response,
      {
        responseType: 'text' as 'json'
      }
    ) as Observable<string>;
  }
  getReclamationsByDate(latest: boolean): Observable<Reclamation[]> {
    return this.http.get<Reclamation[]>(`${this.baseUrl}/filterByDate`, {
      params: new HttpParams().set('latest', latest.toString())
    });
  }
  
  
  }
  
  

  

