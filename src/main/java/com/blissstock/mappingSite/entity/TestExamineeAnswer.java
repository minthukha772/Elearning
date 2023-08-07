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
@Table(name = "test_examinee_answer")
public class TestExamineeAnswer {
    @Column(name = "examinee_answer_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "test_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Test test;

    @JoinColumn(name = "examinee_student_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private UserInfo student;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examinee_guest_id")
    private GuestUser guestUser;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id")
    private TestQuestion question;

    @Column(name = "examinee_answer", length = 1000)
    private String examinee_answer;

    @Column(name = "examinee_answer_link", length = 255)
    private String examinee_answer_link;

    @Column(name = "correct_status", length = 255)
    private String correct_status;

    @Column(name = "acquired_mark")
    private Integer acquired_mark;

    @Column(name = "marked_status", length = 255)
    private String marked_status;

    public TestExamineeAnswer(Long id, Test test, UserInfo student, TestQuestion question, String examinee_answer,
            String examinee_answer_link, String correct_status, Integer acquired_mark, String marked_status) {
        this.id = id;
        this.test = test;
        this.student = student;
        this.question = question;
        this.examinee_answer = examinee_answer;
        this.examinee_answer_link = examinee_answer_link;
        this.correct_status = correct_status;
        this.acquired_mark = acquired_mark;
        this.marked_status = marked_status;
    }

    public String display() {
        return this.id + ", " + this.test.getTest_id() + ", " + this.student + ", "
                + this.question + ", " + this.examinee_answer + ", " + this.examinee_answer_link + ", "
                + this.correct_status
                + ", " + this.acquired_mark + ", " + this.marked_status;

    }

}
