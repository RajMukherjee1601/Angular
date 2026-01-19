package com.example.parcel.dto;

import com.example.parcel.entity.Role;
import jakarta.validation.constraints.*;

public class AuthDtos {
  public record RegisterRequest(
      @NotBlank String fullName,
      @Email @NotBlank String email,
      @Size(min = 6, message = "Password must be at least 6 characters") String password,
      // default to CUSTOMER; allow OFFICER creation from data seeding / future admin UI
      Role role
  ) {}

  public record LoginRequest(
      @Email @NotBlank String email,
      @NotBlank String password
  ) {}

  public record AuthResponse(
      String token,
      String fullName,
      String email,
      Role role
  ) {}
}
