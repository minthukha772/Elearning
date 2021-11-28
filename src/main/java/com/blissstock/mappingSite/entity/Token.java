package com.blissstock.mappingSite.entity;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String token;

  private String type;

  @CreatedDate
  private Long created;

  public Token(String token, UserAccount userAccount) {
    this.token = token;
    this.userAccount = userAccount;
  }

  @ManyToOne(
    targetEntity = UserAccount.class,
    fetch = FetchType.EAGER,
    cascade = CascadeType.ALL
  )
  @JoinColumn(nullable = false, name = "user_account")
  private UserAccount userAccount;

  private Date expiryDate;
}
