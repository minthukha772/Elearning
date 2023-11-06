package com.blissstock.mappingSite.model;

import java.util.List;

public class QuestionAndCorrectAnswerAndExamineeAnswer {
    private long id;
    private Integer examinee_answer_id;
    private String examinee_answer;
    private String examinee_answer_link;
    private String question_text;
    private String question_materials;
    private String question_materials_type;
    private List<ExamineeChoiceModel> choices;
    private String question_type;
    private Integer maximum_mark;
    private Integer acquired_mark;
    private String markedStatus;

    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion_text() {
        return question_text;
    }

    public void setQuestion_text(String question_text) {
        this.question_text = question_text;
    }

    public String getQuestion_materials() {
        return question_materials;
    }

    public void setQuestion_materials(String question_materials) {
        this.question_materials = question_materials;
    }

    public String getQuestion_materials_type() {
        return question_materials_type;
    }

    public void setQuestion_materials_type(String question_materials_type) {
        this.question_materials_type = question_materials_type;
    }

    public List<ExamineeChoiceModel> getChoices() {
        return choices;
    }

    public void setChoices(List<ExamineeChoiceModel> choices) {
        this.choices = choices;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public Integer getMaximum_mark() {
        return maximum_mark;
    }

    public void setMaximum_mark(Integer maximum_mark) {
        this.maximum_mark = maximum_mark;
    }

    public Integer getAcquired_mark() {
        return acquired_mark;
    }

    public void setAcquired_mark(Integer acquired_mark) {
        this.acquired_mark = acquired_mark;
    }

    public String getMarkedStatus() {
        return markedStatus;
    }

    public void setMarkedStatus(String markedStatus) {
        this.markedStatus = markedStatus;
    }

    public String getExaminee_answer() {
        return examinee_answer;
    }

    public void setExaminee_answer(String examinee_answer) {
        this.examinee_answer = examinee_answer;
    }

    public String getExaminee_answer_link() {
        return examinee_answer_link;
    }

    public void setExaminee_answer_link(String examinee_answer_link) {
        this.examinee_answer_link = examinee_answer_link;
    }

    public QuestionAndCorrectAnswerAndExamineeAnswer(long id, Integer examinee_answer_id, String examinee_answer,
            String examinee_answer_link,
            String question_text, String question_materials, String question_materials_type,
            List<ExamineeChoiceModel> choices, String question_type, Integer maximum_mark, Integer acquired_mark, String markedStatus) {
        this.id = id;
        this.examinee_answer_id = examinee_answer_id;
        this.examinee_answer = examinee_answer;
        this.examinee_answer_link = examinee_answer_link;
        this.question_text = question_text;
        this.question_materials = question_materials;
        this.question_materials_type = question_materials_type;
        this.choices = choices;
        this.question_type = question_type;
        this.maximum_mark = maximum_mark;
        this.acquired_mark = acquired_mark;
        this.markedStatus = markedStatus;
    }

    public Integer getExaminee_answer_id() {
        return examinee_answer_id;
    }

    public void setExaminee_answer_id(Integer examinee_answer_id) {
        this.examinee_answer_id = examinee_answer_id;
    }

}
