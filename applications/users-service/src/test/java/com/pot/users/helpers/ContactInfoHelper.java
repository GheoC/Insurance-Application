package com.pot.users.helpers;

import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.mapper.ContactInfoMapper;
import com.pot.users.repository.ContactInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactInfoHelper {
  private final AddressHelper addressHelper;
  private final PhoneHelper phoneHelper;
  private final ContactInfoMapper contactInfoMapper;
  private final ContactInfoRepository contactInfoRepository;

  public ContactInfoMapper getContactInfoMapper() {
    return contactInfoMapper;
  }

  public ContactInfo create() {
    ContactInfo contactInfo = new ContactInfo();
    contactInfo.getAddresses().add(addressHelper.create());
    contactInfo.getPhones().add(phoneHelper.create());
    return contactInfo;
  }

  public ContactInfo saveContactInfoToDB(ContactInfo contactInfo) {
    return contactInfoRepository.save(contactInfo);
  }
}
