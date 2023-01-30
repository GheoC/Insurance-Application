package com.pot.benefits.benefitpackage;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.BenefitPackageDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.common.enums.PackageStatus;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.UUID;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class BenefitPackageValidationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @Test
  public void
      givenIncorrectInputs_whenCreatingBenefitPackageWithAdminClaims_ShouldExpectExceptions()
          throws Exception {

    BenefitPackageDto benefitPackageDto = new BenefitPackageDto();
    benefitPackageDto.setCompanyId(UUID.randomUUID().toString());
    benefitPackageDto.setId("random");
    benefitPackageDto.setStatus(PackageStatus.ACTIVE);

    String url = BASE_URL + benefitPackageDto.getCompanyId() + "/plan-package";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder(
                    "id", "status", "name", "endDate", "startDate", "payrollFrequency", "check object")));
  }

  @Test
  public void
      givenWrongStartDateAndEndDate_whenCreatingBenefitPackageWithAdminClaims_ShouldExpectExceptions()
          throws Exception {
    objectMapper.registerModule(new JavaTimeModule());
    BenefitPackageDto benefitPackageDto =
        benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackageHelper.create());
    benefitPackageDto.setStatus(null);
    benefitPackageDto.setStartDate(LocalDate.now().plusDays(20));
    benefitPackageDto.setEndDate(LocalDate.now().plusDays(15)); // we set endDate before startDate

    String url = BASE_URL + benefitPackageDto.getCompanyId() + "/plan-package";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(benefitPackageDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void
      givenIncorrectInputs_whenUpdatingBenefitPackageWithAdminClaims_shouldExpectExceptions()
          throws Exception {
    objectMapper.registerModule(new JavaTimeModule());
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());

    BenefitPackageDto benefitPackageToUpdate =
        benefitPackageHelper.getBenefitPackageMapper().toDto(savedBenefitPackage);
    benefitPackageToUpdate.setId(null);

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
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.[*].field", containsInAnyOrder("status", "id")));
  }

  @Test
  public void
      givenWrongStartDateAndEndDate_whenUpdatingBenefitPackageWithAdminClaims_shouldExpectExceptions()
          throws Exception {
    objectMapper.registerModule(new JavaTimeModule());
    BenefitPackage savedBenefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());

    BenefitPackageDto benefitPackageToUpdate =
        benefitPackageHelper.getBenefitPackageMapper().toDto(savedBenefitPackage);
    benefitPackageToUpdate.setStatus(null);
    benefitPackageToUpdate.setStartDate(LocalDate.now().plusDays(20));
    benefitPackageToUpdate.setEndDate(LocalDate.now().plusDays(15));

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
        .andExpect(status().isBadRequest());
  }
}
