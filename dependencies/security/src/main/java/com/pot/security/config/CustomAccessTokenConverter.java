package com.pot.security.config;

import com.pot.security.entity.AuthUser;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.HashMap;
import java.util.Map;

public class CustomAccessTokenConverter extends JwtAccessTokenConverter {

  @Override
  public Map<String, ?> convertAccessToken(
      OAuth2AccessToken token, OAuth2Authentication authentication) {
    Map<String, Object> tokenValues =
        new HashMap<>(super.convertAccessToken(token, authentication));

    if (authentication.isClientOnly()) {
      return tokenValues;
    }

    if (authentication.getUserAuthentication().getPrincipal() instanceof AuthUser) {
      AuthUser authUser = (AuthUser) authentication.getUserAuthentication().getPrincipal();
      tokenValues.put("userId", authUser.getUserId());
      if (!isRefreshToken(token)) {
        tokenValues.put("companyId", authUser.getCompanyId());
        tokenValues.put("userProfileId", authUser.getUserProfileId());
      }
    }
    return tokenValues;
  }
}
