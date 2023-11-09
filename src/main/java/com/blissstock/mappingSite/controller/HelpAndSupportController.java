package com.blissstock.mappingSite.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
import com.blissstock.mappingSite.service.UserSessionService;

@Controller
public class HelpAndSupportController {

    @Autowired
    UserSessionService userSessionService;

    @Autowired
    ContactRepository contactRepository;

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
    private String saveInquiry(Model model, @RequestBody String payload) {
        try {
            JSONObject jsonObject = new JSONObject(payload);
            Contact contact = new Contact();
            String name = jsonObject.getString("name");
            String email = jsonObject.getString("email");
            String number = jsonObject.getString("number");
            String details = jsonObject.getString("details");
            String description = jsonObject.getString("description");
            // Contact contact = new
            // Contact(null,name,email,number,details,description,null);
            contact.setName(name);
            contact.setEmail(email);
            contact.setPhoneNumber(number);
            contact.setDetails(details);
            contact.setDescription(description);
            contact.setInquiryDate(new Date());
            // Contact contact = new Contact(null,name,email,number,details,description);
            contactRepository.save(contact);

            return "redirect:/email-confirmscreen";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/email-confirmscreen")
    public String confirmScreen(Model model) {
        Long userId = 52L;

        Contact contact = contactRepository.findById(userId).orElse(null);
        String name = contact.getName();

        model.addAttribute("name", name);

        return "CM0011_EmailConfirm.html";

    }

    private Long getUid() {
        Long uid = userSessionService.getUserAccount().getAccountId();
        return uid;
    }

}
