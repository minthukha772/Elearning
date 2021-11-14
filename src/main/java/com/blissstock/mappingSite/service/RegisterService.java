package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;

public class RegisterService {
  @Autowired
  private UserAccountRepository userAccountRepository;

  @Autowired
  private UserInfoRepository userInfoRepository;

  public void addUser(UserRegisterDTO userRegisterDTO){
    UserInfo userInfo = UserInfo.fromRegisterDTO(userRegisterDTO);
    UserAccount userAccount = UserAccount.fromRegisterDTO(userRegisterDTO);

    userAccountRepository.save(userAccount);
    userInfoRepository.save(userInfo);
    
    //userAccountRepository.save(entity);
  }


}
