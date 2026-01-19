import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-officer-booking',
  templateUrl: './officer-booking.component.html'
})
export class OfficerBookingComponent {
  loading = false;
  error: string | null = null;

  customerEmail = '';

  form = this.fb.group({
    senderName: ['', Validators.required],
    senderAddress: ['', Validators.required],
    senderPhone: ['', Validators.required],
    receiverName: ['', Validators.required],
    receiverAddress: ['', Validators.required],
    receiverPhone: ['', Validators.required],
    parcelType: ['BOX', Validators.required],
    contents: ['', Validators.required],
    weightKg: [1, [Validators.required, Validators.min(0.1)]],
    distanceKm: [1, [Validators.required, Validators.min(0.1)]]
  });

  constructor(private fb: FormBuilder, private booking: BookingService, private router: Router) {}

  submit() {
    this.error = null;
    if (!this.customerEmail.trim()) {
      this.error = 'Customer email is required';
      return;
    }
    if (this.form.invalid) return;

    this.loading = true;
    this.booking.createByOfficer(this.customerEmail.trim(), this.form.value as any).subscribe({
      next: (b) => {
        this.loading = false;
        // Offline payment is auto-marked as PAID on backend.
        this.router.navigate(['/officer/bookings']);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.error || 'Booking failed';
      }
    });
  }
}
