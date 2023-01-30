package com.pot.benefits.service;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Insurance;
import com.pot.benefits.repository.InsuranceRepository;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class InsuranceService {
  private final InsuranceRepository insuranceRepository;
  private final BenefitPackageService benefitPackageService;

  public Insurance create(Insurance insurance, String companyId, String benefitPackageId) {
    BenefitPackage benefitPackage = benefitPackageService.get(companyId, benefitPackageId);
    insurance.setBenefitPackage(benefitPackage);
    return insuranceRepository.save(insurance);
  }

  public Insurance update(Insurance insurance, String benefitPackageId, String companyId) {
    Insurance insuranceFromDB =
        insuranceRepository
            .findByIdAndBenefitPackageIdAndBenefitPackage_CompanyId(
                insurance.getId(), benefitPackageId, companyId)
            .orElseThrow(() -> new ResourceNotFoundException("Insurance not found"));
    if (insuranceFromDB.getBenefitPackage().getStartDate().isBefore(LocalDate.now().plusDays(1))) {
      throw new InconsistentDataException(
          "Insurance cannot be updated. Package is already in progress");
    }

    insurance.setBenefitPackage(insuranceFromDB.getBenefitPackage());
    return insuranceRepository.save(insurance);
  }

  public Insurance get(String insuranceId) {
    return insuranceRepository
        .findById(insuranceId)
        .orElseThrow(() -> new ResourceNotFoundException("Insurance Not Found"));
  }
}
