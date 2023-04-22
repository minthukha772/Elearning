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
@Table(name = "test_question_correct_answer")
public class TestQuestionCorrectAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private TestQuestion TestQuestion;

    @Column(name = "correct_answer")
    private String correctAnswer;

    public TestQuestionCorrectAnswer(long id, TestQuestion TestQuestion, String correctAnswer) {
        this.id = id;
        this.TestQuestion = TestQuestion;
        this.correctAnswer = correctAnswer;
    }
}
