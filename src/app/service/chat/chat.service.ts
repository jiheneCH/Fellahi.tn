import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, Subject } from 'rxjs';


@Injectable({
    providedIn: 'root'
})
export class ChatService {

    private baseUrl = 'http://localhost:8089/internshipApp'; // Assuming your backend is running on the same host as the frontend

    
    private socket!: WebSocket;
    private messageSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

    constructor(private http: HttpClient) {

     }
      // this method is used to start connection/handhshake of socket with server
  

      connect(userId:any): void {
        this.socket = new WebSocket('ws://localhost:8089/websocket-endpoint');

        this.socket.onopen = () => {
          //const message = { type: 'GET_ALL_MESSAGES', id: userId }; // Adjust userId accordingly
          //this.socket.send(JSON.stringify(message));
          console.log('WebSocket connection established.');
        };
    
        this.socket.onclose = (event) => {
          console.log('WebSocket connection closed:', event);
        };
    
       
      }

      
  getMessageSubject(): BehaviorSubject<any> {
    return this.messageSubject;
  }
    getAll(): Observable<any> {
        const url = `${this.baseUrl}/chats/getall`;
        console.log( url);
        return this.http.get<any>(url);
      }
    //chat
      addMessage(data: any,userId: any,userId2: any): Observable<any> {
        const url = `${this.baseUrl}/chats/add/${userId}/${userId2}`;
      
        return this.http.post<any>(url, data);
      }


      //chat
      getAllChat(Id:any): Observable<any> {
        const url = `${this.baseUrl}/chats/getall/${Id}`;
        console.log( url);
        return this.http.get<any>(url);
      }


      
}