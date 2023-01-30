package com.pot.users.service;

import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactInfoService {
  private final ContactInfoRepository contactInfoRepository;

  @Transactional
  public ContactInfo save(ContactInfo contactInfo) {
    return contactInfoRepository.save(contactInfo);
  }
}
