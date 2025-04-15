import { Injectable } from '@angular/core';
import { Event } from './Event';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class EventsService {
 
  private baseUrl = 'http://localhost:8080/piEvent/event'; 

  constructor(private http: HttpClient) {}

  getAllEvents(): Observable<Event[]> {
    return this.http.get<Event[]>(`${this.baseUrl}/retrieveAllEvent`);
  }

  addEvent(formData: FormData): Observable<any> {
    return this.http.post(`${this.baseUrl}/addEvent`, formData, {
      reportProgress: true,
      observe: 'response'
    });
  }

  
  getEventById(idEvent: number): Observable<Event> {
    return this.http.get<Event>(`${this.baseUrl}/retrieveEvent/${idEvent}`);
  }

  

  searchEventByReference(searchReference: string): Observable<Event[]> {
    // Créer les paramètres de la requête
    const params = new HttpParams().set('reference', searchReference);

    // Effectuer la requête GET à l'API
    return this.http.get<Event[]>(`${this.baseUrl}/search`, { params });
  }


  
  
  
  
 
}
