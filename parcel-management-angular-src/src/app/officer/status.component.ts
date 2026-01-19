import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingStatus } from '../models/booking.model';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-officer-status',
  templateUrl: './status.component.html'
})
export class OfficerStatusComponent {
  bookingId = this.route.snapshot.paramMap.get('bookingId')!;
  newStatus: BookingStatus = 'PICKED_UP';
  message: string | null = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private booking: BookingService, private router: Router) {}

  update() {
    this.error = null;
    this.message = null;
    this.booking.updateStatus(this.bookingId, this.newStatus).subscribe({
      next: (res: any) => {
        this.message = res?.message || 'Updated';
        setTimeout(() => this.router.navigate(['/officer/bookings']), 700);
      },
      error: (err) => (this.error = err?.error?.error || 'Update failed')
    });
  }
}
