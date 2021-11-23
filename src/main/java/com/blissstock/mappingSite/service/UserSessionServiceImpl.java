package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.enums.UserRole;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserSessionServiceImpl implements UserSessionService {

  @Override
  public UserRole getRole() {
    Authentication auth = SecurityContextHolder
      .getContext()
      .getAuthentication();
    System.out.println(auth.getAuthorities());
    List<UserRole> userRoles = auth
      .getAuthorities()
      .stream()
      .map(e -> UserRole.strToUserRole(e.getAuthority()))
      .collect(Collectors.toList());
    if (userRoles.isEmpty()) {
      return UserRole.GUEST_USER;
    }
    return userRoles.get(0);
  }

  @Override
  public String getEmail() {
    Authentication auth = SecurityContextHolder
      .getContext()
      .getAuthentication();
    return auth.getName();
  }

  @Override
  public boolean isAuthenticated() {
    Authentication auth = SecurityContextHolder
      .getContext()
      .getAuthentication();
    if (auth == null) return false;
    return auth.isAuthenticated();
  }
}
