import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FeedbackService } from '../services/feedback.service';

@Component({
  selector: 'app-customer-feedback',
  templateUrl: './feedback.component.html'
})
export class CustomerFeedbackComponent {
  bookingId = this.route.snapshot.paramMap.get('bookingId')!;
  rating = 5;
  comments = '';
  message: string | null = null;
  error: string | null = null;

  constructor(private route: ActivatedRoute, private feedback: FeedbackService, private router: Router) {}

  submit() {
    this.error = null;
    this.message = null;
    this.feedback.addFeedback(this.bookingId, this.rating, this.comments).subscribe({
      next: () => {
        this.message = 'Thanks! Feedback submitted.';
        setTimeout(() => this.router.navigate(['/customer/history']), 800);
      },
      error: (err) => (this.error = err?.error?.error || 'Feedback failed')
    });
  }
}
