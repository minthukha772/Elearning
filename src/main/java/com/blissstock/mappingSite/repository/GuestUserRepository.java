package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.GuestUser;

public interface GuestUserRepository extends JpaRepository<GuestUser, Long> {

    @Query(value = "Select * from guest where guest_id = :guest_id", nativeQuery = true)
    public GuestUser findByGuestId(@Param("guest_id") Long guest_id);

    @Query("SELECT gu FROM TestExaminee te " +
            "JOIN te.guestUser gu " +
            "JOIN te.test t " +
            "WHERE gu.mail = :guestEmail " +
            "AND t.test_id = :testId")
    GuestUser findGuestUserByGuestEmailAndTestId(@Param("guestEmail") String guestEmail,
            @Param("testId") Long testId);
    
}
