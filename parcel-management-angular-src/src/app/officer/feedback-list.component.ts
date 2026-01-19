import { Component, OnInit } from '@angular/core';
import { FeedbackService } from '../services/feedback.service';

@Component({
  selector: 'app-feedback-list',
  templateUrl: './feedback-list.component.html'
})
export class FeedbackListComponent implements OnInit {
  items: any[] = [];
  page = 0;
  totalPages = 0;
  error: string | null = null;

  constructor(private feedback: FeedbackService) {}

  ngOnInit() {
    this.load();
  }

  load(page = 0) {
    this.error = null;
    this.feedback.listAll(page, 10).subscribe({
      next: (res) => {
        this.items = res.content;
        this.page = res.number;
        this.totalPages = res.totalPages;
      },
      error: (err) => (this.error = err?.error?.error || 'Failed to load')
    });
  }
}
