import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CompanyService {

  private baseUrl = `http://localhost:8089/internshipApp/company`;

  constructor(private http: HttpClient) {}

  addCompany(company: any): Observable<any> {

    return this.http.post<any>(`${this.baseUrl}/add_company`, company);
  }

  updateCompany(company: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/modify-company`, company);
  }

  deleteCompany(companyId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove-company/${companyId}`);
  }

  getCompany(companyId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/retrieve-company/${companyId}`);
  }

  getCompanies(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/retrieve-all-companies`);
  }
}
