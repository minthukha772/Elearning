package com.blissstock.mappingSite.service;

import javax.mail.MessagingException;

import com.blissstock.mappingSite.entity.UserAccount;

public interface MailService {
  public void sendMail(
      String subject,
      String toAddresses,
      String ccAddresses,
      String bccAddresses,
      String body);

  public void sendVerificationMail(UserAccount userAccount, String appUrl) throws MessagingException;

  public void sendResetPasswordMail(UserAccount userAccount, String appUrl) throws MessagingException;

  public void SendAdminNewTeacher(String appUrl) throws MessagingException;
}
