import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { NavbarComponent } from './layout/navbar.component';

import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';

import { CustomerDashboardComponent } from './customer/customer-dashboard.component';
import { CustomerBookingComponent } from './customer/booking.component';
import { PaymentComponent } from './customer/payment.component';
import { TrackComponent } from './customer/track.component';
import { HistoryComponent } from './customer/history.component';
import { CancelComponent } from './customer/cancel.component';
import { CustomerFeedbackComponent } from './customer/feedback.component';

import { OfficerDashboardComponent } from './officer/officer-dashboard.component';
import { OfficerBookingComponent } from './officer/officer-booking.component';
import { OfficerBookingsComponent } from './officer/bookings.component';
import { OfficerStatusComponent } from './officer/status.component';
import { OfficerPickDropComponent } from './officer/pickdrop.component';
import { OfficerCancelComponent } from './officer/cancel.component';
import { FeedbackListComponent } from './officer/feedback-list.component';

import { AuthInterceptor } from './core/auth.interceptor';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    LoginComponent,
    RegisterComponent,
    CustomerDashboardComponent,
    CustomerBookingComponent,
    PaymentComponent,
    TrackComponent,
    HistoryComponent,
    CancelComponent,
    CustomerFeedbackComponent,
    OfficerDashboardComponent,
    OfficerBookingComponent,
    OfficerBookingsComponent,
    OfficerStatusComponent,
    OfficerPickDropComponent,
    OfficerCancelComponent,
    FeedbackListComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule,
    AppRoutingModule
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
