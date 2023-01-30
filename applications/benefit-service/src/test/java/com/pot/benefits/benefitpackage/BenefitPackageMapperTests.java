package com.pot.benefits.benefitpackage;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.BenefitPackageDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.common.enums.PackageStatus;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class BenefitPackageMapperTests extends BaseBenefitServiceTestClass {

  @Test
  public void whenMappingBenefitPackageToDtoAndBackToEntity_ShouldExpectSameAttributes() {
    BenefitPackage benefitPackage = benefitPackageHelper.create();
    benefitPackage.setId(UUID.randomUUID().toString());
    benefitPackage.setStatus(PackageStatus.ACTIVE);

    BenefitPackageDto dto = benefitPackageHelper.getBenefitPackageMapper().toDto(benefitPackage);

    assertEquals(benefitPackage.getId(), dto.getId());
    assertEquals(benefitPackage.getStatus(), dto.getStatus());
    dto.setId(null);
    dto.setStatus(null);

    BenefitPackage resultedBenefitPackage =
        benefitPackageHelper.getBenefitPackageMapper().toEntity(dto);

    assertEquals(benefitPackage.getCompanyId(), resultedBenefitPackage.getCompanyId());
    assertEquals(benefitPackage.getName(), resultedBenefitPackage.getName());
    assertEquals(benefitPackage.getStartDate(), resultedBenefitPackage.getStartDate());
    assertEquals(benefitPackage.getEndDate(), resultedBenefitPackage.getEndDate());
    assertEquals(
        benefitPackage.getPayrollFrequency(), resultedBenefitPackage.getPayrollFrequency());
    assertEquals(null, resultedBenefitPackage.getId());
    assertEquals(null, resultedBenefitPackage.getStatus());
  }
}
