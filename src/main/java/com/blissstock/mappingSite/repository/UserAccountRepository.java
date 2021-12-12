package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.entity.UserAccount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    
     
    List<UserAccount> findByRole(String role);
    UserAccount findByMail(/* @Param("mail") */String mail);

}
