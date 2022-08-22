package com.blissstock.mappingSite.service;

import javax.mail.MessagingException;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;

public interface MailService {
  public void sendMail(
      String subject,
      String toAddresses,
      String ccAddresses,
      String bccAddresses,
      String body);

  public void sendVerificationMail(UserAccount userAccount, String appUrl) throws MessagingException;

  public void sendResetPasswordMail(UserAccount userAccount, String appUrl) throws MessagingException;

  public void SendAdminNewTeacher(UserInfo userInfo, String appUrl) throws MessagingException;

  public void SendAdminNewStudent(UserInfo userInfo, String appUrl) throws MessagingException;

  public void SendAdminNewCourseByTeacher(CourseInfo courseInfo, String appUrl) throws MessagingException;
  
  public void SendTeacherNewCourseByTeacher(CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void SendAdminNewStudentEnroll(UserInfo userInfo, long courseId, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void SendStudentEnrollCourse(UserInfo userInfo, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void SendTeacherNewStudentEnroll(UserInfo userInfo, CourseInfo courseInfo, String appUrl) throws MessagingException;
}