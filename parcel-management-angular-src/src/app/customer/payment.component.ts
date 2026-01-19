import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { FormBuilder, Validators } from '@angular/forms';
import { PaymentService } from '../services/payment.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html'
})
export class PaymentComponent {
  bookingId = this.route.snapshot.paramMap.get('bookingId')!;
  loading = false;
  message: string | null = null;
  error: string | null = null;

  form = this.fb.group({
    cardNumber: ['', [Validators.required, Validators.minLength(12)]],
    expiry: ['', [Validators.required]], // MM/YY
    cvv: ['', [Validators.required, Validators.minLength(3)]],
    cardHolder: ['', [Validators.required]]
  });

  constructor(
    private route: ActivatedRoute,
    private fb: FormBuilder,
    private payment: PaymentService
  ) {}

  pay() {
    this.error = null;
    this.message = null;
    if (this.form.invalid) return;

    this.loading = true;
    const v = this.form.value;
    this.payment.payOnline(this.bookingId, v.cardNumber!, v.expiry!, v.cvv!, v.cardHolder!).subscribe({
      next: (res) => {
        this.loading = false;
        this.message = `Payment successful. Transaction: ${res.transactionId}`;
      },
      error: (err) => {
        this.loading = false;
        this.error = err?.error?.error || 'Payment failed';
      }
    });
  }

  downloadInvoice() {
    this.payment.downloadInvoice(this.bookingId).subscribe(blob => this.saveBlob(blob, `invoice-${this.bookingId}.pdf`));
  }

  downloadReceipt() {
    this.payment.downloadReceipt(this.bookingId).subscribe(blob => this.saveBlob(blob, `receipt-${this.bookingId}.pdf`));
  }

  private saveBlob(blob: Blob, filename: string) {
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = filename;
    a.click();
    window.URL.revokeObjectURL(url);
  }
}
