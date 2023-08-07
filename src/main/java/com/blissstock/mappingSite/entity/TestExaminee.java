package com.blissstock.mappingSite.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test_examinee")
public class TestExaminee {
    @Id
    @Column(name = "test_examinee_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examinee_student_id")
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examinee_guest_id")
    private GuestUser guestUser;

    @Column(name = "examinee_exam_start_time", length = 15)
    private String examinee_exam_start_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public UserInfo getUserAccount() {
        return userInfo;
    }

    public void UserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getStudentExamStartTime() {
        return examinee_exam_start_time;
    }

    public void setStudentExamStartTime(String examinee_exam_start_time) {
        this.examinee_exam_start_time = examinee_exam_start_time;
    }

    public TestExaminee(Long id, Test test, UserInfo userInfo, String examinee_exam_start_time) {
        this.id = id;
        this.test = test;
        this.userInfo = userInfo;
        this.examinee_exam_start_time = examinee_exam_start_time;
    }
}
