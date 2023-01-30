package com.pot.benefits.benefitpackage;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.BenefitPackageDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.common.enums.FunctionName;
import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.common.enums.UserStatus;
import com.pot.security.claims.UserClaims;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BenefitPackageIntegrationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";
  private BenefitPackage benefitPackage;

  @BeforeEach
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    benefitPackage = benefitPackageHelper.create();
  }

  @Test
  public void whenCreatingBenefitPackageWithoutToken_ShouldReceiveUnauthorized() throws Exception {
    String url = BASE_URL + benefitPackage.getCompanyId() + "/plan-package";
    mockMvc
        .perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackage))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void whenCreatingBenefitPackageWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    String url = BASE_URL + benefitPackage.getCompanyId() + "/plan-package";
    BenefitPackageDto benefitPackageDto =
        benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackage);
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenCreatingBenefitPackageWithCompanyManagerClaims_ShouldReceiveStatusOk()
      throws Exception {
    String url = BASE_URL + benefitPackage.getCompanyId() + "/plan-package";
    UserClaims companyManager =
        authUserHelper.createCustomUserClaims(
            "random",
            "1",
            UserStatus.ACTIVE,
            List.of(FunctionName.COMPANY_MANAGER),
            "a@b.ro",
            "1",
            benefitPackage.getCompanyId());
    BenefitPackageDto benefitPackageDto =
        benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackage);

    mockMvc
        .perform(
            authorized(post(url), companyManager)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenCreatingBenefitPackageWithUserClaims_ShouldReceiveForbidden() throws Exception {
    String url = BASE_URL + benefitPackage.getCompanyId() + "/plan-package";
    BenefitPackageDto benefitPackageDto =
        benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackage);
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultUserClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void
      givenDifferentCompanyIDs_whenCreatingBenefitPackageWithAdminClaims_ShouldExpectException()
          throws Exception {
    String url = BASE_URL + benefitPackage.getCompanyId() + "/plan-package";
    BenefitPackageDto benefitPackageDto =
        benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackage);
    benefitPackageDto.setCompanyId("DIFFERENT_COMPANY_ID");

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void whenGettingBenefitPackageFromDifferentCompanyWithAdminClaims_ShouldExpectException()
      throws Exception {
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());

    String url = BASE_URL + "RANDOM_COMPANY_ID/plan-package/" + savedBenefitPackage.getId();
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void
      whenUpdatingBenefitPackageWithExpectedFieldsPackageWithAdminClaims_ShouldUpdateSuccessful()
          throws Exception {
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());

    BenefitPackageDto benefitPackageToUpdate =
        benefitPackageHelper.getBenefitPackageMapper().toDto(savedBenefitPackage);
    benefitPackageToUpdate.setStatus(null);
    benefitPackageToUpdate.setName("UPDATED");
    benefitPackageToUpdate.setStartDate(LocalDate.now().plusMonths(1));
    benefitPackageToUpdate.setEndDate(LocalDate.now().plusYears(1));
    benefitPackageToUpdate.setPayrollFrequency(PayrollFrequency.WEEKLY);

    String url =
        BASE_URL
            + benefitPackageToUpdate.getCompanyId()
            + "/plan-package/"
            + benefitPackageToUpdate.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageToUpdate))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("UPDATED"))
        .andExpect(jsonPath("$.startDate").value(benefitPackageToUpdate.getStartDate().toString()))
        .andExpect(jsonPath("$.endDate").value(benefitPackageToUpdate.getEndDate().toString()))
        .andExpect(jsonPath("$.payrollFrequency").value(PayrollFrequency.WEEKLY.name()));
  }

  @Test
  public void
      whenUpdatingBenefitPackageWithNameAndCompanyIDThatAlreadyExistsInDatabaseWithAdminClaims_ShouldExpectException()
          throws Exception {
    BenefitPackage existingBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());

    BenefitPackage benefitPackage = benefitPackageHelper.create();
    benefitPackage.setCompanyId(existingBenefitPackage.getCompanyId());
    BenefitPackage savedBenefitPackage = benefitPackageHelper.saveToDatabase(benefitPackage);

    BenefitPackageDto benefitPackageToUpdate =
        benefitPackageHelper.getBenefitPackageMapper().toDto(savedBenefitPackage);
    benefitPackageToUpdate.setStatus(null);
    benefitPackageToUpdate.setName(existingBenefitPackage.getName());

    String url =
        BASE_URL
            + benefitPackageToUpdate.getCompanyId()
            + "/plan-package/"
            + benefitPackageToUpdate.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageToUpdate))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isConflict());
  }

  @Test
  public void whenDeactivatingBenefitPackageWithAdminClaims_shouldReceiveStatusOk()
      throws Exception {
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    String url =
        BASE_URL
            + savedBenefitPackage.getCompanyId()
            + "/plan-package/"
            + savedBenefitPackage.getId()
            + "/status";

    assertEquals(PackageStatus.ACTIVE, savedBenefitPackage.getStatus());

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    savedBenefitPackage =
        benefitPackageHelper
            .getBenefitPackageRepository()
            .findByIdAndCompanyId(savedBenefitPackage.getId(), savedBenefitPackage.getCompanyId())
            .get();

    assertEquals(PackageStatus.DEACTIVATED, savedBenefitPackage.getStatus());
  }
}
