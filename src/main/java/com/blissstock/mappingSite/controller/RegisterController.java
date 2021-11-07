package com.blissstock.mappingSite.controller;

import com.blissstock.mappingSite.constrains.PaymentMethod;
import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.utils.MultipartFileUtil;

import java.util.List;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import org.hibernate.validator.HibernateValidator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller

public class RegisterController {

/*   @ExceptionHandler(value = ConstraintViolationException.class)
  public String exception(ConstraintViolationException exception) {
    System.out.println("excption occur");
    System.out.println(exception.getMessage());
    return "redirect:/";
  }
 */
  @Valid
  @GetMapping("/register/{role}/{email}")
  public String registerForm(
    @PathVariable(required = false) String role,
    @Email @PathVariable String email,
    Model model
  ) {
    if (role == null || role != "teacher") {
      role = "student";
    }
    TeacherRegisterDTO userInfo = new TeacherRegisterDTO();
    userInfo.setEmail(email);

    //Initialized payment value
    model.addAttribute("paymentMethodList", PaymentMethod.list);

    model.addAttribute("action", "register");
    model.addAttribute("role", role);
    
    model.addAttribute("userInfo", userInfo);
    return "ST0001_register.html";
  }

  @PostMapping("/register")
  public String register(
    Model model,
    @Valid @ModelAttribute("userInfo") TeacherRegisterDTO userInfo,
    BindingResult bindingResult
  ) {
    //model.addAttribute("userInfo", userInfo);
    System.out.println(userInfo.getProfilePhoto().getSize());
    
    model.addAttribute("paymentMethodList", PaymentMethod.list);
    model.addAttribute("userInfo", userInfo);

/*     if(userInfo.getProfilePhoto() != null){
      //Convert Multipart to Base64 format to show preview on browser
      String profilePhotoBase64 = MultipartFileUtil.toBase64(userInfo.getProfilePhoto());
      System.out.println(profilePhotoBase64.length());
      model.addAttribute("pic64", profilePhotoBase64);
    } */
    
    //System.out.println(bindingResult.toString());
    return "ST0001_register.html";
  }
}
