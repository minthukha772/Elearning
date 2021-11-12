package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.dto.UserRegisterDTO;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
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

    userInfo.setUid(savedUserAccount.getAccountId());

    userInfoRepository.save(userInfo);
    //userAccountRepository.save(entity);
  }

  @Override
  public UserRegisterDTO getUserByID(Long id) {
    //TODO to Implement
    return new UserRegisterDTO();
  }
}
