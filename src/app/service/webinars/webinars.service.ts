import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
// import { environment } from '../../../environments/environment';
@Injectable({
  providedIn: 'root'
})
export class WebinarsService {
  apiUrl = `http://localhost:8089/internshipApp/course`; 
  constructor(private http: HttpClient) { }

  getCourse(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/retrieve-all-courses`);
  }

   // Fetch a single internship course by id
   getCourseById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/retrieve-course/${id}`);
  }

  // Create a new internship course
  createCourse(course: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/add_course`, course);
  }

  // Update an existing course
  updateCourse(id: number, course: any): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/modify-course`, course);
  }

  // Delete an internship course
  deleteCourse(id: number): Observable<any> {
    return this.http.delete<any>(`${this.apiUrl}/remove-course/${id}`);
  }
}
