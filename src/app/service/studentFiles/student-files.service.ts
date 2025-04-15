import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class StudentFilesService {

  constructor(private _http:HttpClient) { }

  getStudentFiles(idStudent:Number){
    return this._http.get<any[]>(`http://localhost:8089/internshipApp/student-files/retrieve-student-files-by-student/${idStudent}`);
  }
  addCoverLetterToStudent(idStudent:Number,coverLetter: FormData):Observable<any[]>{
    return this._http.post<any[]>(`http://localhost:8089/internshipApp/student-files/add-cover-letter/${idStudent}`,coverLetter);
  }
}
