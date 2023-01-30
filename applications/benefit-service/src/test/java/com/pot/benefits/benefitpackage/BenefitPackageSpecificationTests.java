package com.pot.benefits.benefitpackage;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.searchparams.BenefitPackageSearchParams;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.common.enums.PayrollFrequency;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BenefitPackageSpecificationTests extends BaseBenefitServiceTestClass {

  @BeforeEach
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    claimHelper.clearDatabase();
    enrolmentHelper.clearDatabase();
    insuranceHelper.clearDatabase();
    benefitPackageHelper.clearDatabase();
    BenefitPackage benefitPackage1 = benefitPackageHelper.create();
    benefitPackage1.setName("standard 333");
    benefitPackage1.setCompanyId("company 1");
    benefitPackage1.setPayrollFrequency(PayrollFrequency.WEEKLY);
    benefitPackageHelper.saveToDatabase(benefitPackage1);

    BenefitPackage benefitPackage2 = benefitPackageHelper.create();
    benefitPackage2.setName("VIP 123");
    benefitPackage2.setCompanyId("company 1");
    benefitPackage2.setStartDate(LocalDate.of(2022, 06, 15));
    benefitPackage2.setEndDate(LocalDate.of(2023, 06, 14));
    benefitPackageHelper.saveToDatabase(benefitPackage2);

    BenefitPackage benefitPackage3 = benefitPackageHelper.create();
    benefitPackage3.setName("vip 456");
    benefitPackage3.setCompanyId("company 2");
    benefitPackageHelper.saveToDatabase(benefitPackage3);
  }

  @Test
  public void givenAdminClaims_whenSearchingBenefitPackagesWithParameters01_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/plan-packages/search?page=0&size=5";
    BenefitPackageSearchParams searchParams =
        new BenefitPackageSearchParams("vip", "", null, null, null, null);

    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.content.[*].name", containsInAnyOrder("VIP 123", "vip 456")));
  }

  @Test
  public void givenAdminClaims_whenSearchingBenefitPackagesWithParameters02_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/plan-packages/search?page=0&size=5";
    BenefitPackageSearchParams searchParams =
        new BenefitPackageSearchParams("", "company 1", null, null, null, null);

    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(jsonPath("$.content.[*].name", containsInAnyOrder("VIP 123", "standard 333")));
  }

  @Test
  public void givenAdminClaims_whenSearchingBenefitPackagesWithParameters03_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/plan-packages/search?page=0&size=5";
    BenefitPackageSearchParams searchParams =
        new BenefitPackageSearchParams("standard", "company 1", null, null, null, null);

    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content.[*].name", containsInAnyOrder("standard 333")));
  }

  @Test
  public void givenAdminClaims_whenSearchingBenefitPackagesWithParameters04_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/plan-packages/search?page=0&size=5";
    BenefitPackageSearchParams searchParams =
        new BenefitPackageSearchParams(
            "", "", LocalDate.of(2022, 06, 10), LocalDate.of(2023, 06, 20), null, null);

    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content.[*].name", containsInAnyOrder("VIP 123")));
  }

  @Test
  public void givenAdminClaims_whenSearchingBenefitPackagesWithParameters05_ShouldGetExpectedValue()
      throws Exception {
    String url = "/benefit-service/plan-packages/search?page=0&size=5";
    BenefitPackageSearchParams searchParams =
        new BenefitPackageSearchParams("", "", null, null, null, PayrollFrequency.WEEKLY);

    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content.[*].name", containsInAnyOrder("standard 333")));
  }
}
