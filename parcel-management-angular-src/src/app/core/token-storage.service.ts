import { Injectable } from '@angular/core';

export type UserRole = 'CUSTOMER' | 'OFFICER';

@Injectable({ providedIn: 'root' })
export class TokenStorageService {
  private readonly TOKEN_KEY = 'pm_token';
  private readonly ROLE_KEY = 'pm_role';
  private readonly NAME_KEY = 'pm_name';

  setAuth(token: string, role: UserRole, fullName: string) {
    localStorage.setItem(this.TOKEN_KEY, token);
    localStorage.setItem(this.ROLE_KEY, role);
    localStorage.setItem(this.NAME_KEY, fullName);
  }

  clear() {
    localStorage.removeItem(this.TOKEN_KEY);
    localStorage.removeItem(this.ROLE_KEY);
    localStorage.removeItem(this.NAME_KEY);
  }

  getToken(): string | null {
    return localStorage.getItem(this.TOKEN_KEY);
  }

  getRole(): UserRole | null {
    return localStorage.getItem(this.ROLE_KEY) as UserRole | null;
  }

  getName(): string | null {
    return localStorage.getItem(this.NAME_KEY);
  }

  isLoggedIn(): boolean {
    return !!this.getToken();
  }
}
