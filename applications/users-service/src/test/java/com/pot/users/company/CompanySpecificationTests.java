package com.pot.users.company;

import com.pot.common.enums.CompanyStatus;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.searchparams.CompanySearchParams;
import com.pot.users.entity.company.Company;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CompanySpecificationTests extends BaseUsersServiceTestClass {

  @BeforeEach
  public void setUp() {
    url = "/users-service/companies";
    userProfileHelper.clearDatabase();
    companyHelper.clearDatabase();
    objectMapper.registerModule(new JavaTimeModule());
    Company company1 = companyHelper.create();
    company1.setCompanyName("coherent");
    company1.setEmail("coherent@gmail.com");
    company1.setSite("www.coherent.com");

    Company company2 = companyHelper.create();
    company2.setCompanyName("coherent2");
    company2.setEmail("coh2@gmail.com");
    company2.setSite("www.coh2.com");
    company2.setCountryCode("GG");

    Company company3 = companyHelper.create();
    company3.setCompanyName("something");
    company3.setCountryCode("RO");
    company3.setEmail("something@gmail.com");
    company3.setSite("something.com");

    companyHelper.saveCompanyToDB(company1);
    companyHelper.saveCompanyToDB(company2);
    companyHelper.saveCompanyToDB(company3);
  }

  @Test
  public void givenAdminClaims_whenSearchingWithParameters01_ShouldGetExpectedValue()
      throws Exception {
    CompanySearchParams companySearchParams =
        new CompanySearchParams("coh", "RO", CompanyStatus.ACTIVE);
    mockMvc
        .perform(
            authorized(
                    post(url + "/search?page=0&size=5"), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companySearchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(1)))
        .andExpect(jsonPath("$.content.[*].companyName", containsInAnyOrder("coherent")));
  }

  @Test
  public void givenAdminClaims_whenSearchingWithParameters02_ShouldGetExpectedValue()
      throws Exception {
    CompanySearchParams companySearchParams =
        new CompanySearchParams("", "RO", CompanyStatus.ACTIVE);
    mockMvc
        .perform(
            authorized(
                    post(url + "/search?page=0&size=5"), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companySearchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(
            jsonPath("$.content.[*].companyName", containsInAnyOrder("coherent", "something")));
  }

  @Test
  public void givenAdminClaims_whenSearchingWithParameters03_ShouldGetExpectedValue()
      throws Exception {
    CompanySearchParams companySearchParams =
        new CompanySearchParams("coh", "", CompanyStatus.ACTIVE);

    mockMvc
        .perform(
            authorized(
                    post(url + "/search?page=0&size=5"), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companySearchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(2)))
        .andExpect(
            jsonPath("$.content.[*].companyName", containsInAnyOrder("coherent", "coherent2")));
  }

  @Test
  public void givenAdminClaims_whenSearchingWithParameters04_ShouldGetExpectedValue()
      throws Exception {
    CompanySearchParams companySearchParams =
        new CompanySearchParams("", "", CompanyStatus.INACTIVE);
    mockMvc
        .perform(
            authorized(
                    post(url + "/search?page=0&size=5"), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companySearchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", hasSize(0)));
  }

  @Test
  public void givenUserClaims_whenSearchingWithParameters_ShouldBeForbidden() throws Exception {
    CompanySearchParams companySearchParams = new CompanySearchParams("", "", CompanyStatus.ACTIVE);
    mockMvc
        .perform(
            authorized(post(url + "/search?page=0&size=5"), authUserHelper.createDefaultUserClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companySearchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
