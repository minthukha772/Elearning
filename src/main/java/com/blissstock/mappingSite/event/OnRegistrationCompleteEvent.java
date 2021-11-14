package com.blissstock.mappingSite.event;

import com.blissstock.mappingSite.entity.UserAccount;
import java.util.Locale;
import org.springframework.context.ApplicationEvent;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class OnRegistrationCompleteEvent extends ApplicationEvent {

  private String appUrl;
  private Locale locale;
  private UserAccount userAccount;

  public OnRegistrationCompleteEvent(
    UserAccount userAccount,
    Locale locale,
    String appUrl
  ) {
    super(userAccount);
    this.userAccount = userAccount;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
