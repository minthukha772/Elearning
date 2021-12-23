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
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long leaveId;
	
    @NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column(name="leave_date")
	private Date leaveDate;

    @Column(name = "leave_start_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date leaveStartTime;

    @Column(name = "leave_end_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date leaveEndTime;
	
    @NotBlank(message="Please fill reason")
	@Column(name="reason")
	private String reason;

	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "join_fkey")
    @JsonIgnore
    private JoinCourseUser join;

   
	
}

 

