// package com.blissstock.mappingSite.service;

// import java.util.UUID;

// import com.blissstock.mappingSite.entity.UserAccount;
// import com.blissstock.mappingSite.enums.TokenType;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Qualifier;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.mail.javamail.MimeMessagePreparator;
// import org.springframework.stereotype.Service;

// @Service
// public class MailServiceImpl implements MailService {

//   private static final Logger logger = LoggerFactory.getLogger(
//     MailServiceImpl.class
//   );

//   @Autowired
//   @Qualifier("gmail")
//   private JavaMailSender mailSender;

//   @Autowired
//   private UserService userService;

// /*   @Autowired
//   private MessageSource messages;
//  */
//   private String email = "mappingsite0@gmail.com";
//   private String fromName = "Mapping Site";

//   public void sendMail(
//     String subject,
//     String toAddresses,
//     String ccAddresses,
//     String bccAddresses,
//     String body
//   ) {
//     MimeMessagePreparator preparator = mimeMessage -> {
//       MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
//       message.setTo(toAddresses.split("[,;]"));
//       message.setFrom(email, fromName);
//       message.setSubject(subject);
//       if (!ccAddresses.isBlank()) message.setCc(ccAddresses.split("[;,]"));
//       if (!bccAddresses.isBlank()) message.setBcc(bccAddresses.split("[;,]"));
//       message.setText(body, false);
//     };
//     mailSender.send(preparator);
//     logger.info(
//       "Email sent successfully To {},{} with Subject {}",
//       toAddresses,
//       ccAddresses,
//       subject
//     );
//   }

//   public void sendVerificationMail(UserAccount userAccount, String appUrl) {
//     String token = UUID.randomUUID().toString();
//     userService.createToken(userAccount, token, TokenType.VERIFICATION);

//     String recipientAddress = userAccount.getMail();
//     String subject = "Registration Confirmation";

//     String confirmationUrl = appUrl + "/verify_password?token=" + token;
//     String message =
//       "Congratulations, your account has been successfully created. Please go to the following link to confirm the account";
//     String body = message + "\r\n" + confirmationUrl;
//     System.out.println(confirmationUrl.toString());
//     sendMail(subject, recipientAddress, "", "", body);
//   }

//   @Override
//   public void sendResetPasswordMail(UserAccount userAccount, String appUrl) {
//     String token = UUID.randomUUID().toString();
//     userService.createToken(userAccount, token, TokenType.PASSWORD_RESET);

//     String recipientAddress = userAccount.getMail();
//     String subject = "Reset Password";

//     String confirmationUrl = appUrl + "/password/verify_password?token=" + token;
//     String message =
//       "You have requested to reset password. Please click the following link to continue: ";
//     String body = message + "\r\n" + confirmationUrl;
//     System.out.println(confirmationUrl.toString());
//     sendMail(subject, recipientAddress, "", "", body);
    
//   }
// }
