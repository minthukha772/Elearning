package com.blissstock.mappingSite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Data
@Getter
@Setter
@Entity
@Table(name = "leave_info")
public class LeaveInfo {

  @Column(name = "leave_id")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long leaveId;

  @NotNull
  @DateTimeFormat(pattern = "MM-dd-yyyy")
  //@Temporal(TemporalType.DATE)
  @Column(name = "leave_start_date")
  private Date leaveStartDate;

  @NotNull
  @DateTimeFormat(pattern = "MM-dd-yyyy")
  //@Temporal(TemporalType.DATE)
  @Column(name = "leave_end_date")
  private Date leaveEndDate;

  @Column(
    name = "leave_start_time",
    columnDefinition = "timestamp with time zone not null"
  )
  @NotNull
  //@Temporal(TemporalType.TIME)
  //@Temporal(TemporalType.TIMESTAMP), columnDefinition= "TIMESTAMP WITH TIME ZONE"
  @Temporal(TemporalType.TIMESTAMP)
  private Date leaveStartTime;

  @Column(
    name = "leave_end_time",
    columnDefinition = "timestamp with time zone not null"
  )
  @NotNull
  // @Temporal(TemporalType.TIME)
  //@Temporal(TemporalType.TIMESTAMP)
  @Temporal(TemporalType.TIMESTAMP)
  private Date leaveEndTime;

  @NotBlank(message = "Please fill reason")
  @Column(name = "reason")
  private String reason;

  //mapping
  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "uid_fkey")
  @JsonIgnore
  private UserInfo userInfo;

  @ManyToOne(fetch = FetchType.EAGER, optional = false)
  @JoinColumn(name = "courseId_fkey")
  @JsonIgnore
  private CourseInfo courseInfo;

  public Long getLeaveId() {
    return this.leaveId;
  }

  public void setLeaveId(Long leaveId) {
    this.leaveId = leaveId;
  }

  public Date getLeaveStartTime() {
    return this.leaveStartTime;
  }

  public void setLeaveStartTime(Date leaveStartTime) {
    this.leaveStartTime = leaveStartTime;
  }

  public Date getLeaveEndTime() {
    return this.leaveEndTime;
  }

  public void setLeaveEndTime(Date leaveEndTime) {
    this.leaveEndTime = leaveEndTime;
  }

  public String getReason() {
    return this.reason;
  }

  public void setReason(String reason) {
    this.reason = reason;
  }

  public UserInfo getUserInfo() {
    return this.userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public CourseInfo getCourseInfo() {
    return this.courseInfo;
  }

  public void setCourseInfo(CourseInfo courseInfo) {
    this.courseInfo = courseInfo;
  }

  public Date getLeaveStartDate() {
    return this.leaveStartDate;
  }

  public void setLeaveStartDate(Date leaveStartDate) {
    this.leaveStartDate = leaveStartDate;
  }

  public Date getLeaveEndDate() {
    return this.leaveEndDate;
  }

  public void setLeaveEndDate(Date leaveEndDate) {
    this.leaveEndDate = leaveEndDate;
  }

  public LeaveInfo(
    Long leaveId,
    Date leaveStartDate,
    Date leaveEndDate,
    Date leaveStartTime,
    Date leaveEndTime,
    String reason,
    UserInfo userInfo,
    CourseInfo courseInfo
  ) {
    this.leaveId = leaveId;
    this.leaveStartDate = leaveStartDate;
    this.leaveEndDate = leaveEndDate;
    this.leaveStartTime = leaveStartTime;
    this.leaveEndTime = leaveEndTime;
    this.reason = reason;
    this.userInfo = userInfo;
    this.courseInfo = courseInfo;
  }

  public LeaveInfo() {}
}
