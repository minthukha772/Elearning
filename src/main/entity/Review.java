package entity;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "review")
public class Review {
	
	@Column(name = "review_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long reviewId;
	
    @NotBlank(message="Please choose review type")
    @Column(name = "review_type")
	private int reviewType;

    @NotBlank(message="Please fill rating star")
    @Column(name = "star")
	private int star;

    @NotBlank(message="Please fill feedback")
	@Column(name="feedback")
	private String feedback;

    @NotNull
    @Column(name = "review_status", length = 15)
	private String reviewStatus;

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

