package com.blissstock.mappingSite.entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "leave_test")
public class LeaveTest {

    @Column(name = "leave_test_id")
    @Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long leavetestId;

       //@NotNull
       //@DateTimeFormat(pattern = "MM-dd-yyyy")
       @DateTimeFormat(pattern = "yyyy-MM-dd")
       //@Temporal(TemporalType.TIME)
       @Column(name="leave_start_date")
       private Date leaveStartDate;
   
       //@NotNull
      // @DateTimeFormat(pattern = "MM-dd-yyyy")
      @DateTimeFormat(pattern = "yyyy-MM-dd")
       //@Temporal(TemporalType.TIME)
       @Column(name="leave_end_date")
       private Date leaveEndDate;
   
       //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
       @Column(name = "leave_start_time")
       //@NotNull
       @Temporal(TemporalType.TIME)
       @DateTimeFormat(pattern = "HH:mm")
       private Date leaveStartTime;
   
       //@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
       @Column(name = "leave_end_time")
       //@NotNull
       @Temporal(TemporalType.TIME)
       @DateTimeFormat(pattern = "HH:mm")
       private Date leaveEndTime;
       
       @NotBlank(message="Please fill reason")
       @Column(name="reason")
       private String reason;


    public LeaveTest(Long leavetestId, Date leaveStartDate, Date leaveEndDate, Date leaveStartTime, Date leaveEndTime, String reason) {
        this.leavetestId = leavetestId;
        this.leaveStartDate = leaveStartDate;
        this.leaveEndDate = leaveEndDate;
        this.leaveStartTime = leaveStartTime;
        this.leaveEndTime = leaveEndTime;
        this.reason = reason;
    }

    public Long getLeavetestId() {
        return this.leavetestId;
    }

    public void setLeavetestId(Long leavetestId) {
        this.leavetestId = leavetestId;
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

    public LeaveTest() {
    }

    
}
