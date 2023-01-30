package com.pot.benefits.repository;

import com.pot.benefits.entity.Insurance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<Insurance, String> {

  Optional<Insurance> findByIdAndBenefitPackageIdAndBenefitPackage_CompanyId(
      String id, String benefitPackageId, String benefitPackageCompanyId);
}
