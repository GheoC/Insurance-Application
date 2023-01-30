package com.pot.users.service.specification;

import com.pot.common.enums.CompanyStatus;
import com.pot.database.specification.BaseSpecification;
import com.pot.database.specification.CompositeType;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.company.Company_;

public class CompanySpecificationBuilder extends BaseSpecification<Company> {

  public CompanySpecificationBuilder(CompositeType compositeType) {
    super(compositeType);
  }

  public CompanySpecificationBuilder likeCompanyName(String companyName) {
    addSpecificationIfNotEmptyOrNull(
            companyName,
            (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(Company_.COMPANY_NAME), "%" + companyName + "%"));
    return this;
  }

  public CompanySpecificationBuilder likeCountryCode(String countryCode) {
    addSpecificationIfNotEmptyOrNull(
            countryCode,
            (root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get(Company_.COUNTRY_CODE), "%" + countryCode + "%"));
    return this;
  }

  public CompanySpecificationBuilder hasStatus(CompanyStatus status) {
    specifications.add(
            (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Company_.STATUS), status));
    return this;
  }
}
