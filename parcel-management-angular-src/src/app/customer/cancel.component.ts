import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-cancel',
  templateUrl: './cancel.component.html'
})
export class CancelComponent {
  bookingId = this.route.snapshot.paramMap.get('bookingId')!;
  reason = '';
  message: string | null = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private booking: BookingService, private router: Router) {}

  cancel() {
    this.error = null;
    this.message = null;
    this.booking.cancelCustomer(this.bookingId, this.reason || 'Customer cancelled').subscribe({
      next: (res: any) => {
        this.message = res?.message || 'Cancelled';
        setTimeout(() => this.router.navigate(['/customer/history']), 800);
      },
      error: (err) => (this.error = err?.error?.error || 'Cancel failed')
    });
  }
}
