# Parcel Management Backend (Spring Boot + H2)

## Tech
- Spring Boot (REST)
- Spring Security + JWT
- Spring Data JPA
- H2 in-memory DB

## Quick start
1. **Install Java 17+ and Maven**
2. Run:
   ```bash
   mvn spring-boot:run
   ```
3. API base URL: `http://localhost:8080`

## H2 Console
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:parceldb`
- Username: `sa`
- Password: (blank)

## Default Officer (for demo)
- Email: `officer@parcel.com`
- Password: `officer123`

## Main API endpoints
### Auth
- `POST /api/auth/register`
- `POST /api/auth/login`

### Booking
- `POST /api/bookings/customer` (CUSTOMER)
- `POST /api/bookings/officer/{customerEmail}` (OFFICER)
- `GET /api/bookings/{bookingId}` (CUSTOMER can view only own)
- `GET /api/bookings/my?page=0&size=10&status=PAID` (CUSTOMER)
- `GET /api/bookings?page=0&size=10&status=BOOKED` (OFFICER)
- `PUT /api/bookings/{bookingId}/pickup-drop` (OFFICER)
- `PUT /api/bookings/{bookingId}/status` (OFFICER)
- `POST /api/bookings/{bookingId}/cancel` (CUSTOMER)
- `POST /api/bookings/{bookingId}/cancel-by-officer` (OFFICER)

### Payment
- `POST /api/payments/{bookingId}/online` (CUSTOMER)
- `GET /api/payments/{bookingId}`

### Invoice / Receipt
- `GET /api/invoices/{bookingId}` (PDF)
- `GET /api/invoices/{bookingId}/receipt` (PDF)

### Feedback
- `POST /api/feedback/{bookingId}` (CUSTOMER, only DELIVERED)
- `GET /api/feedback?page=0&size=10` (OFFICER)

### Export
- `GET /api/export/my-bookings` (CUSTOMER, CSV)
- `GET /api/export/all-bookings` (OFFICER, CSV)

## Auth header
For every secured request:
```
Authorization: Bearer <token>
```
