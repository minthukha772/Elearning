package com.blissstock.mappingSite.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.VideoClass;

public interface VideoClassRepository extends JpaRepository<VideoClass, Long> {

        
        @Query(value = "Select * from video_class where course_id = :course_id order by video_order_no ", nativeQuery = true)
        public List<VideoClass> getVideoListByCourseId(@Param("course_id") Long course_id);
       

}
