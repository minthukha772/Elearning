package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.apache.tomcat.jni.User;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.blissstock.mappingSite.entity.Contact;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.repository.ContactRepository;
import com.blissstock.mappingSite.repository.UserAccountRepository;
import com.blissstock.mappingSite.service.MailService;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class HelpAndSupportController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    UserAccountRepository userAccountRepository;

    @Autowired
    MailService mailService;

    private static final Logger logger = LoggerFactory.getLogger(HelpAndSupportController.class);

    @GetMapping("/guest/frequently_asked_questions")
    private String faqs() {

        logger.info("GET request");

        return "frequently_asked_questions";
    }

    @GetMapping("/guest/help_and_support")
    private String HelpSupport() {

        logger.info("GET request");

        return "help_and_support";
    }

    @GetMapping("/contact")
    private String contact() {

        return "contact.html";
    }

    @GetMapping("/guest/contact")
    private String guestContact() {

        return "contactAdmin.html";
    }

    @PostMapping(value = { "/saveInquiry", "/guest/saveInquiry" })
    private ResponseEntity<Long> saveInquiry(@RequestBody String payload) {
        try {
            JSONObject jsonObject = new JSONObject(payload);
            Contact contact = new Contact();
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String number = jsonObject.getString("number");
            String details = jsonObject.getString("details");
            String description = jsonObject.getString("description");

            contact.setName(name);
            contact.setEmail(email);
            contact.setPhoneNumber(number);
            contact.setDetails(details);
            contact.setDescription(description);
            contact.setInquiryDate(new Date());

            Contact savedContact = contactRepository.save(contact);
            Long savedContactId = savedContact.getContactId(); // Assuming getId() returns the ID

            // Return the ID in the response
            return ResponseEntity.ok(savedContactId);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping(value = { "saveInquiry/confirm", "/guest/saveInquiry/confirm" })
    public String confirmScreen(@RequestParam(name = "id") Long id, Model model) {

        System.out.println("Received ID: " + id);

        Optional<Contact> contactOptional = contactRepository.findById(id);

        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();

            Long contactId = contact.getContactId();
            String name = contact.getName();
            String email = contact.getEmail();
            String phone = contact.getPhoneNumber();
            String details = contact.getDetails();
            String description = contact.getDescription();

            model.addAttribute("contactId", contactId);
            model.addAttribute("name", name);
            model.addAttribute("email", email);
            model.addAttribute("phone", phone);
            model.addAttribute("details", details);
            model.addAttribute("description", description);

        } else {
            model.addAttribute("errorMessage", "Contact not found for ID: " + id);

        }

        return "CM0011_EmailConfirm.html";
    }

    /*
     * @PostMapping("/guest/sendEmail")
     * public String sendEmailToAdmin(
     * 
     * @RequestParam(required = false) Long contactId,
     * 
     * @RequestBody String payload) {
     * 
     * JSONObject jsonObject = new JSONObject(payload);
     * String name = jsonObject.getString("name");
     * String contactEmail = jsonObject.getString("email");
     * String phone = jsonObject.getString("number");
     * String details = jsonObject.getString("details");
     * String description = jsonObject.getString("description");
     * List<UserAccount> adminUsers =
     * userAccountRepository.findByRole("ROLE_ADMIN");
     * 
     * if (adminUsers != null && !adminUsers.isEmpty()) {
     * for (UserAccount adminUser : adminUsers) {
     * // Contact contact = contactRepository.findById(contactId).orElse(null);
     * 
     * try {
     * mailService.sendEmailToAdmin(
     * adminUser.getMail(),
     * contactEmail,
     * name,
     * details,
     * description,
     * phone);
     * } catch (MessagingException e) {
     * e.printStackTrace();
     * }
     * 
     * }
     * return "CompleteScreen.html";
     * } else {
     * }
     * return null;
     * }
     */

    @PostMapping("/guest/sendEmail")
    public ResponseEntity<String> sendEmailToAdmin(
            @RequestParam(required = false) Long contactId,
            @RequestBody String payload) {

        JSONObject jsonObject = new JSONObject(payload);
        String name = jsonObject.getString("name");
        String contactEmail = jsonObject.getString("email");
        String phone = jsonObject.getString("number");
        String details = jsonObject.getString("details");
        String description = jsonObject.getString("description");
        List<UserAccount> adminUsers = userAccountRepository.findByRole("ROLE_ADMIN");

        boolean emailsSent = false;

        if (adminUsers != null && !adminUsers.isEmpty()) {
            for (UserAccount adminUser : adminUsers) {
                try {
                    mailService.sendEmailToAdmin(
                            adminUser.getMail(),
                            contactEmail,
                            name,
                            details,
                            description,
                            phone);
                    emailsSent = true;
                } catch (MessagingException e) {
                    e.printStackTrace();
                    emailsSent = false;
                    break; // Break the loop on failure
                }
            }

            if (emailsSent) {
                try {
                    mailService.sendEmailToAsker(
                            contactEmail,
                            name,
                            details,
                            description,
                            phone);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return ResponseEntity.ok("Emails sent successfully");
            } else {
                // Return an error response if sending emails failed
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send emails");
            }
        } else {
            // Return an error response if no admin users found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No admin users found");
        }
    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}
