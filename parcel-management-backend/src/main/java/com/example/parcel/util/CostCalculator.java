package com.example.parcel.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Simple, transparent pricing model (easy to explain to users).
 * You can replace with your real tariff later.
 */
public class CostCalculator {

  // Base charge + per kg + per km
  private static final BigDecimal BASE = new BigDecimal("30.00");
  private static final BigDecimal PER_KG = new BigDecimal("12.00");
  private static final BigDecimal PER_KM = new BigDecimal("2.00");

  public static BigDecimal calculateBaseCost(double weightKg, double distanceKm) {
    BigDecimal w = BigDecimal.valueOf(weightKg);
    BigDecimal d = BigDecimal.valueOf(distanceKm);

    BigDecimal cost = BASE
        .add(PER_KG.multiply(w))
        .add(PER_KM.multiply(d));

    return cost.setScale(2, RoundingMode.HALF_UP);
  }
}
