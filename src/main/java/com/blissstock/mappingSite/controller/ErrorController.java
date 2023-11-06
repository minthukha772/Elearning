package com.blissstock.mappingSite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    private static Logger logger = LoggerFactory.getLogger(
            ErrorController.class);

    @GetMapping("/404")
    public String notFound(Model model) {
        logger.error("Error: 404, Not Found");
        return "error/404";
    }

    @GetMapping("/500")
    public String internal(Model model) {
        logger.error("Error: 500, Internal Server");
        return "error/500";
    }

    @GetMapping("/403")
    public String accessForbidden(Model model) {
        logger.error("Error: 403, Access Forbidden");
        return "error/403";
    }
}