package com.example.parcel.util;

/**
 * Basic card number validation (Luhn algorithm) for demo.
 * Never store full card numbers.
 */
public class CardValidator {

  public static boolean isValidCardNumber(String cardNumber) {
    String digits = cardNumber.replaceAll("\\s+", "");
    if (!digits.matches("\\d{13,19}")) return false;

    int sum = 0;
    boolean alternate = false;
    for (int i = digits.length() - 1; i >= 0; i--) {
      int n = digits.charAt(i) - '0';
      if (alternate) {
        n *= 2;
        if (n > 9) n -= 9;
      }
      sum += n;
      alternate = !alternate;
    }
    return sum % 10 == 0;
  }

  public static String last4(String cardNumber) {
    String digits = cardNumber.replaceAll("\\s+", "");
    if (digits.length() < 4) return "";
    return digits.substring(digits.length() - 4);
  }
}
