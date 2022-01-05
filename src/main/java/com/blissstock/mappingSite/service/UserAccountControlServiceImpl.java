package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.AccountStatus;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.repository.UserInfoRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAccountControlServiceImpl implements UserAccountControlService {

    private static final Logger logger = LoggerFactory.getLogger(UserAccountControlServiceImpl.class);

    @Autowired
    UserService userService;

    @Autowired
    UserInfoRepository userInfoRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Override
    public void suspendUser(UserAccount userAccount) {
        logger.info("suspend user {}", userAccount.getId());
        userAccount.setAccountStatus(AccountStatus.SUSPENDED.getValue());
        userService.updateUserAccount(userAccount);

    }

    @Override
    public void suspendUser(UserInfo userInfo) {
       suspendUser(userInfo.getUserAccount());
    }

    @Override
    public void deleteUser(UserAccount userAccount) {
       userAccountRepository.delete(userAccount);
    }

    @Override
    public void deleteUser(UserInfo userInfo) {
        userInfoRepository.delete(userInfo);

    }

    @Override
    public void approveTeacher(UserAccount userAccount) {
        logger.info("approve [user] {}", userAccount.getId());
        userAccount.setAccountStatus(AccountStatus.SUSPENDED.getValue());
        userService.updateUserAccount(userAccount);

    }

    @Override
    public void approveTeacher(UserInfo userInfo) {
        approveTeacher(userInfo.getUserAccount());

    }

}
