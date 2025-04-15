import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ApplicationService {

  constructor(private _http:HttpClient) { }
  addApplicationToStudent(idOffer:Number,idStudent:Number){
    return this._http.post(`http://localhost:8089/internshipApp/app/add-application-to-student/${idStudent}/${idOffer}`,null)
  }
  getApplicationFromStudent(idStudent:Number){
    return this._http.get(`http://localhost:8089/internshipApp/app/retrieve-apps/${idStudent}`)
  }
}
