package com.blissstock.mappingSite.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.blissstock.mappingSite.entity.Contact;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {

}
