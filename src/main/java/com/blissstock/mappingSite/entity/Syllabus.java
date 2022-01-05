package com.blissstock.mappingSite.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Data
@Table(name = "syllabus")
public class Syllabus {

  @Column(name = "syllabus_id")
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long syllabusId;

  @NotBlank(message = "Please fill title")
  @Column(name = "title", length = 100)
  private String title;

  //mapping
  @ManyToOne(
    fetch = FetchType.EAGER,
    optional = false
  )
  @JoinColumn(name = "course_id")
  @JsonIgnore
  private CourseInfo courseInfo;

  @OneToMany(
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL,
    mappedBy = "syllabus"
  )
  @JsonIgnore
  private List<Content> content = new ArrayList<>();
  
  //Constructors


  public Syllabus(Long syllabusId, String title, CourseInfo courseInfo, List<Content> content) {
    this.syllabusId = syllabusId;
    this.title = title;
    this.courseInfo = courseInfo;
    this.content = content;
  }

  public Long getSyllabusId() {
    return this.syllabusId;
  }

  public void setSyllabusId(Long syllabusId) {
    this.syllabusId = syllabusId;
  }

  public String getTitle() {
    return this.title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public CourseInfo getCourseInfo() {
    return this.courseInfo;
  }

  public void setCourseInfo(CourseInfo courseInfo) {
    this.courseInfo = courseInfo;
  }

  public List<Content> getContent() {
    return this.content;
  }

  public void setContent(List<Content> content) {
    this.content = content;
  }


}
