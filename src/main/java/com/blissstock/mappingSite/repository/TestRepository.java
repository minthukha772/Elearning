package com.blissstock.mappingSite.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.Test;

public interface TestRepository extends JpaRepository<Test, Long> {

    /**
     *
     * @param exam_status
     * @return
     */
    @Query(value = "Select * from test where exam_status = :exam_status and user_info_account_id = :user_id", nativeQuery = true)
    public List<Test> getListByStatusandUser(@Param("exam_status") String exam_status, @Param("user_id") Long user_id);

    @Query(value = "Select * from test where user_info_account_id = :user_id", nativeQuery = true)
    public List<Test> getListByUser(@Param("user_id") Long user_id);
}
