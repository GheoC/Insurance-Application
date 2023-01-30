package com.pot.security.config;

import com.pot.security.entity.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;

import java.util.*;

@RequiredArgsConstructor
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

  @Override
  public Authentication extractAuthentication(Map<String, ?> map) {
    if (map.get("scope")!= null){
    if (map.get("scope").toString().contains("internal-request")) {
      AuthUser internalUser =
          new AuthUser(
              map.get("client_id").toString(),
              "N/A",
              Set.of(new SimpleGrantedAuthority("SUPER_ADMIN")));
      return new UsernamePasswordAuthenticationToken(
          internalUser, "N/A", internalUser.getAuthorities());
    }}
    AuthUser user =
        new AuthUser(
            map.get("user_name").toString(),
            "N/A",
            getAuthority((List<String>) map.get("authorities")));
    user.setUserId(map.get("userId").toString());
    user.setUserProfileId(
        Optional.ofNullable(map.get("userProfileId")).map(it -> it.toString()).orElse(null));
    user.setCompanyId(
        Optional.ofNullable(map.get("companyId")).map(it -> it.toString()).orElse(null));

    return new UsernamePasswordAuthenticationToken(user, "N/A", user.getAuthorities());
  }

  private Set<SimpleGrantedAuthority> getAuthority(List<String> tokenAuthorities) {
    Set<SimpleGrantedAuthority> authorities = new HashSet<>();
    tokenAuthorities.forEach(authority -> authorities.add(new SimpleGrantedAuthority(authority)));
    return authorities;
  }
}
