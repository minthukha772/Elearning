package com.blissstock.mappingSite.entity;

import com.blissstock.mappingSite.dto.TeacherRegisterDTO;
import com.blissstock.mappingSite.dto.UserRegisterDTO;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAccountTests
{
    @Test
    @Tag("Student")
    public void StudentTypeTest(){
        UserRegisterDTO userRegisterDTO = new UserRegisterDTO();
        UserAccount userAccount= UserRegisterDTO.toUserAccount(userRegisterDTO);
        assertThat(userAccount.getRole()).isEqualTo("student");

    }

    @Test
    @Tag("Teacher")
    public void TeacherTypeTest(){
        UserRegisterDTO userRegisterDTO = new TeacherRegisterDTO();
        UserAccount userAccount= UserRegisterDTO.toUserAccount(userRegisterDTO);
        assertThat(userAccount.getRole()).isEqualTo("teacher");

    }
}
