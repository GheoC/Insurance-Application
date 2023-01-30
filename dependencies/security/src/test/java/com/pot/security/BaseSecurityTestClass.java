package com.pot.security;

import com.pot.security.claims.CustomTestClaims;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.SignatureVerifier;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@AutoConfigureMockMvc
public class BaseSecurityTestClass {
  @Autowired protected MockMvc mockMvc;

  @Autowired private JwtTokenStore jwtTokenStore;
  protected ObjectMapper objectMapper = new ObjectMapper();
  protected static final String AUTHORIZATION = "Authorization";
  protected static final String BEARER = "Bearer ";

  @Autowired private JwtAccessTokenConverter jwtAccessTokenConverter;

  @BeforeEach
  public void disableJwtTokenVerifying() {
    JwtAccessTokenConverter jwtAccessTokenConverter =
        (JwtAccessTokenConverter) ReflectionTestUtils.getField(jwtTokenStore, "jwtTokenEnhancer");

    jwtAccessTokenConverter.setVerifier(
        new SignatureVerifier() {
          @Override
          public void verify(byte[] content, byte[] signature) {}

          @Override
          public String algorithm() {

            return ((SignatureVerifier)
                    ReflectionTestUtils.getField(jwtAccessTokenConverter, "verifier"))
                .algorithm();
          }
        });
  }

  @SneakyThrows
  public String serializeObject(Object object) {
    return objectMapper.writeValueAsString(object);
  }

  protected MockHttpServletRequestBuilder authorized(
      MockHttpServletRequestBuilder requestBuilder, CustomTestClaims claims) {
    return requestBuilder.header(AUTHORIZATION, BEARER + createToken(claims));
  }

  public String createToken(CustomTestClaims claims) {
    Signer signer = (Signer) ReflectionTestUtils.getField(jwtAccessTokenConverter, "signer");

    String claimsAsString = serializeObject(claims);
    Map<String, String> header = new HashMap<>();

    return JwtHelper.encode(claimsAsString, signer, header).getEncoded();
  }
}
