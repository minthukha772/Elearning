<<<<<<< HEAD
package com.blissstock.mappingSite.repository;

import java.util.List;

import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

=======
// package com.blissstock.mappingSite.repository;

// import java.util.List;

<<<<<<<< HEAD:src/main/java/com/blissstock/mappingSite/repository/CourseInfoRepository.java
import com.blissstock.mappingSite.entity.CourseInfo;
import com.blissstock.mappingSite.entity.Review;
========
// import com.blissstock.mappingSite.entity.Review;
// import com.blissstock.mappingSite.entity.ReviewTest;
>>>>>>>> origin/data-model-update:src/main/java/com/blissstock/mappingSite/repository/ReviewTestRepository.java

// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.data.repository.query.Param;
// import org.springframework.stereotype.Repository;

<<<<<<<< HEAD:src/main/java/com/blissstock/mappingSite/repository/CourseInfoRepository.java
>>>>>>> origin/data-model-update
@Repository
public interface CourseInfoRepository extends JpaRepository<CourseInfo, Long> {
    @Query(nativeQuery = true, value="select * from course_info where first_name ilike %:courseName%")
	public List<CourseInfo> findByCourseName(@Param("courseName")String courseName);
<<<<<<< HEAD

    // @Query(value = "SELECT * FROM join_course_user WHERE course_id=50004;",nativeQuery = true)
    // public List<CourseInfo> findByCourseI();
}
=======
}
========
// @Repository
// public interface ReviewTestRepository extends JpaRepository<ReviewTest, Long> {
//     @Query(nativeQuery = true, value="select * from review where uid_fkey=:uid")
// 	public List<Review> findReviews(@Param("uid")long uid);
// }
>>>>>>>> origin/data-model-update:src/main/java/com/blissstock/mappingSite/repository/ReviewTestRepository.java
>>>>>>> origin/data-model-update
