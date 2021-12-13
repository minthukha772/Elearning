package com.blissstock.mappingSite.repository;
import java.util.List;

import com.blissstock.mappingSite.entity.UserAccount;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {
    
    List<UserAccount> findByRole(String role);
    UserAccount findByMail(/* @Param("mail") */String mail);

}
