package com.example.parcel.service;

import com.example.parcel.dto.PaymentDtos;
import com.example.parcel.entity.*;
import com.example.parcel.repo.*;
import com.example.parcel.util.CardValidator;
import java.time.YearMonth;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentService {

  private final BookingRepository bookingRepository;
  private final PaymentRepository paymentRepository;

  public PaymentService(BookingRepository bookingRepository, PaymentRepository paymentRepository) {
    this.bookingRepository = bookingRepository;
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Payment payOnline(Long customerId, String bookingId, PaymentDtos.OnlinePaymentRequest req) {
    Booking booking = bookingRepository.findByBookingId(bookingId);
    if (booking == null) throw new ApiException(HttpStatus.NOT_FOUND, "Booking not found");

    if (!booking.getCustomer().getId().equals(customerId)) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can pay only for your own booking");
    }

    if (booking.getStatus() != BookingStatus.BOOKED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Payment allowed only when booking is BOOKED");
    }

    if (!CardValidator.isValidCardNumber(req.cardNumber())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Invalid card number");
    }

    // Expiry validation: MM/YY should be current month or later
    String[] parts = req.expiry().split("/");
    int mm = Integer.parseInt(parts[0]);
    int yy = Integer.parseInt(parts[1]) + 2000;
    YearMonth exp = YearMonth.of(yy, mm);
    if (exp.isBefore(YearMonth.now())) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Card is expired");
    }

    Payment payment = paymentRepository.findByBookingBookingId(bookingId)
        .orElseGet(() -> paymentRepository.save(new Payment(booking, PaymentMode.ONLINE, PaymentStatus.PENDING, booking.getTotalCost())));

    // Demo: Always succeed if validations pass
    String tx = "TX-" + UUID.randomUUID();
    payment.markPaid(tx, CardValidator.last4(req.cardNumber()), req.cardHolder());

    booking.setStatus(BookingStatus.PAID);

    return payment;
  }

  public Payment getPayment(String bookingId) {
    return paymentRepository.findByBookingBookingId(bookingId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Payment not found"));
  }
}
