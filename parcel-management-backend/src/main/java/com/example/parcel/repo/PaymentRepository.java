package com.example.parcel.repo;

import com.example.parcel.entity.Payment;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
  Optional<Payment> findByBookingBookingId(String bookingId);
}
