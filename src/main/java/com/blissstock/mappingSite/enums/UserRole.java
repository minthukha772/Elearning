package com.blissstock.mappingSite.enums;

import lombok.Getter;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserRole {
    SUPER_ADMIN("super_admin"), // all these enums give error, for no constructor
    ADMIN("admin"),
    STUDENT("student"),
    TEACHER("teacher"),
    GUEST_USER("guest_user");

    @Getter private String value;

    public static UserRole strToUserRole(String str){
        for(UserRole userRole: UserRole.values()){
            if(str.equalsIgnoreCase(userRole.value)){
                return userRole;
            }
        }
        return UserRole.GUEST_USER;
    }

  }