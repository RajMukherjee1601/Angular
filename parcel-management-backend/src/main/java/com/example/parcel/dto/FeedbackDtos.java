package com.example.parcel.dto;

import jakarta.validation.constraints.*;
import java.time.Instant;

public class FeedbackDtos {
  public record FeedbackRequest(
      @Min(1) @Max(5) int rating,
      @NotBlank @Size(max = 1000) String comments
  ) {}

  public record FeedbackResponse(
      String bookingId,
      String customerEmail,
      int rating,
      String comments,
      Instant createdAt
  ) {}
}
