package com.blissstock.mappingSite.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.blissstock.mappingSite.entity.TestExaminee;
import com.blissstock.mappingSite.entity.UserInfo;

public interface TestExamineeRepository extends JpaRepository<TestExaminee, Long> {
    @Query(value = "Select * from test_examinee where test_id = :test_id", nativeQuery = true)
    public List<TestExaminee> getExamineeByTest(@Param("test_id") Long test_id);

    @Query(value = "select test_examinee.* from user_info, user_account, test_examinee where user_info.account_id = user_account.account_id and test_examinee.test_id = :test_id and test_examinee.examinee_student_id = user_info.account_id and user_account.role = 'ROLE_STUDENT' and lower(user_name) like :userName%", nativeQuery = true)
    public List<TestExaminee> findByNameandTestId(@Param("userName") String userName, @Param("test_id") Long test_id);

    @Query(value = "select test_examinee.* from guest, test_examinee where test_examinee.test_id = :test_id and test_examinee.examinee_guest_id = guest.guest_id and lower(name) like :guestName%", nativeQuery = true)
    public List<TestExaminee> findByGuestNameandTestId(@Param("guestName") String guestName, @Param("test_id") Long test_id);

    @Query(value = "Select * from test_examinee where examinee_student_id = :id and test_id = :test_id limit 1", nativeQuery = true)
    public TestExaminee getStudentByID(@Param("id") Long id, @Param("test_id") Long test_id);

    @Query(value = "Select * from test_examinee where test_id = :test_id and examinee_student_id = :uid limit 1", nativeQuery = true)
    public TestExaminee findByTestIdAndUid(@Param("test_id") Long test_id, @Param("uid") Long uid);

    @Query(value = "Select * from test_examinee where test_id = :test_id and examinee_guest_id = :guest_id limit 1", nativeQuery = true)
    public TestExaminee findByTestIdAndGuestId(@Param("test_id") Long test_id, @Param("guest_id") Long guest_id);
}
