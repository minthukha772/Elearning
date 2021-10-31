package entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "course_time")
public class CourseTime {
	
	@Column(name = "course_time_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long courseTimeId;
	
    @Column(name = "course_days", length = 255)
    @NotBlank(message="Please choose days")
	private String courseDays;

    @Column(name = "course_start_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date courseStartTime;

    @Column(name = "course_end_time", columnDefinition= "TIMESTAMP WITH TIME ZONE")
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date courseEndTime;
	
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
	
}

