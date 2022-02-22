package com.blissstock.mappingSite.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.blissstock.mappingSite.config.GmailConfig;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.enums.TokenType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

@Service
public class MailServiceImpl implements MailService {

  private static final Logger logger = LoggerFactory.getLogger(
      MailServiceImpl.class);

  @Autowired
  @Qualifier("gmail")
  private JavaMailSender mailSender;

  @Autowired
  private UserService userService;

  @Autowired
  private SpringTemplateEngine templateEngine;

  @Autowired
  GmailConfig gmailConfig;
  /*
   * @Autowired
   * private MessageSource messages;
   */
  private String email = "sys@pyinnyar-subuu.com";
  private String fromName = "PyinnyarSubuu";

  public void sendMail(
      String subject,
      String toAddresses,
      String ccAddresses,
      String bccAddresses,
      String body) {
    MimeMessagePreparator preparator = mimeMessage -> {
      InternetAddress fromAddress = new InternetAddress(email, fromName);
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setTo(toAddresses.split("[,;]"));
      message.setFrom(fromAddress);
      message.setSubject(subject);
      if (!ccAddresses.isBlank())
        message.setCc(ccAddresses.split("[;,]"));
      if (!bccAddresses.isBlank())
        message.setBcc(bccAddresses.split("[;,]"));
      message.setText(body, false);
    };
    mailSender.send(preparator);
    logger.info(
        "Email sent successfully To {},{} with Subject {}",
        toAddresses,
        ccAddresses,
        subject);
  }

  public void sendVerificationMail(UserAccount userAccount, String appUrl) throws MessagingException {
    String token = UUID.randomUUID().toString();
    userService.createToken(userAccount, token, TokenType.VERIFICATION);

    String recipientAddress = userAccount.getMail();
    String subject = "Registration Confirmation";

    String confirmationUrl = appUrl + "/verify_password?token=" + token;
    // String message = "Congratulations, your account has been successfully
    // created. Please go to the following link to confirm the account";
    // String body = message + "\r\n" + confirmationUrl;
    // System.out.println(confirmationUrl.toString());
    // sendMail(subject, recipientAddress, "", "", body);

    // implementation

    final Context ctx = new Context();
    ctx.setVariable("confirmationUrl", confirmationUrl);
    ctx.setVariable("Date", new Date());
    ctx.setVariable("token", token);

    final MimeMessage mimeMessage = mailSender.createMimeMessage();
    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
    message.setSubject(subject);
    message.setFrom("mappingsite0@gmail.com");
    message.setTo(recipientAddress);

    final String htmlContent = templateEngine.process("sampleCss", ctx);
    message.setText(htmlContent, true); // true = isHtml

    this.mailSender.send(mimeMessage);
  }

  @Override
  public void sendResetPasswordMail(UserAccount userAccount, String appUrl) {
    String token = UUID.randomUUID().toString();
    userService.createToken(userAccount, token, TokenType.PASSWORD_RESET);

    String recipientAddress = userAccount.getMail();
    String subject = "Reset Password";

    String confirmationUrl = appUrl + "/password/verify_password?token=" + token;
    String message = "You have requested to reset password. Please click the following link to continue: ";
    String body = message + "\r\n" + confirmationUrl;
    System.out.println(confirmationUrl.toString());
    sendMail(subject, recipientAddress, "", "", body);

  }

  // test impl
  // public void sendMailWithInline(
  // final String recipientName, final String recipientEmail
  // )
  // throws MessagingException {

  // final Context ctx = new Context();
  // ctx.setVariable("name", "Sai Horm Kham");

  // // Prepare message using a Spring helper
  // final MimeMessage mimeMessage = mailSender.createMimeMessage();
  // final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true,
  // "UTF-8"); // true = multipart
  // message.setSubject("Example HTML email with inline image");
  // message.setFrom("mappingsite0@gmail.com");
  // message.setTo(recipientEmail);

  // final String htmlContent = templateEngine.process("simple-mail", ctx);
  // message.setText(htmlContent, true);

  // this.mailSender.send(mimeMessage);

  // }
}
