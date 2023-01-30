package com.pot.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.pot.auth.token-timings")
@Getter
@Setter
public class TokenValidityProperties {
  private int accessTokenValiditySeconds;
  private int refreshTokenValiditySeconds;
}
