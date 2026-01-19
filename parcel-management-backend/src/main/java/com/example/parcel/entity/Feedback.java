package com.example.parcel.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "feedback")
public class Feedback {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false)
  private Booking booking;

  @ManyToOne(optional = false)
  private AppUser customer;

  @Column(nullable = false)
  private int rating; // 1..5

  @Column(nullable = false, length = 1000)
  private String comments;

  @Column(nullable = false)
  private Instant createdAt;

  protected Feedback() {}

  public Feedback(Booking booking, AppUser customer, int rating, String comments) {
    this.booking = booking;
    this.customer = customer;
    this.rating = rating;
    this.comments = comments;
    this.createdAt = Instant.now();
  }

  public Long getId() { return id; }
  public Booking getBooking() { return booking; }
  public AppUser getCustomer() { return customer; }
  public int getRating() { return rating; }
  public String getComments() { return comments; }
  public Instant getCreatedAt() { return createdAt; }
}
