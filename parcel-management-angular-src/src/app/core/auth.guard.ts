import { Injectable } from '@angular/core';
import { CanActivate, Router, UrlTree } from '@angular/router';
import { TokenStorageService } from './token-storage.service';

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {
  constructor(private storage: TokenStorageService, private router: Router) {}

  canActivate(): boolean | UrlTree {
    return this.storage.isLoggedIn() ? true : this.router.parseUrl('/login');
  }
}
