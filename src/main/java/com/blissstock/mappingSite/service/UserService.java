package com.blissstock.mappingSite.service;

import java.util.Optional;

import com.blissstock.mappingSite.dto.PasswordDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.exceptions.UserNotFoundException;

public interface UserService {
  public UserInfo addUser(UserRegisterDTO userRegisterDTO) throws Exception;

  public void updateUser(UserRegisterDTO userRegisterDTO, Long id);

  public UserAccount getUserAccountByEmail(String email);

  public UserInfo getUserInfoByID(Long id);

  public boolean isUserAccountPresent(String email);

  public UserAccount getUserAccountByToken(String verificationToken, String tokenType);

  void createToken(UserAccount userAccount, String token, TokenType tokenType);

  Token getToken(String VerificationToken, TokenType tokenType);

  public void updateUserAccount(UserAccount savedUserAccount);

  public void updateToken(Token savedToken);

  public void updatePassword(PasswordDTO passwordDTO, Long id);

  public void setAsUsedToken(String token);

}
