import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

declare global {
  interface Window {
    gapi: any;
  }
}

@Injectable({
  providedIn: 'root',
})
export class GoogleAuthService {
  private clientId = '1006284615244-t3shp8037r267tqacp3g78c2h9kg08hg.apps.googleusercontent.com';

  constructor() {
    this.loadGapiScript();
  }

  private loadGapiScript(): void {
    if (typeof window.gapi !== 'undefined') {
      window.gapi.load('client:auth2', () => {
        window.gapi.auth2.init({
          client_id: this.clientId,
        });
      });
    } else {
      console.error('gapi is not loaded');
    }
  }

  signInWithGoogle(): Observable<any> {
    return new Observable((observer) => {
      if (typeof window.gapi !== 'undefined') {
        const GoogleAuth = window.gapi.auth2.getAuthInstance();

        GoogleAuth.signIn().then(
          (googleUser: any) => {
            const profile = googleUser.getBasicProfile();
            const authResponse = googleUser.getAuthResponse();

            // Extract username, email, and token
            const username = profile.getName();
            const email = profile.getEmail();
            const password = authResponse.id_token; // This is your password (ID Token) to send to your backend

            // Return user data
            observer.next({ username, email, password });
            observer.complete();
          },
          (error: any) => {
            observer.error(error);
          }
        );
      } else {
        observer.error('gapi is not loaded');
      }
    });
  }
}
