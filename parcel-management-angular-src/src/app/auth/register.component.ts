import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html'
})
export class RegisterComponent {
  loading = false;
  message: string | null = null;
  error: string | null = null;

  form = this.fb.group({
    fullName: ['', [Validators.required]],
    email: ['', [Validators.required, Validators.email]],
    password: ['', [Validators.required, Validators.minLength(6)]]
  });

  constructor(private fb: FormBuilder, private auth: AuthService, private router: Router) {}

  submit() {
    this.error = null;
    this.message = null;
    if (this.form.invalid) return;

    this.loading = true;
    const { fullName, email, password } = this.form.value;
    this.auth.register(fullName!, email!, password!).subscribe({
      next: (res: any) => {
        this.loading = false;
        this.message = res?.message || 'Registration successful';
        setTimeout(() => this.router.navigate(['/login']), 500);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.error || 'Registration failed';
      }
    });
  }
}
