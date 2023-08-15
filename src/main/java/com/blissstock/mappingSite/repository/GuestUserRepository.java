package com.blissstock.mappingSite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.GuestUser;

public interface GuestUserRepository extends JpaRepository<GuestUser, Long> {

    @Query(value = "Select * from guest where mail = :mail limit 1", nativeQuery = true)
        public GuestUser getGuestUserbyEmail(@Param("mail") String mail);
    
}
