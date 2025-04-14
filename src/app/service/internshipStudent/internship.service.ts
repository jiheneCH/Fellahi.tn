import { HttpClient, HttpEvent, HttpHeaders, HttpRequest } from '@angular/common/http';
import { Injectable, Type } from '@angular/core';
import { Observable } from 'rxjs';
import { Internship } from '../../components/internshps/internship';

@Injectable({
  providedIn: 'root'
})
export class InternshipService {
  // apiUrl :string = "http://localhost:8089/internshipApp/inter/retrieve-all-interofs";
  // internships:any[]=[];
  constructor(private _http:HttpClient) { }
  getAllInternshps():Observable<any[]>{
    let headers = new HttpHeaders({
      'x-rapidapi-host': 'http://localhost:8089/internshipApp',
      'x-rapidapi-key': 'your-api-key'
  });
    return this._http.get<any[]>("http://localhost:8089/internshipApp/inter/retrieve-all-interofs");
  }
  setFavInternships(internshipId:Number, studentId:Number):Observable<any[]>{
    return this._http.post<any[]>(`http://localhost:8089/internshipApp/student/mark-internship/${internshipId}/${studentId}`,null);
  }
  getFavInternships(studentId:Number):Observable<any[]>{
    return this._http.get<any[]>(`http://localhost:8089/internshipApp/student/retrieve-marked-internships/${studentId}`);
  }
  getInternshipByType(type:string):Observable<any[]>{
    return this._http.get<any[]>(`http://localhost:8089/internshipApp/inter/retrieve-interof/${type}`);
  }
  getMatch():Observable<any[]>{
    return this._http.get<any[]>(`http://127.0.0.1:5000/get-ats-output`);
  }
  deleteApplication(appId:Number):Observable<any[]>{
    return this._http.delete<any[]>(`http://localhost:8089/internshipApp/app/remove-application/${appId}`);
  }
}
