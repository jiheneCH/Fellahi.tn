import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { WaitingListEntry } from './WaitingListEntry';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class WaitinglistService {
  
  private baseUrl = 'http://localhost:8080/piEvent/reservation'; 

  constructor(private http: HttpClient) {}

  getAllWaitinglists(): Observable<WaitingListEntry[]> {
    return this.http.get<WaitingListEntry[]>(`${this.baseUrl}/retrieveAllWaitinglist`);
  }


  
   searchWaitinglistByReference(searchReference: string): Observable<WaitingListEntry[]> {
      // Créer les paramètres de la requête
      const params = new HttpParams().set('reference', searchReference);
  
      // Effectuer la requête GET à l'API
      return this.http.get<WaitingListEntry[]>(`${this.baseUrl}/search-waitinglist`, { params });
    }
    

}
