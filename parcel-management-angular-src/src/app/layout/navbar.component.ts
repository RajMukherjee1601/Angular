import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { TokenStorageService } from '../core/token-storage.service';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html'
})
export class NavbarComponent {
  constructor(
    public storage: TokenStorageService,
    private auth: AuthService,
    private router: Router
  ) {}

  logout() {
    this.auth.logout();
    this.router.navigate(['/login']);
  }
}
