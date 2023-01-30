package com.pot.benefits.claim;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.ClaimDto;
import com.pot.benefits.entity.Enrolment;
import com.pot.common.enums.ClaimStatus;
import com.pot.common.enums.InsuranceType;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ClaimValidationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @Test
  public void givenIncorrectInputs_whenCreatingClaimWithAdminClaims_ShouldExpectException()
      throws Exception {
    objectMapper.registerModule(new JavaTimeModule());
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());

    ClaimDto claimDto = new ClaimDto();
    claimDto.setInsuranceType(InsuranceType.DENTAL);
    claimDto.setStatus(ClaimStatus.PENDING);
    claimDto.setId("random");

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder(
                    "id",
                    "serialNumber",
                    "serviceDate",
                    "enrolmentId",
                    "amount",
                    "status",
                    "insuranceType")));
  }
}
