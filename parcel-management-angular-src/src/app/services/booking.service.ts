import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Booking, BookingStatus } from '../models/booking.model';

export interface BookingCreateRequest {
  senderName: string;
  senderAddress: string;
  senderPhone: string;
  receiverName: string;
  receiverAddress: string;
  receiverPhone: string;
  parcelType: string;
  contents: string;
  weightKg: number;
  distanceKm: number;
}

@Injectable({ providedIn: 'root' })
export class BookingService {
  constructor(private http: HttpClient) {}

  createCustomer(req: BookingCreateRequest) {
    return this.http.post<Booking>(`${environment.apiUrl}/api/bookings/customer`, req);
  }

  createByOfficer(customerEmail: string, req: BookingCreateRequest) {
    return this.http.post<Booking>(`${environment.apiUrl}/api/bookings/officer/${customerEmail}`, req);
  }

  get(bookingId: string) {
    return this.http.get<Booking>(`${environment.apiUrl}/api/bookings/${bookingId}`);
  }

  myBookings(page = 0, size = 10, status?: BookingStatus) {
    let params = new HttpParams().set('page', page).set('size', size);
    if (status) params = params.set('status', status);
    return this.http.get<any>(`${environment.apiUrl}/api/bookings/my`, { params });
  }

  allBookings(page = 0, size = 10, status?: BookingStatus) {
    let params = new HttpParams().set('page', page).set('size', size);
    if (status) params = params.set('status', status);
    return this.http.get<any>(`${environment.apiUrl}/api/bookings`, { params });
  }

  updatePickupDrop(bookingId: string, pickupTime?: string | null, dropTime?: string | null) {
    return this.http.put(`${environment.apiUrl}/api/bookings/${bookingId}/pickup-drop`, { pickupTime, dropTime });
  }

  updateStatus(bookingId: string, newStatus: BookingStatus) {
    return this.http.put(`${environment.apiUrl}/api/bookings/${bookingId}/status`, { newStatus });
  }

  cancelCustomer(bookingId: string, reason: string) {
    return this.http.post(`${environment.apiUrl}/api/bookings/${bookingId}/cancel`, { reason });
  }

  cancelByOfficer(bookingId: string, reason: string) {
    return this.http.post(`${environment.apiUrl}/api/bookings/${bookingId}/cancel-by-officer`, { reason });
  }

  exportMyBookings() {
    return this.http.get(`${environment.apiUrl}/api/export/my-bookings`, { responseType: 'blob' });
  }

  exportAllBookings() {
    return this.http.get(`${environment.apiUrl}/api/export/all-bookings`, { responseType: 'blob' });
  }
}
