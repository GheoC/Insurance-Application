package com.pot.benefits.dto.searchparams;

import com.pot.benefits.entity.Claim;
import com.pot.benefits.service.specification.ClaimSpecificationBuilder;
import com.pot.common.enums.ClaimStatus;
import com.pot.database.specification.CompositeType;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Data
public class ClaimSearchParams {
  private String serialNumber;
  private String companyId;
  private LocalDate serviceDate;
  private ClaimStatus status;

  public Specification<Claim> buildSpecification(CompositeType compositeType) {
    return new ClaimSpecificationBuilder(compositeType)
        .likeName(serialNumber)
        .hasCompanyId(companyId)
        .greaterThanServiceDate(serviceDate)
        .hasStatus(status)
        .build();
  }
}
