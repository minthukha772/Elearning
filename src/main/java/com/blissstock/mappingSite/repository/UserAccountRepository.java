package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserAccount;
import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}

