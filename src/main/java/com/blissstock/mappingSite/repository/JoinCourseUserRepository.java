package com.blissstock.mappingSite.repository;

import com.blissstock.mappingSite.entity.UserInfo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JoinCourseUserRepository extends CrudRepository<UserInfo, Long> {

}
