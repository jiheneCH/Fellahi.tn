import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ResumeService {

  constructor(private _http:HttpClient) { }
  saveResumeToStudent(idStudent:Number,resume: FormData):Observable<any[]>{
    return this._http.post<any[]>(`http://localhost:8089/internshipApp/studentResume/add-resume-to-student/${idStudent}`,resume);
  }
  showResume(resumeName:string):Observable<Blob>{
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf');
    return this._http.get(`http://localhost:8089/internshipApp/pdf/${resumeName}`,{ responseType: 'blob' });
  }
  getResumeByStudent(idStudent:Number){
    return this._http.get<any[]>(`http://localhost:8089/internshipApp/studentResume/${idStudent}`);
  }
  showResumeInfos():Observable<any[]>{
  // headers = headers.set("Access-Control-Allow-Origin", "http://localhost:5000");
    return this._http.get<any[]>(`http://localhost:5000/get-student-informations`);
  }
  addContentToProfile(content:any,idStudent:Number):Observable<any[]>{
    // headers = headers.set("Access-Control-Allow-Origin", "http://localhost:5000");
      return this._http.post<any[]>(`http://localhost:8089/internshipApp/content/add_content/${idStudent}`,content);
    }

    getContent(idStudent:Number){
      return this._http.get<any[]>(`http://localhost:8089/internshipApp/content/${idStudent}`);
    } 
}
