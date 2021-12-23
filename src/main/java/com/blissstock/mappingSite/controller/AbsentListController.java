package com.blissstock.mappingSite.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AbsentListController {
    
    @GetMapping("/admin-absent-list")
    private String absentList(){
        return "AD0004_AbsentList";
    }
}
