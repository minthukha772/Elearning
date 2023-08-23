package com.blissstock.mappingSite.entity;

import java.util.Date;
import java.util.List;

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
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "test")
public class Test {

    @Column(name = "test_id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long test_id;

    // mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = true)
    private UserInfo userInfo;

    // mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = true)
    private CourseInfo courseInfo;
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private List<TestExaminee> testExaminee;
    @OneToMany(mappedBy = "test")
    @JsonIgnore
    private List<TestQuestion> testQuestions;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "student_guest", length = 255)
    private String student_guest;

    @Column(name = "section_name", length = 255)
    private String section_name;

    @Column(name = "minutes_allowed", length = 100)
    private Integer minutes_allowed;

    @Column(name = "passing_score_percent", length = 100)
    private Integer passing_score_percent;

    @Column(name = "date", length = 100)
    private Date Date;

    @Column(name = "start_time", length = 100)
    private String start_time;

    @Column(name = "end_time", length = 100)
    private String end_time;

    @Column(name = "exam_status", length = 100)
    private String exam_status;

    @Column(name = "is_delete", length = 50)
    private String isDelete;

    @Column(name = "deleted_at", length = 100)
    private String deletedAt = "empty";

    @Column(name = "exam_target", length = 10)
    private Integer exam_target;

    @Column(name = "exam_announce", length = 10)
    private Integer exam_announce;

    public Test(Long test_id, CourseInfo courseInfo, UserInfo userInfo, String description, String section_name,
            Integer minutes_allowed, Integer passing_score_percent, Date date, String start_time,
            String end_time, String exam_status, String isDelete, String deletedAt, String student_guest,
            int exam_target) {
        this.student_guest = student_guest;
        this.test_id = test_id;
        if (courseInfo != null) {
            this.courseInfo = courseInfo;
        }
        if (userInfo != null) {
            this.userInfo = userInfo;
        }
        this.description = description;
        this.section_name = section_name;
        this.minutes_allowed = minutes_allowed;
        this.passing_score_percent = passing_score_percent;
        this.Date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.exam_status = exam_status;
        this.isDelete = isDelete;
        this.deletedAt = deletedAt;
        this.exam_target = exam_target;
    }

    public String display() {
        return this.test_id + ", " + ", " + ", "
                + this.description + ", " + this.section_name + ", " +
                this.minutes_allowed + ", " + this.passing_score_percent + ", " + this.Date + ", " + this.start_time
                + ", " + this.end_time + ", " + this.exam_status + ", " +
                this.isDelete + ", " + this.deletedAt + "," + this.student_guest;

    }
}
