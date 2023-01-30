package com.pot.benefits.service.specification;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.BenefitPackage_;
import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import com.pot.database.specification.BaseSpecification;
import com.pot.database.specification.CompositeType;

import java.time.LocalDate;

public class BenefitPackageSpecificationBuilder extends BaseSpecification<BenefitPackage> {
  public BenefitPackageSpecificationBuilder(CompositeType compositeType) {
    super(compositeType);
  }

  public BenefitPackageSpecificationBuilder likeName(String name) {
    addSpecificationIfNotEmptyOrNull(
        name,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.like(
                criteriaBuilder.lower(root.get(BenefitPackage_.name)),
                "%" + name.toLowerCase() + "%"));
    return this;
  }

  public BenefitPackageSpecificationBuilder hasCompanyId(String companyId) {
    addSpecificationIfNotEmptyOrNull(
        companyId,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(BenefitPackage_.companyId), companyId));
    return this;
  }

  public BenefitPackageSpecificationBuilder greaterThanStartDate(LocalDate startDate) {
    addSpecificationIfNotEmptyOrNull(
        startDate,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.greaterThanOrEqualTo(root.get(BenefitPackage_.startDate), startDate));
    return this;
  }

  public BenefitPackageSpecificationBuilder lessThanOrEqualTo(LocalDate endDate) {
    addSpecificationIfNotEmptyOrNull(
        endDate,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.lessThanOrEqualTo(root.get(BenefitPackage_.endDate), endDate));
    return this;
  }

  public BenefitPackageSpecificationBuilder hasStatus(PackageStatus status) {
    addSpecificationIfNotEmptyOrNull(
        status,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(BenefitPackage_.status), status));
    return this;
  }

  public BenefitPackageSpecificationBuilder hasPayrollFrequency(PayrollFrequency payrollFrequency) {
    addSpecificationIfNotEmptyOrNull(
        payrollFrequency,
        (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get(BenefitPackage_.payrollFrequency), payrollFrequency));
    return this;
  }
}
