package com.blissstock.mappingSite.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UserRoleTest {

  @Test
  @Tag("SuperAdmin")
  public void superAdminTest() {
    assertThat(UserRole.SUPER_ADMIN.getValue()).isEqualTo("super admin");
  }

  @Test
  @Tag("Admin")
  public void adminTest() {
    assertThat(UserRole.ADMIN.getValue()).isEqualTo("admin");
  }

  @Test
  @Tag("Student")
  public void studentTest() {
    assertThat(UserRole.STUDENT.getValue()).isEqualTo("student");
  }

  @Test
  @Tag("Teacher")
  public void teacherTest() {
    assertThat(UserRole.TEACHER.getValue()).isEqualTo("teacher");
  }

  @Test
  @Tag("StrToUser")
  public void strToUserTest() {
    assertThat(UserRole.SUPER_ADMIN)
      .isEqualTo(UserRole.strToUserRole("super admin"));
    assertThat(UserRole.ADMIN).isEqualTo(UserRole.strToUserRole("admin"));
    assertThat(UserRole.TEACHER).isEqualTo(UserRole.strToUserRole("teacher"));
    assertThat(UserRole.STUDENT).isEqualTo(UserRole.strToUserRole("student"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("guest user"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("guest user"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("guest user"));
  }
}
