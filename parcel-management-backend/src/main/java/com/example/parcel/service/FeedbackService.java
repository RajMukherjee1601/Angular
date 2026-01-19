package com.example.parcel.service;

import com.example.parcel.dto.FeedbackDtos;
import com.example.parcel.entity.*;
import com.example.parcel.repo.*;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FeedbackService {

  private final BookingRepository bookingRepository;
  private final FeedbackRepository feedbackRepository;

  public FeedbackService(BookingRepository bookingRepository, FeedbackRepository feedbackRepository) {
    this.bookingRepository = bookingRepository;
    this.feedbackRepository = feedbackRepository;
  }

  @Transactional
  public Feedback addFeedback(AppUser customer, String bookingId, FeedbackDtos.FeedbackRequest req) {
    Booking booking = bookingRepository.findByBookingId(bookingId);
    if (booking == null) throw new ApiException(HttpStatus.NOT_FOUND, "Booking not found");

    if (!booking.getCustomer().getId().equals(customer.getId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can add feedback only for your own booking");
    }

    if (booking.getStatus() != BookingStatus.DELIVERED) {
      throw new ApiException(HttpStatus.BAD_REQUEST, "Feedback is allowed only after delivery");
    }

    if (feedbackRepository.existsByBookingBookingId(bookingId)) {
      throw new ApiException(HttpStatus.CONFLICT, "Feedback already submitted for this booking");
    }

    return feedbackRepository.save(new Feedback(booking, customer, req.rating(), req.comments()));
  }

  public Page<Feedback> listAll(Pageable pageable) {
    return feedbackRepository.findAll(pageable);
  }
}
