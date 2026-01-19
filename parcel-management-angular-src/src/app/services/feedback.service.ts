import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class FeedbackService {
  constructor(private http: HttpClient) {}

  addFeedback(bookingId: string, rating: number, comments: string) {
    return this.http.post(`${environment.apiUrl}/api/feedback/${bookingId}`, { rating, comments });
  }

  listAll(page = 0, size = 10) {
    const params = new HttpParams().set('page', page).set('size', size);
    return this.http.get<any>(`${environment.apiUrl}/api/feedback`, { params });
  }
}
