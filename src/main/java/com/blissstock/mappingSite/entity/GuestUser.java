package com.blissstock.mappingSite.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.blissstock.mappingSite.validation.constrains.ValidEmail;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "guest")
public class GuestUser implements Serializable{

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

    @Column(name = "one_time_password", length = 255, nullable = false)
    private String one_time_password;

    @Column(name = "password_update_date_time", length = 255)
    private String password_update_date_time;

    @Column(name = "updated_date_time", length = 255)
    private String updated_date_time;

    @Column(name = "deleted_date_time", length = 255)
    private String deleted_date_time;

    public GuestUser(Long guest_id, String name, String mail, String phone_no, String one_time_password,
            String password_update_date_time, String updated_date_time, String deleted_date_time) {
        this.guest_id = guest_id;
        this.name = name;
        this.mail = mail;
        this.phone_no = phone_no;
        this.one_time_password = one_time_password;
        this.password_update_date_time = password_update_date_time;
        this.updated_date_time = updated_date_time;
        this.deleted_date_time = deleted_date_time;
    }

    public String display() {
        return this.guest_id + ", " + this.name + ", " + this.mail + ", "
                + this.phone_no + ", " + this.one_time_password + ", " +
                this.password_update_date_time + ", " + this.updated_date_time + ", " + this.deleted_date_time;

    }
}
