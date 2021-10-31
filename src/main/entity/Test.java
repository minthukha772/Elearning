package entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "test")
public class Test {
	
	@Column(name = "test_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long testId;

    @NotBlank(message="Please enter test link")
    @Column(name = "test_link")
	private String testLink;

	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;
	
}

