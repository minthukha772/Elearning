package com.blissstock.mappingSite.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TestGuestList {
    String guestName;
    String guestEmail;
    String guestPhoneNumber;
    String guestOneTimePassword;

    public TestGuestList(String guestName, String guestEmail, String guestPhoneNumber, String guestOneTimePassoword) {
        this.guestName = guestName;
        this.guestEmail = guestEmail;
        this.guestPhoneNumber = guestPhoneNumber;
        this.guestOneTimePassword = guestOneTimePassoword;
    }

}
