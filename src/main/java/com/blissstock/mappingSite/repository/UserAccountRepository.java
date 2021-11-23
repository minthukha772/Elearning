package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository
  extends CrudRepository<UserAccount, String> {
  /* @Query(nativeQuery = true, value = "SELECT * FROM user_account u WHERE u.mail = :mail")
  UserAccount findUserByEmail(@Param("mail") String mail); */
}
