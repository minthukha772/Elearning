package com.blissstock.mappingSite.controller;


import java.util.List;

import javax.validation.Valid;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;


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
    public String register(Model model, @ModelAttribute @Valid TeacherRegisterDTO userInfo,  BindingResult bindingResult){
        model.addAttribute("userInfo", userInfo);
        System.out.println(bindingResult.hasErrors());
        return "register.html";
    }




}
