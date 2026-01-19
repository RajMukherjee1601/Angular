package com.example.parcel.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "payments")
public class Payment {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(optional = false)
  private Booking booking;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentMode mode;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private PaymentStatus status;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal amount;

  private String transactionId;
  private String cardLast4;
  private String cardHolder;

  private Instant paidAt;
  private Instant refundedAt;

  protected Payment() {}

  public Payment(Booking booking, PaymentMode mode, PaymentStatus status, BigDecimal amount) {
    this.booking = booking;
    this.mode = mode;
    this.status = status;
    this.amount = amount;
  }

  public Long getId() { return id; }
  public Booking getBooking() { return booking; }
  public PaymentMode getMode() { return mode; }
  public PaymentStatus getStatus() { return status; }
  public BigDecimal getAmount() { return amount; }
  public String getTransactionId() { return transactionId; }
  public String getCardLast4() { return cardLast4; }
  public String getCardHolder() { return cardHolder; }
  public Instant getPaidAt() { return paidAt; }
  public Instant getRefundedAt() { return refundedAt; }

  public void markPaid(String transactionId, String cardLast4, String cardHolder) {
    this.status = PaymentStatus.PAID;
    this.transactionId = transactionId;
    this.cardLast4 = cardLast4;
    this.cardHolder = cardHolder;
    this.paidAt = Instant.now();
  }

  public void markFailed(String transactionId) {
    this.status = PaymentStatus.FAILED;
    this.transactionId = transactionId;
  }

  public void markRefunded(String refundTransactionId) {
    this.status = PaymentStatus.REFUNDED;
    this.transactionId = refundTransactionId;
    this.refundedAt = Instant.now();
  }
}
