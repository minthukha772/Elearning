package com.blissstock.mappingSite.service;

import java.util.GregorianCalendar;

import javax.transaction.Transactional;

import com.blissstock.mappingSite.dto.PasswordDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.Token;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.exceptions.UserAlreadyExistException;
import com.blissstock.mappingSite.repository.TokenRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
<<<<<<< HEAD
import com.blissstock.mappingSite.repository.UserRepository;

import java.util.GregorianCalendar;
import java.util.Optional;
import java.util.UUID;
=======
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  private static final Logger logger = LoggerFactory.getLogger(
    UserServiceImpl.class
  );

  @Autowired
  UserRepository userRepo;
  @Autowired
  private final UserAccountRepository userAccountRepository;

  @Autowired
  private final UserInfoRepository userInfoRepository;

  @Autowired
  private final TokenRepository tokenRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private MailService mailService;


  public UserServiceImpl(
      UserAccountRepository userAccountRepository,
      UserInfoRepository userInfoRepository,
      TokenRepository tokenRepository) {
    this.userAccountRepository = userAccountRepository;
    this.userInfoRepository = userInfoRepository;
    this.tokenRepository = tokenRepository;
    // validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public UserInfo addUser(UserRegisterDTO userRegisterDTO)
      throws UserAlreadyExistException {
    UserInfo userInfo = UserRegisterDTO.toUserInfo(userRegisterDTO);
    UserAccount userAccount = UserRegisterDTO.toUserAccount(
        userRegisterDTO,
        GregorianCalendar.getInstance().getTime());
    // Encode Password
    userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

    if (this.isUserAccountPresent(userAccount.getMail())) {
      logger.warn("User with {} email already exists", userAccount.getMail());
      throw new UserAlreadyExistException();
    }

    /*
     * UserAccount savedUserAccount = userAccountRepository.save(userAccount);
     * userInfo.setUserAccount(savedUserAccount);
     */
    userInfo.setUserAccount(userAccount);
    UserInfo savedUserInfo = userInfoRepository.save(userInfo);
<<<<<<< HEAD
    // userAccountRepository.save(entity);
=======
    logger.info("User {}, has successfully register with email {}", userAccount.getAccountId(), userAccount.getMail());
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
    return savedUserInfo;
  }

  @Override
  public void updateUser(UserRegisterDTO userRegisterDTO, Long id) {
    UserInfo existingUserInfo = userInfoRepository.findById(id).get();
    System.out.println(existingUserInfo);
    System.out.println(existingUserInfo.getUserAccount());
    if (existingUserInfo != null) {
      existingUserInfo = UserRegisterDTO.toUserInfo(userRegisterDTO, existingUserInfo);
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
<<<<<<< HEAD
      UserAccount userAccount,
      String token,
      TokenType tokenType) {
    Token myToken = new Token(
        token,
        userAccount,
        tokenType,
        System.currentTimeMillis());
=======
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
    logger.info("token {} created",myToken);
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
    tokenRepository.save(myToken);
  }

  @Override
  public Token getToken(String token, TokenType tokenType) {
<<<<<<< HEAD
    System.out.println("get token called");
    // TODO fix bug
    Token token1 = tokenRepository.getToken(token, tokenType.getValue());

    System.out.println("token is " + token1.toString());
    return token1;
=======
    // TODO fix bug
    return tokenRepository.getToken(token, tokenType.getValue());
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
  }

  @Override
  public UserAccount getUserAccountByToken(String verificationToken) {
    try {
      System.out.println("get account by token called");
      Long uid = tokenRepository.findByToken(verificationToken, "VERIFICATION");

      System.out.println("id is " + uid);

      if (uid == null) {
        System.out.println("uid is null");
        return null;
      }

      UserInfo userInfo = userRepo.findById(uid).orElse(null);
      UserAccount userAccount = userInfo.getUserAccount();

      return userAccount;

    } catch (Exception e) {
      System.out.println(e.toString());
    }
    return null;
<<<<<<< HEAD
=======
    // TODo fix bug
    /*
     * UserAccount userAccount = tokenRepository
     * .findByToken(verificationToken)
     * .getUserAccount();
     */
    // return userAccount;
>>>>>>> e3ddc02530232fdac29a31f539222426c8a3c104
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

  @Override
  public void updatePassword(PasswordDTO passwordDTO, Long id) {
    UserAccount userAccount = userAccountRepository.findById(id).get();
    String newPassword = passwordEncoder.encode(passwordDTO.getPassword());
    userAccount.setPassword(newPassword);
    userAccountRepository.save(userAccount);

  }

}
