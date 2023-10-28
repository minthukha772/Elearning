package com.blissstock.mappingSite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.blissstock.mappingSite.entity.Contact;
import com.blissstock.mappingSite.entity.Test;
import com.blissstock.mappingSite.entity.TestExaminee;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

    @Query(value = "SELECT * FROM contact order by contact_id DESC", nativeQuery = true)
    List<Contact> getListByEmail();

    @Query(value = "SELECT * FROM contact WHERE contact_id=:contactId", nativeQuery = true)
    public List<Contact> findByUID(@Param("contactId") Long contactId);

    @Query(value = "SELECT * FROM contact WHERE DATE(inquiry_date) BETWEEN DATE(:fromDate) AND DATE(:toDate) ORDER BY contact_id DESC", nativeQuery = true)
    public List<Contact> getListByDate(@Param("fromDate") Date fromDate, @Param("toDate") Date toDate);

    @Query(value = "Select * from contact where details = :details order by contact_id desc", nativeQuery = true)
    public List<Contact> getListBySendType(@Param("details") String details);

    @Query(value = "Select * from contact where contact_id = :contact_id", nativeQuery = true)
    public Contact getContactByUser(@Param("contact_id") Long contact_id);

}
