// src/app/shared/utils/jwt-utils.ts
export function parseJwt(token: string): any {
    try {
      const base64Payload = token.split('.')[1];
      const decodedPayload = atob(base64Payload);
      return JSON.parse(decodedPayload);
    } catch (e) {
      console.error('Invalid token', e);
      return null;
    }
  }
  