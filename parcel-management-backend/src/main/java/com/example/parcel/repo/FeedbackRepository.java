package com.example.parcel.repo;

import com.example.parcel.entity.Feedback;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
  boolean existsByBookingBookingId(String bookingId);
  Optional<Feedback> findByBookingBookingId(String bookingId);
  Page<Feedback> findAll(Pageable pageable);
}
