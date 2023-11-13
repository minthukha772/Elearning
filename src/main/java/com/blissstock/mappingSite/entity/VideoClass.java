package com.blissstock.mappingSite.entity;

// import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
// import javax.persistence.OneToMany;
import javax.persistence.Table;

// import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "video_class")
public class VideoClass {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // mapping
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = true)
    private CourseInfo courseInfo;

    // @OneToMany(mappedBy = "video_class")
    // @JsonIgnore
    // private List<CourseInfo> courseInfos;

    @Column(name = "video_id", length = 255)
    private String video_id;

    @Column(name = "video_title ", length = 255)
    private String video_title;

    @Column(name = "video_folder_id ", length = 255)
    private String video_folder_id;

    @Column(name = "video_folder_name  ", length = 255)
    private String video_folder_name;

    @Column(name = "video_order_no", length = 100)
    private Integer video_order_no;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_deleted", length = 50)
    private String is_deleted;

    @Column(name = "deleted_At", length = 50)
    private String deleted_At;

    public VideoClass(Long id, CourseInfo courseInfo, String video_id, String video_title, String video_folder_id,
            String video_folder_name, Integer video_order_no, String description, String is_deleted,
            String deleted_At) {
        this.id = id;

        if (courseInfo != null) {
            this.courseInfo = courseInfo;
        }

        this.description = description;
        this.video_id = video_id;
        this.video_title = video_title;
        this.video_folder_id = video_folder_id;
        this.video_folder_name = video_folder_name;
        this.video_order_no = video_order_no;
        this.description = description;
        this.is_deleted = is_deleted;
        this.deleted_At = deleted_At;

    }

    public String display() {
        return this.id + ", "
                + this.courseInfo + ", " + this.description + ", " +
                this.video_id + ", " + this.video_title + ", " + this.video_folder_id + ", " + this.video_folder_name
                + ", " + this.video_order_no + ", " + this.description + ", " +
                this.is_deleted + ", " + this.deleted_At;

    }
}
