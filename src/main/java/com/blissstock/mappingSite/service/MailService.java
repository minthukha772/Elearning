package com.blissstock.mappingSite.service;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;

public interface MailService {
  public void sendMail(
    String subject,
    String toAddresses,
    String ccAddresses,
    String bccAddresses,
    String body
  );

  public void sendVerificationMail(UserAccount userAccount, String appUrl);
  public void sendResetPasswordMail(UserAccount userAccount, String appUrl);
}
