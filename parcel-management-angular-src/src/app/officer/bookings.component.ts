import { Component, OnInit } from '@angular/core';
import { Booking, BookingStatus } from '../models/booking.model';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-officer-bookings',
  templateUrl: './bookings.component.html'
})
export class OfficerBookingsComponent implements OnInit {
  bookings: Booking[] = [];
  page = 0;
  totalPages = 0;
  status: BookingStatus | '' = '';
  error: string | null = null;

  constructor(private bookingService: BookingService) {}

  ngOnInit() {
    this.load();
  }

  load(page = 0) {
    this.error = null;
    this.bookingService.allBookings(page, 10, this.status || undefined).subscribe({
      next: (res) => {
        this.bookings = res.content;
        this.page = res.number;
        this.totalPages = res.totalPages;
      },
      error: (err) => (this.error = err?.error?.error || 'Failed to load')
    });
  }

  exportCsv() {
    this.bookingService.exportAllBookings().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'all-bookings.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
