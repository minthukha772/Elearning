package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Long> {
    
    @Query(nativeQuery = true, value="delete from user_info  where uid =:userId")
	public UserInfo removeById(@Param("userId")Long userId);

    
}
