import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Reservation } from './Reservation';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  private baseUrl = 'http://localhost:8080/piEvent/reservation'; 

  constructor(private http: HttpClient) {}

  getAllReservations(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.baseUrl}/retrieveAllReservation`);
  }

  getAllReservationsClient(userId: number): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.baseUrl}/client/${userId}`);
  }
  
 searchReservationByReference(searchReference: string): Observable<Reservation[]> {
    // Créer les paramètres de la requête
    const params = new HttpParams().set('reference', searchReference);

    // Effectuer la requête GET à l'API
    return this.http.get<Reservation[]>(`${this.baseUrl}/search-reference`, { params });
  }

  cancelReservation(idReservation: number): Observable<any> {
    return this.http.delete(`${this.baseUrl}/cancel/${idReservation}`, { responseType: 'text' });
  }

  
  


   
}
