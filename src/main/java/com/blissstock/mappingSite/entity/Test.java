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
    private Long testId;

    // mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private UserInfo userInfo;

    // mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private CourseInfo courseInfo;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "section_name", length = 255)
    private String section_name;

    @Column(name = "minutes_allowed", length = 100)
    private Integer minutes_allowed;

    @Column(name = "passing_score_percent", length = 100)
    private Integer passing_score_percent;

    @Column(name = "date", length = 100)
    private String Date;

    @Column(name = "start_time", length = 100)
    private String start_time;

    @Column(name = "end_time", length = 100)
    private String end_time;

    @Column(name = "exam_status", length = 100)
    private String exam_status;

    public Test(int course_id, int user_id, String description, String section_name, Integer minutes_allowed,
            Integer passing_score_percent, String date, String start_time, String end_time, String exam_status) {
        this.description = description;
        this.section_name = section_name;
        this.minutes_allowed = minutes_allowed;
        this.passing_score_percent = passing_score_percent;
        this.Date = date;
        this.start_time = start_time;
        this.end_time = end_time;
        this.exam_status = exam_status;
    }
}
