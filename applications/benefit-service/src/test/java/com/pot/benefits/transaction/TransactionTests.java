package com.pot.benefits.transaction;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.EnrolmentDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TransactionTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @BeforeEach
  public void setUp() {
    transactionHelper.clearDatabase();
    claimHelper.clearDatabase();
    enrolmentHelper.clearDatabase();
    insuranceHelper.clearDatabase();
    benefitPackageHelper.clearDatabase();
  }

  @Test
  public void whenEnrolmentIsCreatedSameDayAsBenefitPackage_shouldExpectNoTransactions()
      throws Exception {
    BenefitPackage benefitPackage = benefitPackageHelper.create();
    benefitPackage.setStartDate(LocalDate.now());
    benefitPackage.setEndDate(LocalDate.now().plusYears(1));
    BenefitPackage savedBenefitPackage = benefitPackageHelper.saveToDatabase(benefitPackage);
    Insurance insurance = insuranceHelper.create();
    insurance.setBenefitPackage(savedBenefitPackage);
    Insurance savedInsurance = insuranceHelper.saveToDatabase(insurance);

    EnrolmentDto enrolmentDto = new EnrolmentDto();
    enrolmentDto.setUserProfileId("random");
    enrolmentDto.setInsuranceId(savedInsurance.getId());
    enrolmentDto.setElection(BigDecimal.valueOf(400));

    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/users/"
            + enrolmentDto.getUserProfileId()
            + "/enrolment";
    String contentAsString =
        mockMvc
            .perform(
                authorized(post(url), authUserHelper.createDefaultAdminClaims())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(enrolmentDto))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    EnrolmentDto enrolmentDtoResult = objectMapper.readValue(contentAsString, EnrolmentDto.class);

    Enrolment savedEnrolment =
        enrolmentHelper.getEnrolmentRepository().findById(enrolmentDtoResult.getId()).get();

    assertEquals(savedEnrolment.getDueDate(), benefitPackage.getStartDate().plusMonths(1));
    assertEquals(0, transactionHelper.getTransactionRepository().findAll().size());
  }

  @Test
  public void
      whenEnrolmentIsCreatedAfterFourMonthsFromBenefitPackage_shouldExpectContributionCollectedForFourMonths()
          throws Exception {
    BenefitPackage benefitPackage = benefitPackageHelper.create();
    benefitPackage.setStartDate(LocalDate.now().minusMonths(4));
    benefitPackage.setEndDate(LocalDate.now().plusMonths(8));
    BenefitPackage savedBenefitPackage = benefitPackageHelper.saveToDatabase(benefitPackage);
    Insurance insurance = insuranceHelper.create();
    insurance.setContribution(BigDecimal.valueOf(2400));
    insurance.setBenefitPackage(savedBenefitPackage);
    Insurance savedInsurance = insuranceHelper.saveToDatabase(insurance);

    EnrolmentDto enrolmentDto = new EnrolmentDto();
    enrolmentDto.setUserProfileId("random");
    enrolmentDto.setInsuranceId(savedInsurance.getId());
    enrolmentDto.setElection(BigDecimal.valueOf(400));

    String url =
        BASE_URL
            + benefitPackage.getCompanyId()
            + "/users/"
            + enrolmentDto.getUserProfileId()
            + "/enrolment";
    String contentAsString =
        mockMvc
            .perform(
                authorized(post(url), authUserHelper.createDefaultAdminClaims())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(enrolmentDto))
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse()
            .getContentAsString();

    EnrolmentDto enrolmentDtoResult = objectMapper.readValue(contentAsString, EnrolmentDto.class);

    Enrolment savedEnrolment =
        enrolmentHelper.getEnrolmentRepository().findById(enrolmentDtoResult.getId()).get();

    assertEquals(savedEnrolment.getDueDate(), benefitPackage.getStartDate().plusMonths(5));
    assertEquals(1, transactionHelper.getTransactionRepository().findAll().size());
    assertEquals(
        800, transactionHelper.getTransactionRepository().findAll().get(0).getAmount().intValue());
  }
}
