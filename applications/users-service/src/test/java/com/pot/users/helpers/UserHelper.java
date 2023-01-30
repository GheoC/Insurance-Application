package com.pot.users.helpers;

import com.pot.users.entity.user.User;
import com.pot.users.mapper.UserMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class UserHelper {
  private final UserMapper userMapper;

  public User create() {
    User user = new User();
    user.setUsername("john"+ UUID.randomUUID());
    user.setPassword("asd");
    return user;
  }
}
