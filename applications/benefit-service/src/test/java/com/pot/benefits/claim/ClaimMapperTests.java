package com.pot.benefits.claim;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.ClaimDto;
import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.Enrolment;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ClaimMapperTests extends BaseBenefitServiceTestClass {
  @Test
  public void whenMappingClaimDtoToEntity_ShouldExpectSameAttributes() {
    ClaimDto claimDto = new ClaimDto();
    claimDto.setSerialNumber("randomSN");
    claimDto.setAmount(BigDecimal.valueOf(50));
    claimDto.setServiceDate(LocalDate.of(2022, 10, 28));
    claimDto.setEnrolmentId("randomId");

    Claim claim = claimHelper.getClaimMapper().toEntity(claimDto);
    assertEquals(claimDto.getAmount(), claim.getAmount());
    assertEquals(claimDto.getSerialNumber(), claim.getSerialNumber());
    assertEquals(claimDto.getServiceDate(), claim.getServiceDate());
    assertEquals(claimDto.getEnrolmentId(), claim.getEnrolment().getId());
  }

  @Test
  public void whenMappingClaimEntityToDto_ShouldExpectSameAttributes() {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());
    Claim claim = claimHelper.create();
    claim.setEnrolment(enrolment);
    Claim savedClaim = claimHelper.saveToDatabase(claim);

    ClaimDto claimDto = claimHelper.getClaimMapper().toDto(savedClaim);

    assertEquals(savedClaim.getId(), claimDto.getId());
    assertEquals(savedClaim.getSerialNumber(), claimDto.getSerialNumber());
    assertEquals(savedClaim.getAmount(), claimDto.getAmount());
    assertEquals(savedClaim.getEnrolment().getId(), claimDto.getEnrolmentId());
    assertEquals(savedClaim.getServiceDate(), claimDto.getServiceDate());
    assertEquals(savedClaim.getClaimInformation().getStatus(), claimDto.getStatus());
    assertEquals(savedClaim.getEnrolment().getInsurance().getType(), claimDto.getInsuranceType());
  }
}
