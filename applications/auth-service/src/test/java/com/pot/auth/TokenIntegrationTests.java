package com.pot.auth;

import com.pot.common.enums.FunctionName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class TokenIntegrationTests {

  @Autowired
  private MockMvc mockMvc;
  private static final String CONTENT_TYPE = "application/json;charset=UTF-8";
  private String username;
  private String password;
  private String clientId;
  private String secret;
  MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();


  @BeforeEach
  public void setUp() {
    username = "raisa";
    password = "asd";
    clientId = "client";
    secret = "secret";
    parameters.add("grant_type", "password");
    parameters.add("username", username);
    parameters.add("password", password);
  }

  @Test
  public void givenCorrectCredentials_whenAskingForToken_thenReceiveAccessToken() throws Exception {
    String resultString =
        mockMvc
            .perform(
                post("/oauth/token")
                    .params(parameters)
                    .accept(CONTENT_TYPE)
                    .with(httpBasic(clientId, secret))
                    .contentType(MediaType.APPLICATION_JSON))
            .andReturn()
            .getResponse()
            .getContentAsString();

    JacksonJsonParser jsonParser = new JacksonJsonParser();
    String accessToken = jsonParser.parseMap(resultString).get("access_token").toString();

    MultiValueMap<String, String> authUserParameters = new LinkedMultiValueMap<>();
    authUserParameters.add("token", accessToken);
    mockMvc
        .perform(
            post("/oauth/check_token")
                .params(authUserParameters)
                .accept(MediaType.APPLICATION_JSON)
                .with(httpBasic(clientId, secret)))
        .andExpect(jsonPath("$.authorities").value(FunctionName.SUPER_ADMIN.name()));
  }

  @Test
  public void givenUsernameNotInDatabase_whenAskingForToken_thenBadCredentials() throws Exception {
    parameters.replace("username", List.of("wrongUsername"));
    runMvcQueryAndExpectExceptionThrown(parameters, clientId, secret);
  }

  @Test
  public void givenWrongPassword_whenAskingForToken_thenBadCredentials() throws Exception {
    parameters.replace("password", List.of("wrongPassword"));
    runMvcQueryAndExpectExceptionThrown(parameters, clientId, secret);
  }

  @Test
  public void givenWrongClient_whenAskingForToken_thenUnauthorized() throws Exception {
    clientId = "wrongClient";
    runMvcQueryAndExpectExceptionThrown(parameters, clientId, secret);
  }

  @Test
  public void givenWrongSecret_whenAskingForToken_thenUnauthorized() throws Exception {
    secret = "wrongSecret";
    runMvcQueryAndExpectExceptionThrown(parameters, clientId, secret);
  }

  private void runMvcQueryAndExpectExceptionThrown(
      MultiValueMap<String, String> parameters, String client, String secret) throws Exception {
    ResultActions mvcResult =
        mockMvc
            .perform(
                post("/oauth/token")
                    .params(parameters)
                    .accept(CONTENT_TYPE)
                    .with(httpBasic(client, secret))
                    .contentType(MediaType.APPLICATION_JSON))
            .andExpect(
                result -> {
                  boolean containsError =
                      result
                              .getResponse()
                              .getContentAsString()
                              .contains("Bad credentials") // for username and password
                          || result
                              .getResponse()
                              .getErrorMessage()
                              .contains("Unauthorized"); // for client and secret
                  assertTrue((containsError));
                });
  }
}
