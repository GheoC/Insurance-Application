package com.pot.auth.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.pot.auth.client")
@Getter
@Setter
public class ClientProperties {
  private String clientId;
  private String secret;
}
