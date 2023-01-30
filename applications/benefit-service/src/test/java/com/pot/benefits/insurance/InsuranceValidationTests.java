package com.pot.benefits.insurance;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.InsuranceDto;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InsuranceValidationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @Test
  public void givenIncorrectInputs_whenCreatingInsuranceWithAdminClaims_ShouldExpectExceptions()
      throws Exception {
    InsuranceDto insuranceDto = new InsuranceDto();
    String url = BASE_URL + "RANDOM_COMPANY_ID/plan-package/RANDOM_PACKAGE_ID/insurances";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors.[*].field", containsInAnyOrder("name", "type", "contribution")));
  }

  @Test
  public void givenIncorrectInputs_whenUpdatingInsuranceWithAdminClaims_ShouldExpectExceptions()
      throws Exception {
    InsuranceDto insuranceDto = new InsuranceDto();
    String url =
        BASE_URL
            + "RANDOM_COMPANY_ID/plan-package/RANDOM_PACKAGE_ID/insurances/RANDOM_INSURANCE_ID";
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field", containsInAnyOrder("id", "name", "type", "contribution")));
  }
}
