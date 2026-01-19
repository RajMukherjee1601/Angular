import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { map } from 'rxjs/operators';
import { TokenStorageService, UserRole } from '../core/token-storage.service';

interface LoginResponse {
  token: string;
  fullName: string;
  email: string;
  role: UserRole;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  constructor(private http: HttpClient, private storage: TokenStorageService) {}

  register(fullName: string, email: string, password: string) {
    return this.http.post(`${environment.apiUrl}/api/auth/register`, {
      fullName,
      email,
      password,
      role: 'CUSTOMER'
    });
  }

  login(email: string, password: string) {
    return this.http
      .post<LoginResponse>(`${environment.apiUrl}/api/auth/login`, { email, password })
      .pipe(
        map((res) => {
          this.storage.setAuth(res.token, res.role, res.fullName);
          return res;
        })
      );
  }

  logout() {
    this.storage.clear();
  }
}
