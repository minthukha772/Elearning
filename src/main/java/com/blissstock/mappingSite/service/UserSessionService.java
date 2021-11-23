package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.enums.UserRole;

public interface UserSessionService {
    public UserRole getRole();
    public String getEmail();
    public boolean isAuthenticated();

}
