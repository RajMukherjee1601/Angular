package com.example.parcel.controller;

import com.example.parcel.entity.*;
import com.example.parcel.security.UserPrincipal;
import com.example.parcel.service.BookingService;
import java.nio.charset.StandardCharsets;
import org.springframework.data.domain.*;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
public class ExportController {

  private final BookingService bookingService;

  public ExportController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping(value = "/my-bookings", produces = "text/csv")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<byte[]> exportMyBookings(@AuthenticationPrincipal UserPrincipal principal) {
    var page = bookingService.listCustomerBookings(principal.getUserId(), null, null, null,
        PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "createdAt")));

    StringBuilder sb = new StringBuilder();
    sb.append("bookingId,status,totalCost,createdAt\n");
    for (Booking b : page.getContent()) {
      sb.append(b.getBookingId()).append(',')
          .append(b.getStatus()).append(',')
          .append(b.getTotalCost()).append(',')
          .append(b.getCreatedAt()).append('\n');
    }

    byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=my-bookings.csv")
        .body(bytes);
  }

  @GetMapping(value = "/all-bookings", produces = "text/csv")
  @PreAuthorize("hasRole('OFFICER')")
  public ResponseEntity<byte[]> exportAllBookings() {
    var page = bookingService.listAllBookings(null,
        PageRequest.of(0, 10000, Sort.by(Sort.Direction.DESC, "createdAt")));

    StringBuilder sb = new StringBuilder();
    sb.append("bookingId,customerEmail,status,totalCost,createdAt\n");
    for (Booking b : page.getContent()) {
      sb.append(b.getBookingId()).append(',')
          .append(b.getCustomer().getEmail()).append(',')
          .append(b.getStatus()).append(',')
          .append(b.getTotalCost()).append(',')
          .append(b.getCreatedAt()).append('\n');
    }

    byte[] bytes = sb.toString().getBytes(StandardCharsets.UTF_8);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=all-bookings.csv")
        .body(bytes);
  }
}
