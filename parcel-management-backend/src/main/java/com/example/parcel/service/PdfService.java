package com.example.parcel.service;

import com.example.parcel.entity.*;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import java.io.ByteArrayOutputStream;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

@Service
public class PdfService {

  private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm")
      .withZone(ZoneId.systemDefault());

  public byte[] buildInvoicePdf(Booking booking, Payment payment) {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4);

    try {
      PdfWriter.getInstance(doc, out);
      doc.open();

      doc.add(new Paragraph("Parcel Invoice", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
      doc.add(new Paragraph("Booking ID: " + booking.getBookingId()));
      doc.add(new Paragraph("Customer: " + booking.getCustomer().getFullName() + " (" + booking.getCustomer().getEmail() + ")"));
      doc.add(new Paragraph("Created: " + DT.format(booking.getCreatedAt())));
      doc.add(Chunk.NEWLINE);

      doc.add(new Paragraph("Sender: " + booking.getSenderName() + ", " + booking.getSenderPhone()));
      doc.add(new Paragraph("Sender Address: " + booking.getSenderAddress()));
      doc.add(new Paragraph("Receiver: " + booking.getReceiverName() + ", " + booking.getReceiverPhone()));
      doc.add(new Paragraph("Receiver Address: " + booking.getReceiverAddress()));
      doc.add(Chunk.NEWLINE);

      doc.add(new Paragraph("Parcel Type: " + booking.getParcelType()));
      doc.add(new Paragraph("Contents: " + booking.getContents()));
      doc.add(new Paragraph(String.format("Weight: %.2f kg, Distance: %.2f km", booking.getWeightKg(), booking.getDistanceKm())));
      doc.add(Chunk.NEWLINE);

      doc.add(new Paragraph("Charges", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
      doc.add(new Paragraph("Base Cost: " + booking.getBaseCost()));
      doc.add(new Paragraph("Admin Fee: " + booking.getAdminFee()));
      doc.add(new Paragraph("Total: " + booking.getTotalCost(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
      doc.add(Chunk.NEWLINE);

      if (payment != null) {
        doc.add(new Paragraph("Payment Details", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
        doc.add(new Paragraph("Mode: " + payment.getMode()));
        doc.add(new Paragraph("Status: " + payment.getStatus()));
        doc.add(new Paragraph("Transaction ID: " + payment.getTransactionId()));
        if (payment.getPaidAt() != null) {
          doc.add(new Paragraph("Paid At: " + DT.format(payment.getPaidAt())));
        }
      }

      doc.add(Chunk.NEWLINE);
      doc.add(new Paragraph("Thank you for using Parcel Management.", FontFactory.getFont(FontFactory.HELVETICA, 10)));

      doc.close();
      return out.toByteArray();
    } catch (Exception ex) {
      throw new RuntimeException("PDF generation failed: " + ex.getMessage(), ex);
    }
  }

  public byte[] buildReceiptPdf(Booking booking, Payment payment) {
    // For demo, receipt is same format as invoice but different header
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document doc = new Document(PageSize.A4);

    try {
      PdfWriter.getInstance(doc, out);
      doc.open();

      doc.add(new Paragraph("Payment Receipt", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18)));
      doc.add(new Paragraph("Booking ID: " + booking.getBookingId()));
      doc.add(new Paragraph("Amount: " + payment.getAmount(), FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12)));
      doc.add(new Paragraph("Mode: " + payment.getMode()));
      doc.add(new Paragraph("Status: " + payment.getStatus()));
      doc.add(new Paragraph("Transaction ID: " + payment.getTransactionId()));
      if (payment.getPaidAt() != null) {
        doc.add(new Paragraph("Paid At: " + DT.format(payment.getPaidAt())));
      }
      doc.add(Chunk.NEWLINE);
      doc.add(new Paragraph("(Demo receipt)", FontFactory.getFont(FontFactory.HELVETICA, 10)));

      doc.close();
      return out.toByteArray();
    } catch (Exception ex) {
      throw new RuntimeException("PDF generation failed: " + ex.getMessage(), ex);
    }
  }
}
