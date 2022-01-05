package com.blissstock.mappingSite.entity;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "join_course_user")
public class JoinCourseUser {

    @Column(name = "join_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long joinId;

	//mapping
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "courseId_fkey")
    @JsonIgnore
    private CourseInfo courseInfo;

    
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;

    @OneToMany(
        fetch = FetchType.LAZY, 
        cascade = CascadeType.ALL, 
        mappedBy="join"
        )
    @JsonIgnore
    private List<Review> review= new ArrayList<>();

    @OneToMany(
        fetch = FetchType.LAZY, 
        cascade = CascadeType.ALL, 
        mappedBy="join"
        )
    @JsonIgnore
    private List<LeaveInfo> leaveInfo= new ArrayList<>();

    @OneToMany(
        fetch = FetchType.LAZY, 
        cascade = CascadeType.ALL, 
        mappedBy="join"
        )
    @JsonIgnore
    private List<PaymentReceive> paymentReceive= new ArrayList<>();

    //Constructors

    public Long getJoinId() {
        return this.joinId;
    }

    public void setJoinId(Long joinId) {
        this.joinId = joinId;
    }

    public CourseInfo getCourseInfo() {
        return this.courseInfo;
    }

    public void setCourseInfo(CourseInfo courseInfo) {
        this.courseInfo = courseInfo;
    }

    public UserInfo getUserInfo() {
        return this.userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public List<Review> getReview() {
        return this.review;
    }

    public void setReview(List<Review> review) {
        this.review = review;
    }

    public List<LeaveInfo> getLeaveInfo() {
        return this.leaveInfo;
    }

    public void setLeaveInfo(List<LeaveInfo> leaveInfo) {
        this.leaveInfo = leaveInfo;
    }

    public List<PaymentReceive> getPaymentReceive() {
        return this.paymentReceive;
    }

    public void setPaymentReceive(List<PaymentReceive> paymentReceive) {
        this.paymentReceive = paymentReceive;
    }


}


