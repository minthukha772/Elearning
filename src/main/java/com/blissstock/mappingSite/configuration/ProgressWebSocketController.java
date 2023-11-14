package com.blissstock.mappingSite.configuration;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ProgressWebSocketController {

    @MessageMapping("/progress") 
    @SendTo("/topic/progress") 
    public Double sendProgress(Double progress) {
        return progress;
    }
}

