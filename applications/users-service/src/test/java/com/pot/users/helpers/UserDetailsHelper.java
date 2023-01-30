package com.pot.users.helpers;

import com.pot.users.entity.user.UserDetails;
import com.pot.users.mapper.UserDetailsMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Getter
public class UserDetailsHelper {
  private final UserDetailsMapper userDetailsMapper;

  public UserDetails create() {
    UserDetails johnSmith = new UserDetails();
    johnSmith.setFirstName("john");
    johnSmith.setLastName("smith");
    johnSmith.setEmail("john@smith.com");
    johnSmith.setSnn("123123123");
    johnSmith.setBirthday(LocalDate.of(2000, 12, 15));
    return johnSmith;
  }
}
