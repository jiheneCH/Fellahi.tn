import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as internal from 'stream';

@Injectable({
    providedIn: 'root'
})
export class SupervisorAdminService {

    private baseUrl = 'http://localhost:8089/internshipApp'; // Assuming your backend is running on the same host as the frontend


    constructor(private http: HttpClient) { }
    assignProfessors(): Observable<any> {
        return this.http.put<any>(`${this.baseUrl}/supervisor/assign-supervisors`,{});
  
    }
  ////supervisorAdmin
      getProfessors(): Observable<any> {
          return this.http.get<any>(`${this.baseUrl}/supervisor/retrieve-supervisors`);
  
      }
      getStatsSupervisor(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/supervisor/countByStudentCount`);
    }
}
