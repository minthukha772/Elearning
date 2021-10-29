package com.blissstock.mappingSite.controller;


import java.util.List;

import com.blissstock.mappingSite.dto.UserForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {
    
    @GetMapping("/register")
    public String login(Model model){
        model.addAttribute("userForm", new UserForm());
        return "register.html";
    }


}
