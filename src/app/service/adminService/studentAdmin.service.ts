import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as internal from 'stream';

@Injectable({
    providedIn: 'root'
})
export class StudentAdminService {

    private baseUrl = 'http://localhost:8089/internshipApp'; // Assuming your backend is running on the same host as the frontend


    constructor(private http: HttpClient) { }
    getStudents(): Observable<any> {
        return this.http.get<any>(`${this.baseUrl}/student/retrieve-students`);
    }
    //studentAdmin
    getPdf(fileName: string): Observable<Blob> {
      const url = `${this.baseUrl}/pdf/${fileName}`;
      console.log('PDF File Path:', url);
      console.log(fileName);
      console.log("gjgh");
  
  
      return this.http.get(url, { responseType: 'blob' });
    }
    //studentAdmin
    downloadPdf(fileName: string): Observable<Blob> {
      const url = `${this.baseUrl}/pdf_files/${fileName}`;
     // console.log('PDF File Path:', url);    
      return this.http.get(url, { responseType: 'blob' });
    }
  //studenAdmin
    validate(file: any): Observable<any> {
      const url = `${this.baseUrl}/student-files/modify-student-files`;

      return this.http.put<any>(url, file);
    }//update url
  
  //studentAdmin
    getStudentFileById(studentId: string): Observable<any> {
      const url = `${this.baseUrl}/student-files/retrieve-student-files/${studentId}`;
      return this.http.get<any>(url);
    }

    uploadFile(studentId: any, file: File) {
      const formData = new FormData();
      formData.append('file', file);
  
      return this.http.put(`${this.baseUrl}/student/students/${studentId}/calculateAverageGrade`, formData);
    }

    getStudentsBySup(supervisorId: any): Observable<any> {
      return this.http.get<any>(`${this.baseUrl}/student/supervisor/${supervisorId}`);
  }
}