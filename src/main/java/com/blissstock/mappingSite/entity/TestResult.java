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
@Table(name = "test_result")
public class TestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "result_id")
    private Long resultId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private Test test;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examinee_student_id")
    private UserInfo user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "examinee_guest_id")
    private GuestUser guestUser;

    @Column(name = "mark")
    private Integer resultMark;

    @Column(name = "result", length = 100)
    private String result;

    @Column(name = "teacher_comment", columnDefinition = "TEXT")
    private String teacherComment;

    public Long getResultId() {
        return resultId;
    }

    public void setResultId(Long resultId) {
        this.resultId = resultId;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public Integer getResultMark() {
        return resultMark;
    }

    public void setResultMark(Integer resultMark) {
        this.resultMark = resultMark;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getTeacherComment() {
        return teacherComment;
    }

    public void setTeacherComment(String teacherComment) {
        this.teacherComment = teacherComment;
    }

    public TestResult(Long resultId, Test test, UserInfo user, GuestUser guestUser, Integer resultMark, String result,
            String teacherComment) {
        this.resultId = resultId;
        this.test = test;
        this.user = user;
        this.guestUser = guestUser;
        this.resultMark = resultMark;
        this.result = result;
        this.teacherComment = teacherComment;
    }

    public String display() {
        return this.resultId + ", " + this.test.getTest_id() + ", " + this.user.getUserName() + ", "
                + this.resultMark + ", " + this.result + ", " +
                this.teacherComment;

    }

}
