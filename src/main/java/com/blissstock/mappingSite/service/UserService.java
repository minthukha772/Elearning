package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.Token;

public interface UserService {
  public UserInfo addUser(UserRegisterDTO userRegisterDTO) throws Exception;
  public void updateUser(UserRegisterDTO userRegisterDTO);
  public UserRegisterDTO getUserByEmail(String email);
  public UserAccount getUserAccountByEmail(String email);
  public UserInfo getUserInfoByID(Long id);
  public boolean isUserAccountPresent(String email);
  public UserAccount getUserAccountByToken(String verificationToken);
  void createVerificationToken(UserAccount userAccount, String token);
  Token getVerificationToken(String VerificationToken);
}
