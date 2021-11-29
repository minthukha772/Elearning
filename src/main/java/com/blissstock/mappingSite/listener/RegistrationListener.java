package com.blissstock.mappingSite.listener;

import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.enums.TokenType;
import com.blissstock.mappingSite.event.OnRegistrationCompleteEvent;
import com.blissstock.mappingSite.service.MailServiceImpl;
import com.blissstock.mappingSite.service.UserService;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

@Component
public class RegistrationListener
  implements ApplicationListener<OnRegistrationCompleteEvent> {

  @Autowired
  private UserService service;

  @Autowired
  private MessageSource messages;

  @Autowired
  private MailServiceImpl mailService;

  @Override
  public void onApplicationEvent(OnRegistrationCompleteEvent event) {
    this.confirmRegistration(event);
  }

  private void confirmRegistration(OnRegistrationCompleteEvent event) {
    System.out.println("Init Sending Mail");
    UserInfo userInfo = event.getUserInfo();
    String token = UUID.randomUUID().toString();
    service.createToken(
      userInfo.getUserAccount(),
      token,
      TokenType.VERIFICATION
    );

    String recipientAddress = userInfo.getUserAccount().getMail();
    String subject = "Registration Confirmation";
    String confirmationUrl =
      event.getAppUrl() + "/regitrationConfirm.html?token=" + token;
    String message = messages.getMessage(
      "message.regSucc",
      null,
      event.getLocale()
    );

    mailService.sendMail(
      subject,
      recipientAddress,
      "",
      "",
      message + "\r\n" + "http://localhost:8080" + confirmationUrl
    );
  }
}
