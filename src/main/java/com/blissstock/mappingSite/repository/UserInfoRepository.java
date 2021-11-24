package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserInfo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {

    @Query(nativeQuery = true, value="SELECT * FROM user_info WHERE user_account_mail=:email")
	public UserInfo findUserInfoByEmail(@Param("email") String email);

}
