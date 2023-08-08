package com.blissstock.mappingSite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "guest")
public class GuestUser {

    @Column(name = "guest_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long guest_id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "mail", length = 255)
    private String mail;

    @Column(name = "phone_no", length = 255)
    private String phone_no;

    @Column(name = "password_update_date_time", length = 255)
    private String password_update_date_time;

    @Column(name = "updated_date_time", length = 255)
    private String updated_date_time;

    @Column(name = "deleted_date_time", length = 255)
    private String deleted_date_time;
}
