package com.example.parcel.config;

import com.example.parcel.entity.*;
import com.example.parcel.repo.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

  /**
   * Creates a default OFFICER account for demo.
   * Email: officer@parcel.com
   * Password: officer123
   */
  @Bean
  CommandLineRunner seedOfficer(UserRepository userRepository, PasswordEncoder encoder) {
    return args -> {
      userRepository.findByEmail("officer@parcel.com").ifPresentOrElse(
          u -> {},
          () -> userRepository.save(new AppUser(
              "Default Officer",
              "officer@parcel.com",
              encoder.encode("officer123"),
              Role.OFFICER
          ))
      );
    };
  }
}
