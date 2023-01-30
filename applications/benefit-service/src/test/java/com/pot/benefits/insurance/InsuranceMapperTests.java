package com.pot.benefits.insurance;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.InsuranceDto;
import com.pot.benefits.entity.Insurance;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class InsuranceMapperTests extends BaseBenefitServiceTestClass {

  @Test
  public void whenMappingInsuranceToDtoAndBackToEntity_shouldGetSameAttributes() {
    Insurance insurance = insuranceHelper.create();
    InsuranceDto insuranceDto = insuranceHelper.getInsuranceMapper().toDto(insurance);
    Insurance resultedInsurance = insuranceHelper.getInsuranceMapper().toEntity(insuranceDto);

    assertEquals(insurance.getName(), resultedInsurance.getName());
    assertEquals(insurance.getType(), resultedInsurance.getType());
    assertEquals(insurance.getContribution(), resultedInsurance.getContribution());
  }
}
