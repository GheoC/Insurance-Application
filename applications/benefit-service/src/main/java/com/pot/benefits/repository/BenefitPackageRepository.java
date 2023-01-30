package com.pot.benefits.repository;

import com.pot.benefits.entity.BenefitPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface BenefitPackageRepository
    extends JpaRepository<BenefitPackage, String>, JpaSpecificationExecutor<BenefitPackage> {
  Optional<BenefitPackage> findByIdAndCompanyId(String id, String companyId);
}
