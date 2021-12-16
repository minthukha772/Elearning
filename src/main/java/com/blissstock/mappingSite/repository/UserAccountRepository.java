package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserAccount;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository
  extends JpaRepository<UserAccount, Long> {
  List<UserAccount> findByRole(String role);
  UserAccount findByMail(/* @Param("mail") */String mail);
}
