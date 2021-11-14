package com.blissstock.mappingSite.service;

import java.util.Set;

import javax.transaction.Transactional;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.VerificationToken;
import com.blissstock.mappingSite.exceptions.UserAlreadyExistException;
import com.blissstock.mappingSite.repository.TokenRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;

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

  private Validator validator;

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
    this.validator = Validation.buildDefaultValidatorFactory().getValidator();
  }

  public UserAccount addUser(UserRegisterDTO userRegisterDTO) throws UserAlreadyExistException {
    UserInfo userInfo = UserInfo.fromRegisterDTO(userRegisterDTO);
    UserAccount userAccount = UserAccount.fromRegisterDTO(userRegisterDTO);
    //Encode Password
    userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));

    if(this.isUserAccountPresent(userAccount.getMail())){
      throw new UserAlreadyExistException();
    }

    UserAccount savedUserAccount = userAccountRepository.save(userAccount);
    userInfo.setUserAccount(savedUserAccount);
    userInfoRepository.save(userInfo);
    //userAccountRepository.save(entity);
    return savedUserAccount;
  }

  @Override
  public UserRegisterDTO getUserByID(Long id) {
    //TODO to Implement
    return new UserRegisterDTO();
  }

  @Override
  public UserAccount getUserAccountByEmail(String email) {

    return userAccountRepository.findById(email).get();
  }

  @Override
  public boolean isUserAccountPresent(String email) {
    return userAccountRepository.findById(email).isPresent();
  }

  @Override
  public void createVerificationToken(UserAccount userAccount, String token) {
    VerificationToken myToken = new VerificationToken(token, userAccount);
    tokenRepository.save(myToken);
    
  }

  @Override
  public VerificationToken getVerificationToken(String VerificationToken) {
    return tokenRepository.findByToken(VerificationToken);
  }

  @Override
  public UserAccount getUserAccountByToken(String verificationToken) {
    UserAccount userAccount = tokenRepository.findByToken(verificationToken).getUserAccount();
    return userAccount;
  }
}
