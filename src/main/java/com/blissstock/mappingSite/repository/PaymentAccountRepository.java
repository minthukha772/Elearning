package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.PaymentAccount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAccountRepository extends JpaRepository<PaymentAccount, Long> {
}

