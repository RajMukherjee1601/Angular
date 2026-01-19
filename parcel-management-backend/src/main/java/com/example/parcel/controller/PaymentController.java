package com.example.parcel.controller;

import com.example.parcel.dto.PaymentDtos;
import com.example.parcel.entity.Payment;
import com.example.parcel.security.UserPrincipal;
import com.example.parcel.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

  private final PaymentService paymentService;

  public PaymentController(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  @PostMapping("/{bookingId}/online")
  @PreAuthorize("hasRole('CUSTOMER')")
  public PaymentDtos.PaymentResponse payOnline(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId,
      @Valid @RequestBody PaymentDtos.OnlinePaymentRequest req
  ) {
    Payment p = paymentService.payOnline(principal.getUserId(), bookingId, req);
    return toResponse(p);
  }

  @GetMapping("/{bookingId}")
  public PaymentDtos.PaymentResponse getPayment(@PathVariable String bookingId) {
    return toResponse(paymentService.getPayment(bookingId));
  }

  private PaymentDtos.PaymentResponse toResponse(Payment p) {
    return new PaymentDtos.PaymentResponse(
        p.getBooking().getBookingId(),
        p.getMode(),
        p.getStatus(),
        p.getAmount(),
        p.getTransactionId(),
        p.getCardLast4(),
        p.getCardHolder(),
        p.getPaidAt(),
        p.getRefundedAt()
    );
  }
}
