package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.entity.PaymentAccount;
import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentAccountRepository extends CrudRepository<PaymentAccount, Long> {
    List<PaymentAccount> findByUserInfo(UserInfo userInfo);
}

