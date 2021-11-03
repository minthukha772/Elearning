package com.blissstock.entity;

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
@Table(name = "certificate")
public class Certificate {
	
	@Column(name = "certificate_id")
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long certificateId;
	
    @Column(name = "certificate_name", length = 255)
    @NotBlank(message="Please fill certificate name")
	private String certificatName;

    @Column(name = "certificate_photo")
    @NotBlank(message="Please upload certificate photos")
	private String certificatePhoto;

	
	//mapping
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "uid_fkey")
    @JsonIgnore
    private UserInfo userInfo;
	
}

