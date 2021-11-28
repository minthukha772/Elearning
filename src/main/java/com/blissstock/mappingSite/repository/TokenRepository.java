package com.blissstock.mappingSite.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.blissstock.mappingSite.entity.Token;

@Repository
public interface  TokenRepository extends CrudRepository<Token, Long>{

    //Token findByToken(String token);
    
}
