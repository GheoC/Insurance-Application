package com.pot.benefits.service;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.repository.BenefitPackageRepository;
import com.pot.common.enums.PackageStatus;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BenefitPackageService {
  private final BenefitPackageRepository benefitPackageRepository;

  public BenefitPackage create(BenefitPackage benefitPackage) {
    benefitPackage.setStatus(PackageStatus.ACTIVE);
    return benefitPackageRepository.save(benefitPackage);
  }

  public BenefitPackage get(String companyId, String id) {
    return benefitPackageRepository
        .findByIdAndCompanyId(id, companyId)
        .orElseThrow(() -> new ResourceNotFoundException("Package not found"));
  }

  public Page<BenefitPackage> search(
      Specification<BenefitPackage> specification, Pageable pageable) {
    return benefitPackageRepository.findAll(specification, pageable);
  }

  public BenefitPackage update(BenefitPackage benefitPackage) {
    BenefitPackage benefitPackageFromDatabase =
        get(benefitPackage.getCompanyId(), benefitPackage.getId());
    checkStartDateToBeInFuture(benefitPackageFromDatabase.getStartDate());
    benefitPackage.setStatus(benefitPackageFromDatabase.getStatus());
    return benefitPackageRepository.save(benefitPackage);
  }

  public void deactivate(String companyId, String id) {
    BenefitPackage benefitPackage = get(companyId, id);
    if (benefitPackage.getStatus() == PackageStatus.DEACTIVATED) {
      return;
    }
    checkStartDateToBeInFuture(benefitPackage.getStartDate());
    benefitPackage.setStatus(PackageStatus.DEACTIVATED);
    benefitPackageRepository.save(benefitPackage);
  }

  private void checkStartDateToBeInFuture(LocalDate startDate) {
    if (!startDate.isAfter(LocalDate.now())) {
      throw new InconsistentDataException(
          "Benefit Package cannot be updated. It is already in progress");
    }
  }
}
