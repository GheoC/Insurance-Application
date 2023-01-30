package com.pot.security.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "com.pot.security")
@Getter
@Setter
public class KeyProperties
{
    private String publicKey;
}
