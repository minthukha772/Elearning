package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserAccount;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, String> {
    
}

