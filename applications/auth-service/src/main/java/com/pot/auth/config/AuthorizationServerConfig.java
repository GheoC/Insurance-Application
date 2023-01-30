package com.pot.auth.config;

import com.pot.auth.properties.ClientProperties;
import com.pot.auth.properties.JksFileProperties;
import com.pot.auth.properties.TokenValidityProperties;
import com.pot.security.config.CustomAccessTokenConverter;
import com.pot.security.config.CustomUserAuthenticationConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

@Configuration
@EnableAuthorizationServer
@RequiredArgsConstructor
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
  private final AuthenticationManager authenticationManager;
  private final ClientProperties clientProperties;
  private final JksFileProperties jksFileProperties;
  private final TokenValidityProperties tokenValidityProperties;
  private final PasswordEncoder passwordEncoder;
  private final UserDetailsService userDetailsService;

  @Override
  public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
    clients
        .inMemory()
            .withClient(clientProperties.getClientId())
            .secret(passwordEncoder.encode(clientProperties.getSecret()))
            .scopes("read")
            .authorizedGrantTypes("password", "refresh_token")
        .accessTokenValiditySeconds(tokenValidityProperties.getAccessTokenValiditySeconds())
        .refreshTokenValiditySeconds(tokenValidityProperties.getRefreshTokenValiditySeconds())
        .and()
            .withClient("benefit-service")
            .secret(passwordEncoder.encode(clientProperties.getSecret()))
            .scopes("internal-request")
            .authorizedGrantTypes("client_credentials")
            .accessTokenValiditySeconds(tokenValidityProperties.getAccessTokenValiditySeconds())
            .refreshTokenValiditySeconds(tokenValidityProperties.getRefreshTokenValiditySeconds());
  }

  @Bean
  public TokenStore tokenStore() {
    return new JwtTokenStore(accessTokenConverter());
  }

  @Bean
  public JwtAccessTokenConverter accessTokenConverter() {
    var converter = new JwtAccessTokenConverter();

    var keyFactory =
        new KeyStoreKeyFactory(
            new ClassPathResource(jksFileProperties.getName()),
            jksFileProperties.getPassword().toCharArray());
    converter.setKeyPair(keyFactory.getKeyPair("jwt"));
    var jwtAccessTokenEnhancer = new CustomAccessTokenConverter();
    var defaultAccessTokenConverter = new DefaultAccessTokenConverter();
    defaultAccessTokenConverter.setUserTokenConverter(new CustomUserAuthenticationConverter());
    jwtAccessTokenEnhancer.setAccessTokenConverter(defaultAccessTokenConverter);
    converter.setAccessTokenConverter(jwtAccessTokenEnhancer);

    return converter;
  }

  @Override
  public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
    endpoints
        .authenticationManager(authenticationManager)
        .userDetailsService(userDetailsService)
        .accessTokenConverter(accessTokenConverter())
        .tokenStore(tokenStore());
  }

  @Override
  public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
    security.checkTokenAccess("isAuthenticated()");
  }
}
