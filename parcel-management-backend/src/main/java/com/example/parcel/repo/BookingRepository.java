package com.example.parcel.repo;

import com.example.parcel.entity.Booking;
import com.example.parcel.entity.BookingStatus;
import java.time.Instant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {
  Booking findByBookingId(String bookingId);

  Page<Booking> findByCustomerId(Long customerId, Pageable pageable);

  Page<Booking> findByCustomerIdAndStatus(Long customerId, BookingStatus status, Pageable pageable);

  Page<Booking> findByCustomerIdAndCreatedAtBetween(Long customerId, Instant from, Instant to, Pageable pageable);

  Page<Booking> findByStatus(BookingStatus status, Pageable pageable);
}
