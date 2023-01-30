package com.pot.auth.service;

import com.pot.auth.entity.*;
import com.pot.common.enums.UserStatus;
import com.pot.common.enums.UserType;
import com.pot.security.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UserService userService;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userService.get(username);
    BaseProfile userProfile =
        user.getBaseProfiles().stream()
            .filter(baseProfile -> baseProfile.getStatus() == UserStatus.ACTIVE)
            .findFirst()
            .orElseThrow(() -> new BadCredentialsException("Active User NOT found"));

    if (userProfile.getType() == UserType.USER) {
      List<SimpleGrantedAuthority> authorities =
          ((UserProfile) userProfile)
              .getRoles().stream()
                  .map(ClientRole::getName)
                  .map(Enum::name)
                  .map(SimpleGrantedAuthority::new)
                  .collect(Collectors.toList());
      AuthUser userType = new AuthUser(user.getUsername(), user.getPassword(), authorities);
      userType.setUserId(user.getId());
      userType.setUserProfileId(userProfile.getId());
      userType.setCompanyId(((UserProfile) userProfile).getCompanyId());

      return userType;
    }
    List<SimpleGrantedAuthority> authorities =
        ((AdminProfile) userProfile)
            .getRoles().stream()
                .map(Function::getName)
                .map(Enum::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    AuthUser adminType = new AuthUser(user.getUsername(), user.getPassword(), authorities);
    adminType.setUserId(user.getId());

    return adminType;
  }
}
