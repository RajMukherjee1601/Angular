import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Payment } from '../models/payment.model';

@Injectable({ providedIn: 'root' })
export class PaymentService {
  constructor(private http: HttpClient) {}

  payOnline(bookingId: string, cardNumber: string, expiry: string, cvv: string, cardHolder: string) {
    return this.http.post<Payment>(`${environment.apiUrl}/api/payments/${bookingId}/online`, {
      cardNumber,
      expiry, // MM/YY
      cvv,
      cardHolder
    });
  }

  getPayment(bookingId: string) {
    return this.http.get<Payment>(`${environment.apiUrl}/api/payments/${bookingId}`);
  }

  downloadReceipt(bookingId: string) {
    return this.http.get(`${environment.apiUrl}/api/invoices/${bookingId}/receipt`, { responseType: 'blob' });
  }

  downloadInvoice(bookingId: string) {
    return this.http.get(`${environment.apiUrl}/api/invoices/${bookingId}`, { responseType: 'blob' });
  }
}
