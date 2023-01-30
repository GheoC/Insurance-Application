package com.pot.users.helpers;

import com.pot.users.entity.contact.Phone;
import com.pot.users.mapper.PhoneMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class PhoneHelper {

  private final PhoneMapper phoneMapper;
  public Phone create() {
    Phone phone = new Phone();
    phone.setPhoneNumber("07771112");
    phone.setCountryCode("RO");
    return phone;
  }
}
