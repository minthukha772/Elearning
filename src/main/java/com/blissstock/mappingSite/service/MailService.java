package com.blissstock.mappingSite.service;

import javax.mail.MessagingException;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;
import com.blissstock.mappingSite.entity.CourseInfo;


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
  
  public void PaymentByStudent(UserInfo userInfo, long courseId, CourseInfo courseInfo) throws MessagingException;

  public void PaymentReceivedByAdmin(UserInfo userInfo, long courseId, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void VerifiedTeacherByAdmin(UserInfo teacherInfo, UserInfo adminInfo, String appUrl) throws MessagingException;

  public void VerifiedTeacherByAdminToTeacher(UserInfo teacherInfo, UserInfo adminInfo, String appUrl) throws MessagingException;
  
  public void StudentChangedPassword(UserInfo userInfo, UserAccount userAccount, String appUrl) throws MessagingException;
  
  public void TeacherChangedPassword(UserInfo userInfo, UserAccount userAccount, String appUrl) throws MessagingException;

  public void SendAdminNewStudentEnroll(UserInfo userInfo, long courseId, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void SendStudentEnrollCourse(UserInfo userInfo, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void SendTeacherNewStudentEnroll(UserInfo userInfo, CourseInfo courseInfo, String appUrl) throws MessagingException;

  public void AdminChangedPassword(UserInfo userInfo, UserAccount userAccount, String appUrl) throws MessagingException;

  public void AdminResetPassword(UserAccount user, String appUrl) throws MessagingException;

  public void TeacherResetPassword(UserAccount user, String appUrl) throws MessagingException;

  public void StudentResetPassword(UserAccount user, String appUrl) throws MessagingException;

}