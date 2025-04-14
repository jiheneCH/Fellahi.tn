import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentService {

  private baseUrl = "http://localhost:8089/internshipApp/student";
  constructor(private http: HttpClient) { }


  addStudent(student: any): Observable<any> {
    return this.http.post<any>(`${this.baseUrl}/add-student`, student);
  }

  updateStudent(student: any): Observable<any> {
    return this.http.put<any>(`${this.baseUrl}/modify-student`, student);
  }

  deleteStudent(studentId: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/remove-student/${studentId}`);
  }

  getStudent(studentId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/retrieve-student/${studentId}`);
  }

  getStudents(): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/retrieve-students`);
}
}