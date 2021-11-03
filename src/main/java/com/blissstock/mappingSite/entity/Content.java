package com.blissstock.mappingSite.entity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "content")
public class Content {
	
	@Column(name = "content_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long contentId;

    @NotBlank(message="Please fill content under title")
	@Column(name="content", length = 50)
	private String content;

	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "syllabusId_fkey")
    @JsonIgnore
    private Syllabus syllabus;
	
}

