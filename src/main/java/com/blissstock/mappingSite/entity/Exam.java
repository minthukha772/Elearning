package com.blissstock.mappingSite.entity;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="exam")
public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="test_name")
    private String testName;
    @Column(name="exam_status")
    private String examStatus;
    @Column(name="class_name")
    private String className;
    @Column(name="section_name")
    private String sectionName;
    @Column(name="teacher_name")
    private String teacherName;
    @Column(name="passing_score")
    private String passingScore;
    @Column(name = "exam_minutes")
    private String examMinutes;
    @Column(name="exam_date")
    private LocalDate examDate;
    @Column(name = "start_time")
    private LocalTime startTime;
    @Column(name = "end_time")
    private LocalTime endTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    private UserAccount userAccount;

    @OneToMany(fetch = FetchType.LAZY,mappedBy = "exam",cascade = CascadeType.ALL)
    private List<TestStudent> testStudentList = new ArrayList<>();


    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getTestName() {
        return testName;
    }
    public void setTestName(String testName) {
        this.testName = testName;
    }
    public String getExamStatus() {
        return examStatus;
    }
    public void setExamStatus(String examStatus) {
        this.examStatus = examStatus;
    }
    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }
    public String getSectionName() {
        return sectionName;
    }
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }
    public String getTeacherName() {
        return teacherName;
    }
    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
    public String getPassingScore() {
        return passingScore;
    }
    public void setPassingScore(String passingScore) {
        this.passingScore = passingScore;
    }
    
    
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public String getExamMinutes() {
        return examMinutes;
    }
    public void setExamMinutes(String examMinutes) {
        this.examMinutes = examMinutes;
    }
    public LocalDate getExamDate() {
        return examDate;
    }
    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }
    public UserAccount getUserAccount() {
        return userAccount;
    }
    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }
    public List<TestStudent> getTestStudentList() {
        return testStudentList;
    }
    public void setTestStudentList(List<TestStudent> testStudentList) {
        this.testStudentList = testStudentList;
    }
    
    
    
    


}
