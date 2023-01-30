package com.pot.users.dto.searchparams;

import com.pot.common.enums.CompanyStatus;
import com.pot.database.specification.CompositeType;
import com.pot.users.entity.company.Company;
import com.pot.users.service.specification.CompanySpecificationBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompanySearchParams {
  private String companyName;
  private String countryCode;
  private CompanyStatus status = CompanyStatus.ACTIVE;

  public Specification<Company> buildSpecification(CompositeType compositeType) {
    return new CompanySpecificationBuilder(compositeType)
        .likeCompanyName(companyName)
        .likeCountryCode(countryCode)
        .hasStatus(status)
        .build();
  }
}
