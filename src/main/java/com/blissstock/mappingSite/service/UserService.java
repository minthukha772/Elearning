package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;

public interface UserService {
  public void addUser(UserRegisterDTO userRegisterDTO);
  public UserRegisterDTO getUserByID(Long id);
}
