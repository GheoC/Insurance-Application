package com.pot.auth.service;

import com.pot.auth.entity.User;
import com.pot.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;

  public User get(String username) {
    return userRepository
        .findByUsername(username)
        .orElseThrow(() -> new InternalAuthenticationServiceException("Bad credentials"));
  }
}
