package com.pot.security.config;

import com.pot.security.properties.KeyProperties;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, securedEnabled = true, prePostEnabled = true)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

  private final KeyProperties keyProperties;

  @ConditionalOnMissingBean
  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(converter());
  }

  @ConditionalOnMissingBean
  @Bean
  public JwtAccessTokenConverter converter() {

    var converter = new JwtAccessTokenConverter();

    var jwtAccessTokenEnhancer = new CustomAccessTokenConverter();
    var defaultAccessTokenConverter = new DefaultAccessTokenConverter();
    defaultAccessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
    jwtAccessTokenEnhancer.setAccessTokenConverter(defaultAccessTokenConverter);
    converter.setAccessTokenConverter(jwtAccessTokenEnhancer);

    converter.setVerifierKey(keyProperties.getPublicKey());
    return converter;
  }

  @Override
  public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
    resources.tokenStore(tokenStore());
  }

  @Override
  public void configure(HttpSecurity http) throws Exception {
    http.csrf()
        .disable()
        .anonymous()
        .disable()
        .authorizeRequests()
        .antMatchers("/oauth/token").permitAll()
        .anyRequest().authenticated();
  }
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public GrantedAuthorityDefaults grantedAuthorityDefaults() {
    return new GrantedAuthorityDefaults(Strings.EMPTY);
  }

}
