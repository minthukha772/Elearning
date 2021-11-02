package com.blissstock.mappingSite.controller;


import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class RegisterController {
    
    @GetMapping("/register")
    public String form(Model model){
        TeacherRegisterDTO userInfo = new TeacherRegisterDTO();
        userInfo.setEmail("sai@gmail.com");
        model.addAttribute("action", "register");
        model.addAttribute("userInfo", userInfo);
        return "register.html";
    }

    @PostMapping("/check")
    public String register(Model model,@Valid @ModelAttribute("userInfo")  TeacherRegisterDTO userInfo,  BindingResult bindingResult){
        //model.addAttribute("userInfo", userInfo);
        //System.out.println(bindingResult.getFieldError("password").get());
        //System.out.println(bindingResult.toString());
        return "register.html";
    }




}
