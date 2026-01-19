package com.example.parcel.service;

import com.example.parcel.dto.BookingDtos;
import com.example.parcel.entity.*;
import com.example.parcel.repo.BookingRepository;
import com.example.parcel.repo.PaymentRepository;
import com.example.parcel.util.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookingService {

  private static final BigDecimal OFFICER_ADMIN_FEE = new BigDecimal("50.00");

  private final BookingRepository bookingRepository;
  private final PaymentRepository paymentRepository;

  public BookingService(BookingRepository bookingRepository, PaymentRepository paymentRepository) {
    this.bookingRepository = bookingRepository;
    this.paymentRepository = paymentRepository;
  }

  @Transactional
  public Booking createBookingForCustomer(AppUser customer, BookingDtos.BookingCreateRequest req) {
    BigDecimal base = CostCalculator.calculateBaseCost(req.weightKg(), req.distanceKm());
    BigDecimal admin = BigDecimal.ZERO;
    BigDecimal total = base.add(admin);

    Booking booking = new Booking(
        BookingIdGenerator.nextId(),
        customer,
        false,
        req.senderName(), req.senderAddress(), req.senderPhone(),
        req.receiverName(), req.receiverAddress(), req.receiverPhone(),
        req.parcelType(), req.contents(),
        req.weightKg(), req.distanceKm(),
        base, admin, total,
        BookingStatus.BOOKED
    );
    bookingRepository.save(booking);

    // Create a payment placeholder (online expected for customer)
    paymentRepository.save(new Payment(booking, PaymentMode.ONLINE, PaymentStatus.PENDING, total));
    return booking;
  }

  @Transactional
  public Booking createBookingByOfficer(AppUser officer, AppUser customer, BookingDtos.BookingCreateRequest req) {
    // Officer creates booking on behalf of a customer; includes admin fee and offline payment.
    if (officer.getRole() != Role.OFFICER) {
      throw new ApiException(HttpStatus.FORBIDDEN, "Only officer can create officer bookings");
    }

    BigDecimal base = CostCalculator.calculateBaseCost(req.weightKg(), req.distanceKm());
    BigDecimal admin = OFFICER_ADMIN_FEE;
    BigDecimal total = base.add(admin);

    Booking booking = new Booking(
        BookingIdGenerator.nextId(),
        customer,
        true,
        req.senderName(), req.senderAddress(), req.senderPhone(),
        req.receiverName(), req.receiverAddress(), req.receiverPhone(),
        req.parcelType(), req.contents(),
        req.weightKg(), req.distanceKm(),
        base, admin, total,
        BookingStatus.BOOKED
    );
    bookingRepository.save(booking);

    Payment p = new Payment(booking, PaymentMode.OFFLINE, PaymentStatus.PAID, total);
    p.markPaid("OFFLINE-" + System.currentTimeMillis(), null, "OFFLINE");
    paymentRepository.save(p);

    // Since officer collected offline payment, mark booking as PAID immediately
    booking.setStatus(BookingStatus.PAID);
    return booking;
  }

  public Booking getByBookingId(String bookingId) {
    Booking b = bookingRepository.findByBookingId(bookingId);
    if (b == null) throw new ApiException(HttpStatus.NOT_FOUND, "Booking not found");
    return b;
  }

  public Page<Booking> listCustomerBookings(Long customerId, BookingStatus status, Instant from, Instant to, Pageable pageable) {
    if (status != null) {
      return bookingRepository.findByCustomerIdAndStatus(customerId, status, pageable);
    }
    if (from != null && to != null) {
      return bookingRepository.findByCustomerIdAndCreatedAtBetween(customerId, from, to, pageable);
    }
    return bookingRepository.findByCustomerId(customerId, pageable);
  }

  public Page<Booking> listAllBookings(BookingStatus status, Pageable pageable) {
    if (status != null) {
      return bookingRepository.findByStatus(status, pageable);
    }
    return bookingRepository.findAll(pageable);
  }

  @Transactional
  public void updatePickupDrop(String bookingId, Instant pickupTime, Instant dropTime) {
    Booking b = getByBookingId(bookingId);
    b.setPickupTime(pickupTime);
    b.setDropTime(dropTime);
  }

  private static final Map<BookingStatus, Set<BookingStatus>> ALLOWED_TRANSITIONS = Map.of(
      BookingStatus.BOOKED, Set.of(BookingStatus.PAID, BookingStatus.CANCELLED),
      BookingStatus.PAID, Set.of(BookingStatus.PICKED_UP, BookingStatus.CANCELLED),
      BookingStatus.PICKED_UP, Set.of(BookingStatus.IN_TRANSIT),
      BookingStatus.IN_TRANSIT, Set.of(BookingStatus.DELIVERED),
      BookingStatus.DELIVERED, Set.of(),
      BookingStatus.CANCELLED, Set.of()
  );

  @Transactional
  public void updateStatus(String bookingId, BookingStatus newStatus) {
    Booking b = getByBookingId(bookingId);
    BookingStatus current = b.getStatus();
    if (!ALLOWED_TRANSITIONS.getOrDefault(current, Set.of()).contains(newStatus)) {
      throw new ApiException(HttpStatus.BAD_REQUEST,
          "Invalid status transition: " + current + " -> " + newStatus);
    }
    b.setStatus(newStatus);
  }

  @Transactional
  public void cancelByCustomer(AppUser customer, String bookingId, String reason) {
    Booking b = getByBookingId(bookingId);

    if (!b.getCustomer().getId().equals(customer.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can cancel only your own booking");
    }
    if (b.getStatus() != BookingStatus.BOOKED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Only BOOKED parcels can be cancelled by customer");
    }
    b.cancel(reason);
  }

  @Transactional
  public void cancelByOfficer(String bookingId, String reason) {
    Booking b = getByBookingId(bookingId);

    if (b.getStatus() == BookingStatus.DELIVERED || b.getStatus() == BookingStatus.CANCELLED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Cannot cancel a delivered/cancelled booking");
    }

    // Refund logic: if online payment was PAID, mark refund
    paymentRepository.findByBookingBookingId(bookingId).ifPresent(p -> {
      if (p.getMode() == PaymentMode.ONLINE && p.getStatus() == PaymentStatus.PAID) {
        p.markRefunded("REFUND-" + System.currentTimeMillis());
      }
    });

    b.cancel(reason);
  }
}
