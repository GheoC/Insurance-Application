package com.pot.benefits.dto.searchparams;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.service.specification.BenefitPackageSpecificationBuilder;
import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.database.specification.CompositeType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BenefitPackageSearchParams {
  private String name;
  private String companyId;
  private LocalDate startDate;
  private LocalDate endDate;
  private PackageStatus status = PackageStatus.ACTIVE;
  private PayrollFrequency payrollFrequency;

  public Specification<BenefitPackage> buildSpecification(CompositeType compositeType) {
    return new BenefitPackageSpecificationBuilder(compositeType)
        .likeName(name)
        .hasCompanyId(companyId)
        .greaterThanStartDate(startDate)
        .lessThanOrEqualTo(endDate)
        .hasStatus(status)
        .hasPayrollFrequency(payrollFrequency)
        .build();
  }
}
