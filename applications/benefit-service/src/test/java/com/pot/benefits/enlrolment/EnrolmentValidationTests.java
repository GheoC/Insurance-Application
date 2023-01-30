package com.pot.benefits.enlrolment;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.EnrolmentDto;
import com.pot.common.enums.InsuranceType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EnrolmentValidationTests extends BaseBenefitServiceTestClass {
  @Test
  public void
      givenIncorrectInputs_whenEnrollingUserToInsuranceWithAdminClaims_ShouldExpectException()
          throws Exception {
    EnrolmentDto enrolmentDto = new EnrolmentDto();
    enrolmentDto.setId("RANDOM_USER_ID");
    enrolmentDto.setContribution(BigDecimal.valueOf(2000));
    enrolmentDto.setInsuranceName("RANDOM");
    enrolmentDto.setInsuranceType(InsuranceType.DENTAL);
    String url = "/benefit-service/companies/RANDOM_COMPANY_ID/users/RANDOM_USER_ID/enrolment";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrolmentDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder(
                    "id",
                    "userProfileId",
                    "insuranceId",
                    "election",
                    "contribution",
                    "insuranceName",
                    "insuranceType")));
  }
}
