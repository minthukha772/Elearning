package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;

public interface UserService {
  public void addUser(UserRegisterDTO userRegisterDTO) throws Exception;
  public UserRegisterDTO getUserByID(Long id);
  public UserAccount getUserAccountByEmail(String email);
  public boolean isUserAccountPresent(String email);
}
