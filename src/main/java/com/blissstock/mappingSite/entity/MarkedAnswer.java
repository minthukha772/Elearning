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

@Entity
@Table(name = "marked_answer")
public class MarkedAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "marked_answer_id")
    private Long markedAnswerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "test_student_id")
    private TestStudent testStudent;

    @Column(name = "total_questions")
    private Integer totalQuestions;

    @Column(name = "marked_questions")
    private Integer markedQuestions;

    // Constructor, getters, and setters

    public MarkedAnswer() {}

    public MarkedAnswer(TestStudent testStudent, Integer totalQuestions, Integer markedQuestions) {
        this.testStudent = testStudent;
        this.totalQuestions = totalQuestions;
        this.markedQuestions = markedQuestions;
    }

    public Long getMarkedAnswerId() {
        return markedAnswerId;
    }

    public void setMarkedAnswerId(Long markedAnswerId) {
        this.markedAnswerId = markedAnswerId;
    }

    public TestStudent getTestStudent() {
        return testStudent;
    }

    public void setTestStudent(TestStudent testStudent) {
        this.testStudent = testStudent;
    }

    public Integer getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(Integer totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public Integer getMarkedQuestions() {
        return markedQuestions;
    }

    public void setMarkedQuestions(Integer markedQuestions) {
        this.markedQuestions = markedQuestions;
    }
}