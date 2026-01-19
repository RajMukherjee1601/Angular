package com.example.parcel.util;

import java.security.SecureRandom;

public class BookingIdGenerator {
  private static final SecureRandom RAND = new SecureRandom();

  public static String nextId() {
    // Example: BK-20260119-483920
    String date = java.time.LocalDate.now().toString().replace("-", "");
    int suffix = 100000 + RAND.nextInt(900000);
    return "BK-" + date + "-" + suffix;
  }
}
