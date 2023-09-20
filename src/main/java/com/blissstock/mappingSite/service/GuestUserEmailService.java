package com.blissstock.mappingSite.service;

public interface GuestUserEmailService {
    public void sendEmail(String to, String subject, String body);
}
