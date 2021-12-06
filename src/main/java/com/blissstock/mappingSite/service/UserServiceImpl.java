package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.exceptions.UserAlreadyExistException;
import com.blissstock.mappingSite.repository.TokenRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import java.util.GregorianCalendar;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private final UserAccountRepository userAccountRepository;

  @Autowired
  private final UserInfoRepository userInfoRepository;

  @Autowired
  private final TokenRepository tokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  public UserServiceImpl(
    UserAccountRepository userAccountRepository,
    UserInfoRepository userInfoRepository,
    TokenRepository tokenRepository
  ) {
    this.userAccountRepository = userAccountRepository;
    this.userInfoRepository = userInfoRepository;
    this.tokenRepository = tokenRepository;
    //validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public UserInfo addUser(UserRegisterDTO userRegisterDTO)
    throws UserAlreadyExistException {
    UserInfo userInfo = UserRegisterDTO.toUserInfo(userRegisterDTO);
    UserAccount userAccount = UserRegisterDTO.toUserAccount(
      userRegisterDTO,
      GregorianCalendar.getInstance().getTime()
    );
    //Encode Password
    userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

    if (this.isUserAccountPresent(userAccount.getMail())) {
      throw new UserAlreadyExistException();
    }

    /*  UserAccount savedUserAccount = userAccountRepository.save(userAccount);
    userInfo.setUserAccount(savedUserAccount); */
    userInfo.setUserAccount(userAccount);
    UserInfo savedUserInfo = userInfoRepository.save(userInfo);
    //userAccountRepository.save(entity);
    return savedUserInfo;
  }

  @Override
  public void updateUser(UserRegisterDTO userRegisterDTO) {
    UserInfo existingUserInfo = userInfoRepository.findUserInfoByEmail(
      userRegisterDTO.getEmail()
    );
    System.out.println(existingUserInfo);
    if (existingUserInfo != null) {
      existingUserInfo =
        UserRegisterDTO.toUserInfo(userRegisterDTO, existingUserInfo);
    }
    userInfoRepository.save(existingUserInfo);
  }

 
  @Override
  public UserAccount getUserAccountByEmail(String email) {
    return userAccountRepository.findByMail(email);
  }

  @Override
  public boolean isUserAccountPresent(String email) {
    return userAccountRepository.findByMail(email) != null;
  }

  @Override
  public void createToken(
    UserAccount userAccount,
    String token,
    TokenType tokenType
  ) {
    Token myToken = new Token(
      token,
      userAccount,
      tokenType,
      System.currentTimeMillis()
    );
    tokenRepository.save(myToken);
  }

  @Override
  public Token getToken(String token, TokenType tokenType) {
    //TODO fix bug
    return tokenRepository.getToken(token, tokenType.getValue());
  }

  @Override
  public UserAccount getUserAccountByToken(String verificationToken) {
    return null;
    //TODo fix bug
    /* UserAccount userAccount = tokenRepository
      .findByToken(verificationToken)
      .getUserAccount(); */
    //return userAccount;
  }

  @Override
  public UserInfo getUserInfoByID(Long id) {
    return userInfoRepository.findById(id).get();
  }

  @Override
  public void updateUserAccount(UserAccount savedUserAccount) {
    userAccountRepository.save(savedUserAccount);
  }

  @Override
  public void updateToken(Token savedToken) {
    tokenRepository.save(savedToken);
  }
}
