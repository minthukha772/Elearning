package com.blissstock.mappingSite.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
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
  private String email = "mappingsite0@gmail.com";
  private String fromName = "Mapping Site";

  public void sendMail(
      String subject,
      String toAddresses,
      String ccAddresses,
      String bccAddresses,
      String body) {
    MimeMessagePreparator preparator = mimeMessage -> {
      MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
      message.setTo(toAddresses.split("[,;]"));
      message.setFrom(email, fromName);
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

  public void sendVerificationMail(UserAccount userAccount, String appUrl) {
    String token = UUID.randomUUID().toString();
    userService.createToken(userAccount, token, TokenType.VERIFICATION);

    String recipientAddress = userAccount.getMail();
    String subject = "Registration Confirmation";

    String confirmationUrl = appUrl + "/verify_password?token=" + token;
    String message = "Congratulations, your account has been successfully created. Please go to the following link to confirm the account";
    String body = message + "\r\n" + confirmationUrl;
    System.out.println(confirmationUrl.toString());
    sendMail(subject, recipientAddress, "", "", body);
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
  public void sendMailWithInline(
      final String recipientName, final String recipientEmail
  // final String imageResourceName,
  // final byte[] imageBytes, final String imageContentType, final Locale locale
  )
      throws MessagingException {

    // Prepare the evaluation context
    final Context ctx = new Context();
    // ctx.setVariable("name", recipientName);
    // ctx.setVariable("subscriptionDate", new Date());
    // ctx.setVariable("hobbies", Arrays.asList("Cinema", "Sports", "Music"));
    // ctx.setVariable("imageResourceName", imageResourceName);
    // so that we can reference it from HTML

    // Prepare message using a Spring helper
    final MimeMessage mimeMessage = mailSender.createMimeMessage();
    final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
    message.setSubject("Example HTML email with inline image");
    message.setFrom("mappingsite0@gmail.com");
    message.setTo(recipientEmail);

    // Create the HTML body using Thymeleaf
    // try {
    //   FileReader fr = new FileReader("src\\main\\resources\\templates\\mail\\registration_complete.html");
    //   int i;
    //   while ((i = fr.read()) != -1)
    //     logger.info("The character is {}", (char) i);
    //   logger.info("Oki Toki");
    //   fr.close();
    // } catch (IOException e) {
    //   logger.error("File not found!");
    // }

    final String htmlContent = templateEngine.process("simple-mail", ctx);
    message.setText(htmlContent, true); // true = isHtml

  
    // Add the inline image, referenced from the HTML code as
    // "cid:${imageResourceName}"
    // final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
    // message.addInline(imageResourceName, imageSource, imageContentType);

    // Send mail
    this.mailSender.send(mimeMessage);

  }
}
