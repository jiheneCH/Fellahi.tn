import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReviewService {
  private baseUrl = 'http://localhost:8080/piEvent/review'; 

  constructor(private http: HttpClient) {}

  addReview(review: any): Observable<any> {
    return this.http.post(`${this.baseUrl}/add`, review);
  }
}
