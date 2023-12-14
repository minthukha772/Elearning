package com.blissstock.mappingSite.service;

import javax.mail.MessagingException;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.Test;
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

        public void sendVerificationMail(UserAccount userAccount) throws MessagingException;

        public void sendResetPasswordMail(UserAccount userAccount) throws MessagingException;

        public void SendAdminNewTeacher(UserInfo userInfo) throws MessagingException;

        public void SendAdminNewAdmin(UserAccount userAccount, UserInfo adminInfo, String appUrl)
                        throws MessagingException;

        public void SendSuperAdminNewAdmin(UserAccount userAccount, UserInfo adminInfo, String appUrl)
                        throws MessagingException;

        public void SendAdminNewStudent(UserInfo userInfo) throws MessagingException;

        public String getServerAddress();

        public void SendAdminNewCourseByTeacher(CourseInfo courseInfo) throws MessagingException;

        public void SendTeacherNewCourseByTeacher(CourseInfo courseInfo) throws MessagingException;

        public void SendAdminNewCourseByAdmin(UserAccount userAccount, CourseInfo courseInfo, String appUrl)
                        throws MessagingException;

        public void SendTeacherNewCourseByAdmin(CourseInfo courseInfo, String appUrl) throws MessagingException;

        public void PaymentByStudent(UserInfo userInfo, long courseId, CourseInfo courseInfo) throws MessagingException;

        public void PaymentReceivedByAdmin(UserInfo userInfo, long courseId, CourseInfo courseInfo)
                        throws MessagingException;

        public void VerifiedTeacherByAdmin(UserInfo teacherInfo, UserInfo adminInfo) throws MessagingException;

        public void VerifiedTeacherByAdminToTeacher(UserInfo teacherInfo, UserInfo adminInfo) throws MessagingException;

        public void StudentChangedPassword(UserInfo userInfo, UserAccount userAccount) throws MessagingException;

        public void TeacherChangedPassword(UserInfo userInfo, UserAccount userAccount) throws MessagingException;

        public void SendAdminNewStudentEnroll(UserInfo userInfo, long courseId, CourseInfo courseInfo)
                        throws MessagingException;

        public void SendStudentEnrollCourse(UserInfo userInfo, CourseInfo courseInfo) throws MessagingException;

        public void SendTeacherNewStudentEnroll(UserInfo userInfo, CourseInfo courseInfo) throws MessagingException;

        public void SendGuestOneTimePassword(GuestUser guestUser, String otp) throws MessagingException;

        public void SendGuestRemovedNotification(GuestUser guestUser, Test test) throws MessagingException;

        public void AdminChangedPassword(UserInfo userInfo, UserAccount userAccount) throws MessagingException;

        public void AdminResetPassword(UserAccount user) throws MessagingException;

        public void TeacherResetPassword(UserAccount user) throws MessagingException;

        public void StudentResetPassword(UserAccount user) throws MessagingException;

        public void guestResetOneTimePassword(String guestUserName, String email, String examID,
                        String guestUserPhoneNumber,
                        String oneTimePassword) throws MessagingException;

        public void guestsendVerificationMail(String guestUserName, String email, String examID, Test testData)
                        throws MessagingException;

        public void sendEmailToAdmin(String adminEmail, String name, String contactEmail, String phoneNumber,
                        String details, String description) throws MessagingException;

        public void sendEmailToAsker(String name, String contactEmail, String phoneNumber, String details,
                        String description) throws MessagingException;

}