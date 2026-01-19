package com.example.parcel.controller;

import com.example.parcel.entity.*;
import com.example.parcel.repo.PaymentRepository;
import com.example.parcel.security.UserPrincipal;
import com.example.parcel.service.*;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {

  private final BookingService bookingService;
  private final PaymentRepository paymentRepository;
  private final PdfService pdfService;

  public InvoiceController(BookingService bookingService, PaymentRepository paymentRepository, PdfService pdfService) {
    this.bookingService = bookingService;
    this.paymentRepository = paymentRepository;
    this.pdfService = pdfService;
  }

  @GetMapping(value = "/{bookingId}", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> downloadInvoice(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId
  ) {
    Booking b = bookingService.getByBookingId(bookingId);

    // Customer can download only their invoice
    if (principal.getRole() == Role.CUSTOMER && !b.getCustomer().getId().equals(principal.getUserId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can download only your invoices");
    }

    Payment p = paymentRepository.findByBookingBookingId(bookingId).orElse(null);
    byte[] pdf = pdfService.buildInvoicePdf(b, p);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=invoice-" + bookingId + ".pdf")
        .body(pdf);
  }

  @GetMapping(value = "/{bookingId}/receipt", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> downloadReceipt(
      @AuthenticationPrincipal UserPrincipal principal,
      @PathVariable String bookingId
  ) {
    Booking b = bookingService.getByBookingId(bookingId);

    if (principal.getRole() == Role.CUSTOMER && !b.getCustomer().getId().equals(principal.getUserId())) {
      throw new ApiException(HttpStatus.FORBIDDEN, "You can download only your receipts");
    }

    Payment p = paymentRepository.findByBookingBookingId(bookingId)
        .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "Payment not found"));

    byte[] pdf = pdfService.buildReceiptPdf(b, p);

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=receipt-" + bookingId + ".pdf")
        .body(pdf);
  }
}
