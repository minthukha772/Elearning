package com.blissstock.mappingSite.enums;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

public class UserRoleTest {

  @Test
  @Tag("SuperAdmin")
  public void superAdminTest() {
    assertThat(UserRole.SUPER_ADMIN.getValue()).isEqualTo("ROLE_SUPER_ADMIN");
  }

  @Test
  @Tag("Admin")
  public void adminTest() {
    assertThat(UserRole.ADMIN.getValue()).isEqualTo("ROLE_ADMIN");
  }

  @Test
  @Tag("Student")
  public void studentTest() {
    assertThat(UserRole.STUDENT.getValue()).isEqualTo("ROLE_STUDENT");
  }

  @Test
  @Tag("Teacher")
  public void teacherTest() {
    assertThat(UserRole.TEACHER.getValue()).isEqualTo("ROLE_TEACHER");
  }

  @Test
  @Tag("StrToUser")
  public void strToUserTest() {
    assertThat(UserRole.SUPER_ADMIN)
      .isEqualTo(UserRole.strToUserRole("ROLE_SUPER_ADMIN"));
    assertThat(UserRole.ADMIN).isEqualTo(UserRole.strToUserRole("ROLE_ADMIN"));
    assertThat(UserRole.TEACHER).isEqualTo(UserRole.strToUserRole("ROLE_TEACHER"));
    assertThat(UserRole.STUDENT).isEqualTo(UserRole.strToUserRole("ROLE_STUDENT"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("ROLE_GUEST_USER"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("ROLE_GUEST_USER"));
    assertThat(UserRole.GUEST_USER)
      .isEqualTo(UserRole.strToUserRole("ROLE_GUEST_USER"));
  }
}
