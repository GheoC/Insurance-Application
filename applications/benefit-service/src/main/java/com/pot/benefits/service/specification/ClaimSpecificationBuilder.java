package com.pot.benefits.service.specification;

import com.pot.benefits.entity.*;
import com.pot.common.enums.ClaimStatus;
import com.pot.database.specification.BaseSpecification;
import com.pot.database.specification.CompositeType;

import javax.persistence.criteria.Join;
import java.time.LocalDate;

public class ClaimSpecificationBuilder extends BaseSpecification<Claim> {
  public ClaimSpecificationBuilder(CompositeType compositeType) {
    super(compositeType);
  }
    public ClaimSpecificationBuilder likeName(String serialNumber) {
        addSpecificationIfNotEmptyOrNull(
                serialNumber,
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get(Claim_.serialNumber)),
                                "%" + serialNumber.toLowerCase() + "%"));
        return this;
    }

    public ClaimSpecificationBuilder hasCompanyId(String companyId) {
        addSpecificationIfNotEmptyOrNull(
                companyId,
                (root, query, criteriaBuilder) -> {
                    Join<Claim, Enrolment> enrolmentClaims = root.join(Claim_.enrolment);
                    Join<Enrolment, Insurance> insuranceEnrolments =
                            enrolmentClaims.join(Enrolment_.insurance);
                    Join<Insurance, BenefitPackage> benefitPackageInsurances =
                            insuranceEnrolments.join(Insurance_.benefitPackage);
                    return criteriaBuilder.equal(
                            criteriaBuilder.lower(benefitPackageInsurances.get(BenefitPackage_.companyId)),
                            companyId.toLowerCase());
                });
        return this;
    }

    public ClaimSpecificationBuilder greaterThanServiceDate(LocalDate serviceDate) {
        addSpecificationIfNotEmptyOrNull(
                serviceDate,
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.greaterThanOrEqualTo(root.get(Claim_.serviceDate), serviceDate));
        return this;
    }

    public ClaimSpecificationBuilder hasStatus(ClaimStatus status) {
        addSpecificationIfNotEmptyOrNull(
                status,
                (root, query, criteriaBuilder) ->
                        criteriaBuilder.equal(
                                root.get(Claim_.claimInformation).get(ClaimInformation_.status), status));
        return this;
    }
}
