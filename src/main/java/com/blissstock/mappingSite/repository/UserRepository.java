package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    
}

