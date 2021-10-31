package entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
	
}

