package com.pot.benefits.enlrolment;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.EnrolmentDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import com.pot.common.enums.EnrolmentStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EnrolmentIntegrationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";
  @Test
  public void whenEnrollingUserToInsuranceWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    Insurance insurance = insuranceHelper.create();
    insurance.setBenefitPackage(benefitPackage);
    Insurance savedInsurance = insuranceHelper.saveToDatabase(insurance);

    EnrolmentDto enrolmentDto = new EnrolmentDto();
    enrolmentDto.setUserProfileId("user from company");
    enrolmentDto.setInsuranceId(savedInsurance.getId());
    enrolmentDto.setElection(BigDecimal.valueOf(400));

    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/users/"
            + enrolmentDto.getUserProfileId()
            + "/enrolment";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(enrolmentDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userProfileId").value(enrolmentDto.getUserProfileId()))
        .andExpect(jsonPath("$.insuranceId").value(savedInsurance.getId()))
        .andExpect(jsonPath("$.insuranceName").value(savedInsurance.getName()))
        .andExpect(jsonPath("$.insuranceType").value(savedInsurance.getType().name()))
        .andExpect(
            jsonPath("$.contribution").value(savedInsurance.getContribution().doubleValue()));
  }

  @Test
  public void whenCancelingEnrolmentWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());
    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/enrolment/"
            + enrolment.getId();
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    enrolment = enrolmentHelper.get(enrolment.getId());
    assertEquals(enrolment.getStatus(), EnrolmentStatus.CANCELED);
  }

  @Test
  public void whenGettingEnrolmentsForUserWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    Insurance insurance1 = insuranceHelper.create();
    Insurance insurance2 = insuranceHelper.create();
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    insurance1.setBenefitPackage(savedBenefitPackage);
    insurance2.setBenefitPackage(savedBenefitPackage);
    Insurance savedInsurance1 = insuranceHelper.saveToDatabase(insurance1);
    Insurance savedInsurance2 = insuranceHelper.saveToDatabase(insurance2);
    Enrolment enrolment1 = enrolmentHelper.create();
    Enrolment enrolment2 = enrolmentHelper.create();
    enrolment1.setUserProfileId("random");
    enrolment2.setUserProfileId("random");
    enrolment1.setInsurance(savedInsurance1);
    enrolment2.setInsurance(savedInsurance2);
    enrolmentHelper.saveToDatabase(enrolment1);
    enrolmentHelper.saveToDatabase(enrolment2);

    String url =
        BASE_URL
            + enrolment2.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment2.getUserProfileId()
            + "/enrolment";
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(
            jsonPath(
                "$.[*].insuranceName",
                containsInAnyOrder(
                    enrolment1.getInsurance().getName(), enrolment2.getInsurance().getName())));
  }
}
