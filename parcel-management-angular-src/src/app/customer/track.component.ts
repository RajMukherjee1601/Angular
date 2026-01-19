import { Component } from '@angular/core';
import { Booking } from '../models/booking.model';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-track',
  templateUrl: './track.component.html'
})
export class TrackComponent {
  bookingId = '';
  booking: Booking | null = null;
  error: string | null = null;

  constructor(private bookingService: BookingService) {}

  track() {
    this.error = null;
    this.booking = null;
    if (!this.bookingId.trim()) return;

    this.bookingService.get(this.bookingId.trim()).subscribe({
      next: (b) => (this.booking = b),
      error: (err) => (this.error = err?.error?.error || 'Not found')
    });
  }
}
