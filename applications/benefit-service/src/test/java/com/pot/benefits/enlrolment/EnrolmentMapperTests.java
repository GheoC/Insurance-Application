package com.pot.benefits.enlrolment;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.EnrolmentDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class EnrolmentMapperTests extends BaseBenefitServiceTestClass {

  @Test
  public void whenMappingEnrolmentDtoToEntity_shouldExpectCorrectAttributes() {
    EnrolmentDto enrolmentDto = new EnrolmentDto();
    enrolmentDto.setUserProfileId("RANSOM_USER_ID");
    enrolmentDto.setInsuranceId("RANDOM_INSURANCE_ID");
    enrolmentDto.setElection(BigDecimal.valueOf(200));

    Enrolment enrolment = enrolmentHelper.getEnrolmentMapper().toEntity(enrolmentDto);

    assertEquals(enrolmentDto.getInsuranceId(), enrolment.getInsurance().getId());
    assertEquals(enrolmentDto.getUserProfileId(), enrolment.getUserProfileId());
    assertEquals(enrolmentDto.getElection(), enrolment.getElection());
  }

  @Test
  public void whenMappingEntityToDto_shouldExpectCorrectAttributes() {
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    Insurance insurance = insuranceHelper.create();
    insurance.setBenefitPackage(benefitPackage);
    Insurance savedInsurance = insuranceHelper.saveToDatabase(insurance);

    Enrolment enrolment = new Enrolment();
    enrolment.setUserProfileId("RANDOM_USER_ID");
    enrolment.setInsurance(savedInsurance);
    enrolment.setElection(BigDecimal.valueOf(999));
    enrolment.setContribution(savedInsurance.getContribution());

    EnrolmentDto enrolmentDto = enrolmentHelper.getEnrolmentMapper().toDto(enrolment);

    assertEquals(enrolment.getUserProfileId(), enrolmentDto.getUserProfileId());
    assertEquals(enrolment.getInsurance().getId(), enrolmentDto.getInsuranceId());
    assertEquals(enrolment.getContribution(), enrolmentDto.getContribution());
    assertEquals(enrolment.getElection(), enrolmentDto.getElection());
    assertEquals(enrolment.getInsurance().getName(), enrolmentDto.getInsuranceName());
    assertEquals(enrolment.getInsurance().getType(), enrolmentDto.getInsuranceType());
  }
}
