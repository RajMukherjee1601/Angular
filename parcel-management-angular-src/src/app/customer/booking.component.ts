import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-customer-booking',
  templateUrl: './booking.component.html'
})
export class CustomerBookingComponent {
  loading = false;
  error: string | null = null;
  createdBookingId: string | null = null;
  totalCost: string | null = null;

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
    if (this.form.invalid) return;

    this.loading = true;
    this.booking.createCustomer(this.form.value as any).subscribe({
      next: (b) => {
        this.loading = false;
        this.createdBookingId = b.bookingId;
        this.totalCost = b.totalCost;
        // Redirect to payment screen
        this.router.navigate(['/customer/pay', b.bookingId]);
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.error || 'Booking failed';
      }
    });
  }
}
