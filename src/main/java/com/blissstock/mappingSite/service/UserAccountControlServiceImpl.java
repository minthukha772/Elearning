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
    public void verifyUser(UserAccount userAccount) {
        logger.info("verify user {}", userAccount.getAccountId());
        userAccount.setAccountStatus(AccountStatus.VERIFIED.getValue());
        userService.updateUserAccount(userAccount);
    }

    @Override
    public void verifyUser(UserInfo userInfo) {
       verifyUser(userInfo.getUserAccount());
    }

    @Override
    public void suspendUser(UserAccount userAccount) {
        logger.info("suspend user {}", userAccount.getAccountId());
        userAccount.setAccountStatus(AccountStatus.SUSPENDED.getValue());
        userService.updateUserAccount(userAccount);

    }

    @Override
    public void suspendUser(UserInfo userInfo) {
       suspendUser(userInfo.getUserAccount());
    }

    @Override
    public void reactivateUser(UserAccount userAccount){
        logger.info("reactivate user {}", userAccount.getAccountId());
        if(userAccount.getRole().equals("ROLE_STUDENT"))
            userAccount.setAccountStatus(AccountStatus.REGISTERED.getValue());
        else
            userAccount.setAccountStatus(AccountStatus.VERIFIED.getValue());
        userService.updateUserAccount(userAccount);
    }

    @Override
    public void reactivateUser(UserInfo userInfo) {
        reactivateUser(userInfo.getUserAccount());
    }

    @Override
    public void deleteUser(UserAccount userAccount) {
       UserInfo userInfo = userInfoRepository.findById(userAccount.getAccountId()).get();
       deleteUser(userInfo);
    }

    @Override
    public void deleteUser(UserInfo userInfo) {
        logger.info("delete user {}", userInfo.getUid());
        userInfoRepository.delete(userInfo);
        userAccountRepository.delete(userInfo.getUserAccount());

    }

    @Override
    public void approveTeacher(UserAccount userAccount) {
        logger.info("approve [user] {}", userAccount.getAccountId());
        userAccount.setAccountStatus(AccountStatus.SUSPENDED.getValue());
        userService.updateUserAccount(userAccount);

    }

    @Override
    public void approveTeacher(UserInfo userInfo) {
        approveTeacher(userInfo.getUserAccount());

    }

   

}
