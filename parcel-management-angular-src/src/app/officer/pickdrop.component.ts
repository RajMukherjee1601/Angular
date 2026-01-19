import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-officer-pickdrop',
  templateUrl: './pickdrop.component.html'
})
export class OfficerPickDropComponent {
  bookingId = this.route.snapshot.paramMap.get('bookingId')!;
  pickupTime: string | null = null;
  dropTime: string | null = null;
  message: string | null = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private booking: BookingService, private router: Router) {}

  update() {
    this.error = null;
    this.message = null;
    this.booking.updatePickupDrop(this.bookingId, this.pickupTime, this.dropTime).subscribe({
      next: (res: any) => {
        this.message = res?.message || 'Updated';
        setTimeout(() => this.router.navigate(['/officer/bookings']), 700);
      },
      error: (err) => (this.error = err?.error?.error || 'Update failed')
    });
  }
}
