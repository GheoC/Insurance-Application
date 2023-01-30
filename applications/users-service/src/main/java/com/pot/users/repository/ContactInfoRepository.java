package com.pot.users.repository;

import com.pot.users.entity.contact.ContactInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactInfoRepository extends JpaRepository<ContactInfo, String> {
}
