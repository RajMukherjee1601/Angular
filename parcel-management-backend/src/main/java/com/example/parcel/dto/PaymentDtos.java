package com.example.parcel.dto;

import com.example.parcel.entity.PaymentMode;
import com.example.parcel.entity.PaymentStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public class PaymentDtos {

  public record OnlinePaymentRequest(
      @NotBlank String cardNumber,
      @NotBlank String cardHolder,
      @Pattern(regexp = "^(0[1-9]|1[0-2])\\/\\d{2}$", message = "Expiry must be in MM/YY") String expiry,
      @Pattern(regexp = "^\\d{3}$", message = "CVV must be 3 digits") String cvv
  ) {}

  public record PaymentResponse(
      String bookingId,
      PaymentMode mode,
      PaymentStatus status,
      BigDecimal amount,
      String transactionId,
      String cardLast4,
      String cardHolder,
      Instant paidAt,
      Instant refundedAt
  ) {}
}
