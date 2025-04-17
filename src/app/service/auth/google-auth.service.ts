import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

declare global {
  interface Window {
    google: any;
  }
}

@Injectable({
  providedIn: 'root',
})
export class GoogleAuthService {
  private clientId = '1006284615244-t3shp8037r267tqacp3g78c2h9kg08hg.apps.googleusercontent.com';
  private tokenClient: any;

  constructor() {
    this.initializeGoogleSignIn();
  }

  private initializeGoogleSignIn(): void {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    script.onload = () => {
      this.tokenClient = window.google.accounts.oauth2.initTokenClient({
        client_id: this.clientId,
        scope: 'email profile openid',
        callback: () => {},
      });
    };
    document.head.appendChild(script);
  }

  signInWithGoogle(): Observable<any> {
    return new Observable((observer) => {
      if (!this.tokenClient) {
        observer.error('Google Identity Services not initialized.');
        return;
      }

      this.tokenClient.callback = async (response: any) => {
        if (response.error) {
          observer.error(response.error);
          return;
        }

        try {
          const userInfo = await this.fetchUserInfo(response.access_token);
          observer.next({
            username: userInfo.name,
            email: userInfo.email,
            password: response.access_token, // Send token to backend
          });
          observer.complete();
        } catch (err) {
          observer.error(err);
        }
      };

      this.tokenClient.requestAccessToken();
    });
  }

  private async fetchUserInfo(accessToken: string): Promise<any> {
    const response = await fetch('https://www.googleapis.com/oauth2/v3/userinfo', {
      headers: {
        Authorization: `Bearer ${accessToken}`,
      },
    });

    if (!response.ok) {
      throw new Error('Failed to fetch user info');
    }

    return await response.json();
  }
}
