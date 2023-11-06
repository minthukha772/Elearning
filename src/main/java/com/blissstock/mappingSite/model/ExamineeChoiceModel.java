package com.blissstock.mappingSite.model;

public class ExamineeChoiceModel {
    public int index;
    public String choice;
    public boolean correct;
    public boolean examinee_choice;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isExaminee_choice() {
        return examinee_choice;
    }

    public void setExaminee_choice(boolean examinee_choice) {
        this.examinee_choice = examinee_choice;
    }

    public ExamineeChoiceModel(int index, String choice, boolean correct, boolean examinee_choice) {
        this.index = index;
        this.choice = choice;
        this.correct = correct;
        this.examinee_choice = examinee_choice;
    }
}
