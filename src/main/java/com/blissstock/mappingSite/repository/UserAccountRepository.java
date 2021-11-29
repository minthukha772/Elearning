package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserAccount;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository
  extends CrudRepository<UserAccount, Long> {
  /*   @Query(
    nativeQuery = true,
    value = "SELECT * FROM user_account u WHERE u.mail = :mail"
  ) */
  UserAccount findByMail(/* @Param("mail") */String mail);
}
