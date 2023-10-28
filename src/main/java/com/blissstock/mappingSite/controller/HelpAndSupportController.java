package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

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

import com.blissstock.mappingSite.entity.Contact;
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.repository.ContactRepository;
import com.blissstock.mappingSite.service.EmailReplyService;
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class HelpAndSupportController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    EmailReplyService emailReplyService;

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
    private ResponseEntity saveInquiry(@RequestBody String payload) {
        try {
            JSONObject jsonObject = new JSONObject(payload);
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String number = jsonObject.getString("number");
            String details = jsonObject.getString("details");
            String description = jsonObject.getString("description");
            // Contact contact = new
            // Contact(null,name,email,number,details,description,null);
            Contact contact = new Contact();
            contact.setName(name);
            contact.setEmail(email);
            contact.setPhoneNumber(number);
            contact.setDetails(details);
            contact.setDescription(description);
            contact.setInquiryDate(new Date());
            // Contact contact = new Contact(null,name,email,number,details,description);
            contactRepository.save(contact);

            return ResponseEntity.ok(HttpStatus.OK);
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/admin/email")
    public String AdminEmail(Model model,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(required = false) String details,
            @RequestParam(required = false) String sendType) {
        try {
            List<Contact> emailList;

            if ((fromDate != null && !fromDate.isEmpty()) && (toDate != null && !toDate.isEmpty())) {
                // Date parameters are provided
                logger.info("Called AD0009 with parameters: fromDate={} and toDate={}", fromDate, toDate);
                Date from = new SimpleDateFormat("yyyy-MM-dd").parse(fromDate);
                Date to = new SimpleDateFormat("yyyy-MM-dd").parse(toDate);
                logger.info("Initiate to Operation Retrieve Table test by Query fromDate {} toDate {}", fromDate,
                        toDate);
                emailList = contactRepository.getListByDate(from, to);
                logger.info("Operation Retrieve Table test by Query fromDate {} toDate {} Result list {} Success", from,
                        to, emailList.size());
                model.addAttribute("emailList", emailList);
                model.addAttribute("filter", "( " + fromDate + " - " + toDate + " )");
            } else if (details != null && !details.isEmpty()) {
                logger.info("Initiate to Operation Retrieve Table Contact by Query Status {}", details);
                emailList = contactRepository.getListBySendType(details);
                logger.info("Operation Retrieve Table Contact by Query Status {} Result list {} Success", details,
                        emailList.size());
                model.addAttribute("emailList", emailList);
                model.addAttribute("filterType", "Filter By Status");
                model.addAttribute("filter", "( " + details + " )");
            } else {
                emailList = contactRepository.getListByEmail();
                logger.info("Operation Retrieve Table Contact by Query None Result list {} Success", emailList.size());
                model.addAttribute("emailList", emailList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "AD0009_AdminEmail.html";
    }

    @GetMapping(value = { "/admin/get-email" })
    private ResponseEntity getEmailByAdmin(@RequestParam(required = false) Long contactId) throws ParseException {
        try {

            logger.info("Called getByEmail with parameter(email_id={})", contactId);
            // Long emailId = Long.parseLong(contactId);
            logger.info("Initiate Operation Retrieve Table contact by Query: contactId:{}", contactId);
            List<Contact> emailInfos = contactRepository.findByUID(contactId);
            logger.info(
                    "Contact ID {} Operation Retrieve Table contact by Query: email_id:{}. Result: course(s)={} | Success",
                    emailInfos.size());
            logger.info("Called getCourseByEmail with parameter(teacher_id={}) Success", emailInfos);
            logger.info("email_id: {}");
            return ResponseEntity.ok(emailInfos);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            logger.error("Failed to retrieve courses for teacher with ID {}", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/admin/sendEmail")
    public ResponseEntity sendEmail(@RequestBody String payload, @RequestParam Long userId) {
        try {
            JSONObject jsonObject = new JSONObject(payload);
            String description = jsonObject.getString("description");
            Contact contact = new Contact();
            contact.setAdminReply(description);
            contactRepository.save(contact);

            Contact contactId = contactRepository.getContactByUser(userId);
            String email = contactId.getEmail();
            String subject = "To Reply your question" + contactId.getDetails();
            String body = "Dear Mr./Ms. " + contactId.getName() + "\n\n" +
                    "Hello, We are from Pyinnyar Subuu Team.\n\n" +
                    "We are replied to your questions.\n\n" +
                    "Question: " + contactId.getDetails() + "\n" +
                    "Our Answer: " + contactId.getAdminReply() + "\n" +
                    "============\n" +
                    "============\n\n" +
                    "* Depending on the email software you are using, the URL may be broken in the middle.\n" +
                    "In that case, enter the first \"https: //\" to the last alphanumerical in the browser.\n" +
                    "Please copy and paste directly to access.\n\n" +
                    "* This email is delivered from the send-only email address.\n" +
                    "Thank you for using our service!\n\n" +
                    "Pyinnyar Subuu\n" +
                    "Bliss Stock JP";

            emailReplyService.sendEmail(email, subject, body);

            return ResponseEntity.ok(HttpStatus.OK);

        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();

        }

    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}
