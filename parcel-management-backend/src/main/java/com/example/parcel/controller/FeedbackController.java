package com.example.parcel.controller;

import com.example.parcel.dto.FeedbackDtos;
import com.example.parcel.entity.Feedback;
import com.example.parcel.security.UserPrincipal;
import com.example.parcel.service.FeedbackService;
import jakarta.validation.Valid;
import org.springframework.data.domain.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/feedback")
public class FeedbackController {

  private final FeedbackService feedbackService;

  public FeedbackController(FeedbackService feedbackService) {
    this.feedbackService = feedbackService;
  }

  @PostMapping("/{bookingId}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public FeedbackDtos.FeedbackResponse addFeedback(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId,
      @Valid @RequestBody FeedbackDtos.FeedbackRequest req
  ) {
    Feedback fb = feedbackService.addFeedback(principal.getUser(), bookingId, req);
    return toResponse(fb);
  }

  @GetMapping
  @PreAuthorize("hasRole('OFFICER')")
  public Page<FeedbackDtos.FeedbackResponse> listFeedback(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
    return feedbackService.listAll(pageable).map(this::toResponse);
  }

  private FeedbackDtos.FeedbackResponse toResponse(Feedback fb) {
    return new FeedbackDtos.FeedbackResponse(
        fb.getBooking().getBookingId(),
        fb.getCustomer().getEmail(),
        fb.getRating(),
        fb.getComments(),
        fb.getCreatedAt()
    );
  }
}
