package com.example.parcel.service;

import com.example.parcel.dto.AuthDtos;
import com.example.parcel.entity.*;
import com.example.parcel.repo.UserRepository;
import com.example.parcel.security.JwtService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtService jwtService;

  public AuthService(UserRepository userRepository,
                     PasswordEncoder passwordEncoder,
                     AuthenticationManager authenticationManager,
                     JwtService jwtService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.authenticationManager = authenticationManager;
    this.jwtService = jwtService;
  }

  public void register(AuthDtos.RegisterRequest req) {
    if (userRepository.existsByEmail(req.email())) {
      throw new ApiException(HttpStatus.CONFLICT, "Email already registered");
    }

    Role role = (req.role() == null) ? Role.CUSTOMER : req.role();
    if (role == Role.OFFICER) {
      // Keep it safe for now - create OFFICER through seeding.
      throw new ApiException(HttpStatus.BAD_REQUEST, "Officer accounts are created by admin");
    }

    String hash = passwordEncoder.encode(req.password());
    userRepository.save(new AppUser(req.fullName(), req.email(), hash, role));
  }

  public AuthDtos.AuthResponse login(AuthDtos.LoginRequest req) {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(req.email(), req.password())
      );
    } catch (BadCredentialsException ex) {
      throw new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
    }

    AppUser user = userRepository.findByEmail(req.email())
        .orElseThrow(() -> new ApiException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));

    String token = jwtService.generateToken(user.getEmail(), Map.of(
        "role", user.getRole().name(),
        "fullName", user.getFullName(),
        "uid", user.getId()
    ));

    return new AuthDtos.AuthResponse(token, user.getFullName(), user.getEmail(), user.getRole());
  }
}
