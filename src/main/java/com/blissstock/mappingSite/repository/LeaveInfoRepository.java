package com.blissstock.mappingSite.repository;


import com.blissstock.mappingSite.entity.LeaveInfo;
import com.blissstock.mappingSite.entity.LeaveTest;

//import com.blissstock.mappingSite.entity.PaymentReceive;
//import com.blissstock.mappingSite.entity.PaymentTesting;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface LeaveInfoRepository extends JpaRepository<LeaveTest, Long>{
    
}
