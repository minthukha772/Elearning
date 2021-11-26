package com.blissstock.mappingSite.event;

import java.util.Locale;

import com.blissstock.mappingSite.entity.UserInfo;

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
  private UserInfo userInfo;

  public OnRegistrationCompleteEvent(
    UserInfo userInfo,
    Locale locale,
    String appUrl
  ) {
    super(userInfo);
    this.userInfo = userInfo;
    this.locale = locale;
    this.appUrl = appUrl;
  }
}
