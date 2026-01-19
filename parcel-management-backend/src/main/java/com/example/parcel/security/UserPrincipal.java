package com.example.parcel.security;

import com.example.parcel.entity.AppUser;
import com.example.parcel.entity.Role;
import java.util.*;
import org.springframework.security.core.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserPrincipal implements UserDetails {
  private final AppUser user;

  public UserPrincipal(AppUser user) {
    this.user = user;
  }

  public AppUser getUser() { return user; }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()));
  }

  @Override
  public String getPassword() { return user.getPasswordHash(); }

  @Override
  public String getUsername() { return user.getEmail(); }

  public Role getRole() { return user.getRole(); }

  public Long getUserId() { return user.getId(); }

  @Override
  public boolean isAccountNonExpired() { return true; }
  @Override
  public boolean isAccountNonLocked() { return true; }
  @Override
  public boolean isCredentialsNonExpired() { return true; }
  @Override
  public boolean isEnabled() { return true; }
}
