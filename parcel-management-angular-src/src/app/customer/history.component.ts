import { Component, OnInit } from '@angular/core';
import { Booking } from '../models/booking.model';
import { BookingService } from '../services/booking.service';

@Component({
  selector: 'app-history',
  templateUrl: './history.component.html'
})
export class HistoryComponent implements OnInit {
  bookings: Booking[] = [];
  page = 0;
  totalPages = 0;
  error: string | null = null;

  constructor(private bookingService: BookingService) {}

  ngOnInit() {
    this.load();
  }

  load(page = 0) {
    this.error = null;
    this.bookingService.myBookings(page, 10).subscribe({
      next: (res) => {
        this.bookings = res.content;
        this.page = res.number;
        this.totalPages = res.totalPages;
      },
      error: (err) => (this.error = err?.error?.error || 'Failed to load')
    });
  }

  exportCsv() {
    this.bookingService.exportMyBookings().subscribe((blob) => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'my-bookings.csv';
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }
}
