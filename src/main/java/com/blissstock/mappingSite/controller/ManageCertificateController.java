package com.blissstock.mappingSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManageCertificateController {
    @GetMapping("/manage_certificate")
    public String manageCertificate(Model model){
        
        return "AT0007_manage_certificate";
        
    }
}
