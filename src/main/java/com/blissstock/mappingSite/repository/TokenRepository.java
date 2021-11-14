package com.blissstock.mappingSite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blissstock.mappingSite.entity.VerificationToken;

@Repository
public interface TokenRepository extends CrudRepository<VerificationToken, Long>{

    VerificationToken findByToken(String token);
    
}
