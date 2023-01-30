package com.pot.benefits.helpers;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.mapper.BenefitPackageMapper;
import com.pot.benefits.repository.BenefitPackageRepository;
import com.pot.common.enums.PackageStatus;
import com.pot.common.enums.PayrollFrequency;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class BenefitPackageHelper {
  private final BenefitPackageRepository benefitPackageRepository;
  private final BenefitPackageMapper benefitPackageMapper;

  public BenefitPackage create() {
    BenefitPackage benefitPackage = new BenefitPackage();
    benefitPackage.setName("package " + UUID.randomUUID());
    benefitPackage.setCompanyId(UUID.randomUUID().toString());
    benefitPackage.setPayrollFrequency(PayrollFrequency.MONTHLY);
    benefitPackage.setStartDate(LocalDate.now().plusDays(1));
    benefitPackage.setEndDate(LocalDate.now().plusYears(1));
    return benefitPackage;
  }

  public BenefitPackage saveToDatabase(BenefitPackage benefitPackage) {
    benefitPackage.setStatus(PackageStatus.ACTIVE);
    return benefitPackageRepository.save(benefitPackage);
  }

  public void clearDatabase() {
    benefitPackageRepository.deleteAll();
  }
}
