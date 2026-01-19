package com.example.parcel.controller;

import com.example.parcel.dto.BookingDtos;
import com.example.parcel.entity.*;
import com.example.parcel.repo.UserRepository;
import com.example.parcel.security.UserPrincipal;
import com.example.parcel.service.*;
import jakarta.validation.Valid;
import java.time.Instant;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {

  private final BookingService bookingService;
  private final UserRepository userRepository;

  public BookingController(BookingService bookingService, UserRepository userRepository) {
    this.bookingService = bookingService;
    this.userRepository = userRepository;
  }

  @PostMapping("/customer")
  @PreAuthorize("hasRole('CUSTOMER')")
  public BookingDtos.BookingResponse createCustomerBooking(
      @AuthenticationPrincipal UserPrincipal principal,
      @Valid @RequestBody BookingDtos.BookingCreateRequest req
  ) {
    AppUser customer = principal.getUser();
    Booking b = bookingService.createBookingForCustomer(customer, req);
    return toResponse(b);
  }

  @PostMapping("/officer/{customerEmail}")
  @PreAuthorize("hasRole('OFFICER')")
  public BookingDtos.BookingResponse createOfficerBooking(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String customerEmail,
      @Valid @RequestBody BookingDtos.BookingCreateRequest req
  ) {
    AppUser officer = principal.getUser();
    AppUser customer = userRepository.findByEmail(customerEmail)
        .orElseThrow(() -> new ApiException(org.springframework.http.HttpStatus.NOT_FOUND, "Customer not found"));

    Booking b = bookingService.createBookingByOfficer(officer, customer, req);
    return toResponse(b);
  }

  @GetMapping("/{bookingId}")
  public BookingDtos.BookingResponse getBooking(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId
  ) {
    Booking b = bookingService.getByBookingId(bookingId);

    // Tracking rules:
    if (principal.getRole() == Role.CUSTOMER && !b.getCustomer().getId().equals(principal.getUserId())) {
      throw new ApiException(org.springframework.http.HttpStatus.FORBIDDEN, "You can view only your bookings");
    }
    return toResponse(b);
  }

  @GetMapping("/my")
  @PreAuthorize("hasRole('CUSTOMER')")
  public Page<BookingDtos.BookingResponse> myBookings(
      @AuthenticationPrincipal UserPrincipal principal,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) BookingStatus status,
      @RequestParam(required = false) Instant from,
      @RequestParam(required = false) Instant to
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return bookingService.listCustomerBookings(principal.getUserId(), status, from, to, pageable)
        .map(this::toResponse);
  }

  @GetMapping
  @PreAuthorize("hasRole('OFFICER')")
  public Page<BookingDtos.BookingResponse> allBookings(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size,
      @RequestParam(required = false) BookingStatus status
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return bookingService.listAllBookings(status, pageable).map(this::toResponse);
  }

  @PutMapping("/{bookingId}/pickup-drop")
  @PreAuthorize("hasRole('OFFICER')")
  public java.util.Map<String, Object> updatePickupDrop(
      @PathVariable String bookingId,
      @RequestBody BookingDtos.UpdatePickupDropRequest req
  ) {
    bookingService.updatePickupDrop(bookingId, req.pickupTime(), req.dropTime());
    return java.util.Map.of("message", "Pickup/Drop updated");
  }

  @PutMapping("/{bookingId}/status")
  @PreAuthorize("hasRole('OFFICER')")
  public java.util.Map<String, Object> updateStatus(
      @PathVariable String bookingId,
      @Valid @RequestBody BookingDtos.UpdateStatusRequest req
  ) {
    bookingService.updateStatus(bookingId, req.newStatus());
    return java.util.Map.of("message", "Status updated");
  }

  @PostMapping("/{bookingId}/cancel")
  @PreAuthorize("hasRole('CUSTOMER')")
  public java.util.Map<String, Object> cancelCustomer(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId,
      @Valid @RequestBody BookingDtos.CancelRequest req
  ) {
    bookingService.cancelByCustomer(principal.getUser(), bookingId, req.reason());
    return java.util.Map.of("message", "Booking cancelled");
  }

  @PostMapping("/{bookingId}/cancel-by-officer")
  @PreAuthorize("hasRole('OFFICER')")
  public java.util.Map<String, Object> cancelOfficer(
      @PathVariable String bookingId,
      @Valid @RequestBody BookingDtos.CancelRequest req
  ) {
    bookingService.cancelByOfficer(bookingId, req.reason());
    return java.util.Map.of("message", "Booking cancelled by officer (refund if applicable)" );
  }

  private BookingDtos.BookingResponse toResponse(Booking b) {
    return new BookingDtos.BookingResponse(
        b.getBookingId(),
        b.getCustomer().getEmail(),
        b.isCreatedByOfficer(),
        b.getSenderName(),
        b.getReceiverName(),
        b.getParcelType(),
        b.getWeightKg(),
        b.getDistanceKm(),
        b.getBaseCost(),
        b.getAdminFee(),
        b.getTotalCost(),
        b.getStatus(),
        b.getPickupTime(),
        b.getDropTime(),
        b.getCreatedAt(),
        b.getUpdatedAt()
    );
  }
}
