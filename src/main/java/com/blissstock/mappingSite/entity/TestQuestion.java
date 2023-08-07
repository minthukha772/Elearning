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
@Table(name = "test_question")
public class TestQuestion {
    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "test_id")
    private Test test;

    @Column(name = "question_text", columnDefinition = "TEXT")
    private String question_text;

    @Column(name = "question_materials", length = 255)
    private String question_materials;

    @Column(name = "question_materials_type", length = 255)
    private String question_materials_type;

    @Column(name = "choices", length = 255)
    private String choices;

    @Column(name = "question_type", length = 255)
    private String question_type;

    @Column(name = "maximum_mark", length = 100)
    private Integer maximum_mark;

    public TestQuestion(Long id, Test test, String question_text, String question_materials,
            String question_materials_type, String choices, String question_type, Integer maximum_mark) {
        this.id = id;
        this.test = test;
        this.question_text = question_text;
        this.question_materials = question_materials;
        this.question_materials_type = question_materials_type;
        this.choices = choices;
        this.question_type = question_type;
        this.maximum_mark = maximum_mark;
    }


    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Test getTest() {
        return this.test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getQuestion_text() {
        return this.question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_materials() {
        return this.question_materials;
    }

    public void setQuestion_materials(String question_materials) {
        this.question_materials = question_materials;
    }

    public String getQuestion_materials_type() {
        return this.question_materials_type;
    }

    public void setQuestion_materials_type(String question_materials_type) {
        this.question_materials_type = question_materials_type;
    }

    public String getChoices() {
        return this.choices;
    }

    public void setChoices(String choices) {
        this.choices = choices;
    }

    public String getQuestion_type() {
        return this.question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public Integer getMaximum_mark() {
        return this.maximum_mark;
    }

    public void setMaximum_mark(Integer maximum_mark) {
        this.maximum_mark = maximum_mark;
    }    
    
}
