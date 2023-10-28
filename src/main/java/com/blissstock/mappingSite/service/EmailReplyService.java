package com.blissstock.mappingSite.service;

public interface EmailReplyService {
    public void sendEmail(String to, String subject, String body);
}
