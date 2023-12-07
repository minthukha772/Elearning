package com.blissstock.mappingSite.model;

import com.blissstock.mappingSite.entity.GuestUser;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.UserInfo;

public class TestExamineeWithMarkedCountModel {
    public Long id;
    public Test test;
    public UserInfo userInfo;
    public GuestUser guestUserInfo;
    public int total_free_answer;
    public int total_unmark_answer;
    public FileInfo profilePic;

    public TestExamineeWithMarkedCountModel(Long id, Test test, UserInfo userInfo, int total_free_answer,
            int total_unmark_answer, FileInfo profilePic) {
        this.id = id;
        this.test = test;
        this.userInfo = userInfo;
        this.total_free_answer = total_free_answer;
        this.total_unmark_answer = total_unmark_answer;
        this.profilePic = profilePic;
    }

    public TestExamineeWithMarkedCountModel(Long id, Test test, GuestUser guestUserInfo, int total_free_answer,
            int total_unmark_answer) {
        this.id = id;
        this.test = test;
        this.guestUserInfo = guestUserInfo;
        this.total_free_answer = total_free_answer;
        this.total_unmark_answer = total_unmark_answer;
    }
}
