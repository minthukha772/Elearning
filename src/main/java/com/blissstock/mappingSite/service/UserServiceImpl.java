package com.blissstock.mappingSite.service;

import javax.transaction.Transactional;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  public void addUser(UserRegisterDTO userRegisterDTO) {
    UserInfo userInfo = UserInfo.fromRegisterDTO(userRegisterDTO);
    UserAccount userAccount = UserAccount.fromRegisterDTO(userRegisterDTO);

    System.out.println(userAccount);

    UserAccount savedUserAccount = userAccountRepository.save(userAccount);

    userInfo.setUserAccount(savedUserAccount);

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    validator.validate(userInfo).forEach(e -> System.out.println(e.getMessage()));

    userInfoRepository.save(userInfo);
    //userAccountRepository.save(entity);
  }

  @Override
  public UserRegisterDTO getUserByID(Long id) {
    //TODO to Implement
    return new UserRegisterDTO();
  }
}
