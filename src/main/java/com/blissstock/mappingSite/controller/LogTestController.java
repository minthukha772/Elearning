package com.blissstock.mappingSite.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LogTestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogTestController.class);

    @GetMapping("/log/test")
    public ResponseEntity<Object> testLog(){
        LOGGER.info("This  is Info level logger");
        LOGGER.warn("This  is Warn level logger");
        LOGGER.error("This  is Error level logger");
        //Debug and trace not showing
        LOGGER.debug("This  is Debug level logger");
        LOGGER.trace("This  is Info level logger");
        return ResponseEntity.status(HttpStatus.OK).body("Log Test");
    }
}
