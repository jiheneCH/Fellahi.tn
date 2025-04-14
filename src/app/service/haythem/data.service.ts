import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class DataService {

    private baseUrl = 'http://localhost:8089/internshipApp'; // Assuming your backend is running on the same host as the frontend


    constructor(private http: HttpClient) { }

    getProfessors(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/supervisor/retrieve-supervisors`);

    }

    getStudents(): Observable<any> {
      return this.http.get<any>(`${this.baseUrl}/student/retrieve-students`);
  }
  getPdf(fileName: string): Observable<Blob> {
    const url = `${this.baseUrl}/pdf/${fileName}`;
    console.log('PDF File Path:', url);

    return this.http.get(url, { responseType: 'blob' });
  }
  downloadPdf(fileName: string): Observable<Blob> {
    const url = `${this.baseUrl}/pdf_files/${fileName}`;
    console.log('PDF File Path:', url);

    return this.http.get(url, { responseType: 'blob' });
  }
  validate(studentFiles: any): Observable<any> {
    return this.http.put<any>(this.baseUrl, studentFiles);
  }//update url

  getStudentFileById(studentId: string): Observable<any> {
    const url = `${this.baseUrl}/retrieve-student-files/${studentId}`;
    return this.http.get<any>(url);
  }
  getStudentById(studentId:Number):Observable<any>{
    return this.http.get<any>(`${this.baseUrl}/retrieve-student/${studentId}`)
  }
  getStudentsByInternship(internshipId:Number):Observable<any>{
    return this.http.get<any>(`${this.baseUrl}/student/retrieve-students-by-internship/${internshipId}`)
  }
}

