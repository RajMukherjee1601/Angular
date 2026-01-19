import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, UrlTree } from '@angular/router';
import { TokenStorageService, UserRole } from './token-storage.service';

@Injectable({ providedIn: 'root' })
export class RoleGuard implements CanActivate {
  constructor(private storage: TokenStorageService, private router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean | UrlTree {
    const expected = route.data['role'] as UserRole;
    const actual = this.storage.getRole();
    if (expected && actual === expected) return true;
    return this.router.parseUrl('/');
  }
}
