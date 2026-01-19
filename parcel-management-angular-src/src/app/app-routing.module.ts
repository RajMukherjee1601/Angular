import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './core/auth.guard';
import { RoleGuard } from './core/role.guard';

import { LoginComponent } from './auth/login.component';
import { RegisterComponent } from './auth/register.component';
import { CustomerDashboardComponent } from './customer/customer-dashboard.component';
import { OfficerDashboardComponent } from './officer/officer-dashboard.component';
import { CustomerBookingComponent } from './customer/booking.component';
import { PaymentComponent } from './customer/payment.component';
import { TrackComponent } from './customer/track.component';
import { HistoryComponent } from './customer/history.component';
import { CancelComponent } from './customer/cancel.component';
import { CustomerFeedbackComponent } from './customer/feedback.component';

import { OfficerBookingComponent } from './officer/officer-booking.component';
import { OfficerBookingsComponent } from './officer/bookings.component';
import { OfficerStatusComponent } from './officer/status.component';
import { OfficerPickDropComponent } from './officer/pickdrop.component';
import { OfficerCancelComponent } from './officer/cancel.component';
import { FeedbackListComponent } from './officer/feedback-list.component';

const routes: Routes = [
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },

  // CUSTOMER
  {
    path: 'customer',
    component: CustomerDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/book',
    component: CustomerBookingComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/pay/:bookingId',
    component: PaymentComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/track',
    component: TrackComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/history',
    component: HistoryComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/cancel/:bookingId',
    component: CancelComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },
  {
    path: 'customer/feedback/:bookingId',
    component: CustomerFeedbackComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'CUSTOMER' }
  },

  // OFFICER
  {
    path: 'officer',
    component: OfficerDashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/book',
    component: OfficerBookingComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/bookings',
    component: OfficerBookingsComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/status/:bookingId',
    component: OfficerStatusComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/pickdrop/:bookingId',
    component: OfficerPickDropComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/cancel/:bookingId',
    component: OfficerCancelComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },
  {
    path: 'officer/feedback',
    component: FeedbackListComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { role: 'OFFICER' }
  },

  { path: '**', redirectTo: 'login' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}
