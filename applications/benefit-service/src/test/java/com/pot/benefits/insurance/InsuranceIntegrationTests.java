package com.pot.benefits.insurance;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.InsuranceDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Insurance;
import com.pot.common.enums.InsuranceType;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class InsuranceIntegrationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @Test
  public void whenCreatingInsuranceWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    InsuranceDto insuranceDto =
        insuranceHelper.getInsuranceMapper().toDto(insuranceHelper.create());
    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/plan-package/"
            + benefitPackage.getId()
            + "/insurances";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenCreatingInsuranceWithUserClaims_ShouldReceiveForbidden() throws Exception {
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    InsuranceDto insuranceDto =
        insuranceHelper.getInsuranceMapper().toDto(insuranceHelper.create());
    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/plan-package/"
            + benefitPackage.getId()
            + "/insurances";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultUserClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void
      givenUnknownBenefitPackage_whenCreatingInsuranceWithAdminClaims_ShouldReceiveNotFound()
          throws Exception {
    InsuranceDto insuranceDto =
        insuranceHelper.getInsuranceMapper().toDto(insuranceHelper.create());
    String url = BASE_URL + "RANDOM_COMPANY_ID/plan-package/RANDOM_PACKAGE_ID/insurances";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenUpdatingInsuranceWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    Insurance insurance = insuranceHelper.create();
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    insurance.setBenefitPackage(benefitPackage);
    Insurance savedInsurance = insuranceHelper.getInsuranceRepository().save(insurance);
    InsuranceDto insuranceDto = insuranceHelper.getInsuranceMapper().toDto(savedInsurance);
    insuranceDto.setName("Updated");
    insuranceDto.setContribution(BigDecimal.valueOf(999));
    insuranceDto.setType(InsuranceType.DENTAL);
    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/plan-package/"
            + benefitPackage.getId()
            + "/insurances/"
            + savedInsurance.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(insuranceDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Updated"))
        .andExpect(jsonPath("$.type").value(InsuranceType.DENTAL.name()))
        .andExpect(jsonPath("$.contribution").value("999"));
  }
}
