package com.blissstock.mappingSite.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {

    @Column(name = "contact_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long contactId;

    @Column(name = "name")
    private String name;

    @Column(name = "email")
    private String email;

    @Column(name = "phoneNumber")
    private String phoneNumber;

    @Column(name = "details")
    private String details;

    @Column(name = "description")
    private String description;

    @Past
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "inquiry_date")
    private Date inquiryDate;

    @Column(name = "adminReply")
    private String adminReply;

    @Column(name = "replyStatus")
    private Boolean replyStatus;

    public Contact(Long contactId, String name, String email, String phoneNumber, String details, String description,
            @Past Date inquiryDate, String adminReply, Boolean replyStatus) {
        this.contactId = contactId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.details = details;
        this.description = description;
        this.inquiryDate = inquiryDate;
        this.adminReply = adminReply;
        this.replyStatus = replyStatus;
    }

}
