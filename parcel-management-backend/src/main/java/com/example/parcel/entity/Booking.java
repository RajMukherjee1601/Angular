package com.example.parcel.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "bookings")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true)
  private String bookingId;

  @ManyToOne(optional = false)
  private AppUser customer;

  @Column(nullable = false)
  private boolean createdByOfficer;

  // Sender
  @Column(nullable = false)
  private String senderName;
  @Column(nullable = false)
  private String senderAddress;
  @Column(nullable = false)
  private String senderPhone;

  // Receiver
  @Column(nullable = false)
  private String receiverName;
  @Column(nullable = false)
  private String receiverAddress;
  @Column(nullable = false)
  private String receiverPhone;

  @Column(nullable = false)
  private String parcelType; // e.g., DOCUMENT, BOX

  @Column(nullable = false)
  private String contents;

  @Column(nullable = false)
  private double weightKg;

  @Column(nullable = false)
  private double distanceKm;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal baseCost;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal adminFee;

  @Column(nullable = false, precision = 12, scale = 2)
  private BigDecimal totalCost;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private BookingStatus status;

  private Instant pickupTime;
  private Instant dropTime;

  private String cancelReason;
  private Instant cancelledAt;

  @Column(nullable = false)
  private Instant createdAt;

  @Column(nullable = false)
  private Instant updatedAt;

  protected Booking() {}

  public Booking(
      String bookingId,
      AppUser customer,
      boolean createdByOfficer,
      String senderName,
      String senderAddress,
      String senderPhone,
      String receiverName,
      String receiverAddress,
      String receiverPhone,
      String parcelType,
      String contents,
      double weightKg,
      double distanceKm,
      BigDecimal baseCost,
      BigDecimal adminFee,
      BigDecimal totalCost,
      BookingStatus status
  ) {
    this.bookingId = bookingId;
    this.customer = customer;
    this.createdByOfficer = createdByOfficer;
    this.senderName = senderName;
    this.senderAddress = senderAddress;
    this.senderPhone = senderPhone;
    this.receiverName = receiverName;
    this.receiverAddress = receiverAddress;
    this.receiverPhone = receiverPhone;
    this.parcelType = parcelType;
    this.contents = contents;
    this.weightKg = weightKg;
    this.distanceKm = distanceKm;
    this.baseCost = baseCost;
    this.adminFee = adminFee;
    this.totalCost = totalCost;
    this.status = status;
    this.createdAt = Instant.now();
    this.updatedAt = Instant.now();
  }

  @PreUpdate
  public void preUpdate() {
    this.updatedAt = Instant.now();
  }

  public Long getId() { return id; }
  public String getBookingId() { return bookingId; }
  public AppUser getCustomer() { return customer; }
  public boolean isCreatedByOfficer() { return createdByOfficer; }

  public String getSenderName() { return senderName; }
  public String getSenderAddress() { return senderAddress; }
  public String getSenderPhone() { return senderPhone; }
  public String getReceiverName() { return receiverName; }
  public String getReceiverAddress() { return receiverAddress; }
  public String getReceiverPhone() { return receiverPhone; }

  public String getParcelType() { return parcelType; }
  public String getContents() { return contents; }
  public double getWeightKg() { return weightKg; }
  public double getDistanceKm() { return distanceKm; }
  public BigDecimal getBaseCost() { return baseCost; }
  public BigDecimal getAdminFee() { return adminFee; }
  public BigDecimal getTotalCost() { return totalCost; }

  public BookingStatus getStatus() { return status; }
  public Instant getPickupTime() { return pickupTime; }
  public Instant getDropTime() { return dropTime; }

  public String getCancelReason() { return cancelReason; }
  public Instant getCancelledAt() { return cancelledAt; }

  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }

  public void setStatus(BookingStatus status) { this.status = status; }
  public void setPickupTime(Instant pickupTime) { this.pickupTime = pickupTime; }
  public void setDropTime(Instant dropTime) { this.dropTime = dropTime; }

  public void cancel(String reason) {
    this.status = BookingStatus.CANCELLED;
    this.cancelReason = reason;
    this.cancelledAt = Instant.now();
  }
}
