package com.blissstock.mappingSite.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Data
@Getter
@Setter
@AllArgsConstructor
public class VerificationToken {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  @CreatedDate
  private Long created;

  public VerificationToken(String token, UserAccount userAccount) {
    this.token = token;
    this.userAccount = userAccount;
  }

  @OneToOne(
    targetEntity = UserAccount.class,
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL
  )
  @JoinColumn(nullable = false, name = "user_account")
  private UserAccount userAccount;
  // standard constructors, getters and setters
}
