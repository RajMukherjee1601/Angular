package com.example.parcel.dto;

import com.example.parcel.entity.BookingStatus;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.Instant;

public class BookingDtos {

  public record BookingCreateRequest(
      @NotBlank String senderName,
      @NotBlank String senderAddress,
      @NotBlank String senderPhone,
      @NotBlank String receiverName,
      @NotBlank String receiverAddress,
      @NotBlank String receiverPhone,
      @NotBlank String parcelType,
      @NotBlank String contents,
      @DecimalMin(value = "0.1") double weightKg,
      @DecimalMin(value = "0.1") double distanceKm
  ) {}

  public record BookingResponse(
      String bookingId,
      String customerEmail,
      boolean createdByOfficer,
      String senderName,
      String receiverName,
      String parcelType,
      double weightKg,
      double distanceKm,
      BigDecimal baseCost,
      BigDecimal adminFee,
      BigDecimal totalCost,
      BookingStatus status,
      Instant pickupTime,
      Instant dropTime,
      Instant createdAt,
      Instant updatedAt
  ) {}

  public record UpdatePickupDropRequest(
      Instant pickupTime,
      Instant dropTime
  ) {}

  public record UpdateStatusRequest(
      @NotNull BookingStatus newStatus
  ) {}

  public record CancelRequest(
      @NotBlank String reason
  ) {}
}
