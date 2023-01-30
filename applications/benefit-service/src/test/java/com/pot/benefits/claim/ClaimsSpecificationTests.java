package com.pot.benefits.claim;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.searchparams.ClaimSearchParams;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import com.pot.common.enums.ClaimStatus;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ClaimsSpecificationTests extends BaseBenefitServiceTestClass {
  @BeforeEach
  public void setUp() {
    claimHelper.clearDatabase();
    objectMapper.registerModule(new JavaTimeModule());
    BenefitPackage benefitPackage1 = benefitPackageHelper.create();
    benefitPackage1.setCompanyId("my_company_id");
    BenefitPackage savedBenefitPackageForMyCompany =
        benefitPackageHelper.saveToDatabase(benefitPackage1);
    BenefitPackage benefitPackage2 = benefitPackageHelper.create();
    benefitPackage2.setCompanyId("other_company");
    BenefitPackage savedBenefitPackageForOtherCompany =
        benefitPackageHelper.saveToDatabase(benefitPackage2);

    Insurance insurance1 = insuranceHelper.create();
    insurance1.setBenefitPackage(savedBenefitPackageForMyCompany);
    Insurance savedInsuranceForMyCompany = insuranceHelper.saveToDatabase(insurance1);

    Insurance insurance2 = insuranceHelper.create();
    insurance2.setBenefitPackage(savedBenefitPackageForOtherCompany);
    Insurance savedInsuranceForOtherCompany = insuranceHelper.saveToDatabase(insurance2);

    Enrolment enrolment = enrolmentHelper.create();
    enrolment.setUserProfileId("user_profile_1");
    enrolment.setInsurance(insurance1);
    Enrolment savedEnrolment = enrolmentHelper.saveToDatabase(enrolment);
    Claim claimNumber01 = claimHelper.create();
    claimNumber01.setSerialNumber("serial_number_1");
    claimNumber01.setServiceDate(LocalDate.of(2022, 10, 01));
    claimNumber01.getClaimInformation().setStatus(ClaimStatus.APPROVED);
    claimNumber01.setEnrolment(savedEnrolment);
    claimHelper.saveToDatabase(claimNumber01);

    Claim claimNumber02 = claimHelper.create();
    claimNumber02.setSerialNumber("random");
    claimNumber02.setServiceDate(LocalDate.of(2022, 10, 15));
    claimNumber02.getClaimInformation().setStatus(ClaimStatus.PENDING);
    claimNumber02.setEnrolment(savedEnrolment);
    claimHelper.saveToDatabase(claimNumber02);

    Enrolment enrolment2 = enrolmentHelper.create();
    enrolment2.setUserProfileId("user_profile_2");
    enrolment2.setInsurance(insurance2);
    Enrolment savedEnrolment2 = enrolmentHelper.saveToDatabase(enrolment2);
    Claim claimNumber03 = claimHelper.create();
    claimNumber03.setSerialNumber("serial_number_2");
    claimNumber03.setServiceDate(LocalDate.of(2022, 10, 25));
    claimNumber03.getClaimInformation().setStatus(ClaimStatus.APPROVED);
    claimNumber03.setEnrolment(savedEnrolment2);
    claimHelper.saveToDatabase(claimNumber03);
  }

  @Test
  public void givenAdminClaims_whenSearchingClaimsWithParameters01_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setCompanyId("my_company_id");
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(
            jsonPath(
                "$.content.[*].serialNumber",
                Matchers.containsInAnyOrder("serial_number_1", "random")));
  }

  @Test
  public void givenAdminClaims_whenSearchingClaimsWithParameters02_ShouldGetExpectedValue()
          throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setCompanyId("other_company");
    mockMvc
            .perform(
                    authorized(get(url), authUserHelper.createDefaultAdminClaims())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(searchParams))
                            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(
                    jsonPath(
                            "$.content.[*].serialNumber",
                            Matchers.containsInAnyOrder("serial_number_2")));
  }

  @Test
  public void givenAdminClaims_whenSearchingClaimsWithParameters03_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setSerialNumber("serial");
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(
            jsonPath(
                "$.content.[*].serialNumber",
                Matchers.containsInAnyOrder("serial_number_1", "serial_number_2")));
  }

  @Test
  public void givenAdminClaims_whenSearchingClaimsWithParameters04_ShouldGetExpectedValue()
          throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setStatus(ClaimStatus.APPROVED);
    mockMvc
            .perform(
                    authorized(get(url), authUserHelper.createDefaultAdminClaims())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(searchParams))
                            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(2))
            .andExpect(
                    jsonPath(
                            "$.content.[*].serialNumber",
                            Matchers.containsInAnyOrder("serial_number_1", "serial_number_2")));
  }

  @Test
  public void givenAdminClaims_whenSearchingClaimsWithParameters05_ShouldGetExpectedValue()
          throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setServiceDate(LocalDate.of(2022,10,19));
    mockMvc
            .perform(
                    authorized(get(url), authUserHelper.createDefaultAdminClaims())
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(searchParams))
                            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.totalElements").value(1))
            .andExpect(
                    jsonPath(
                            "$.content.[*].serialNumber",
                            Matchers.containsInAnyOrder("serial_number_2")));
  }
}
