package com.pot.benefits.helpers;

import com.pot.benefits.entity.BenefitPackage;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import com.pot.benefits.mapper.EnrolmentMapper;
import com.pot.benefits.repository.EnrolmentRepository;
import com.pot.common.enums.EnrolmentStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class EnrolmentHelper {
  private final EnrolmentMapper enrolmentMapper;
  private final EnrolmentRepository enrolmentRepository;
  private final InsuranceHelper insuranceHelper;
  private final BenefitPackageHelper benefitPackageHelper;

  public Enrolment create() {
    BenefitPackage benefitPackage =
        benefitPackageHelper.saveToDatabase(benefitPackageHelper.create());
    Insurance insurance = insuranceHelper.create();
    insurance.setBenefitPackage(benefitPackage);
    Insurance savedInsurance = insuranceHelper.saveToDatabase(insurance);
    Enrolment enrolment = new Enrolment();
    enrolment.setUserProfileId("RANDOM_USER_ID");
    enrolment.setInsurance(savedInsurance);
    enrolment.setContribution(savedInsurance.getContribution());
    enrolment.setElection(BigDecimal.valueOf(999));
    enrolment.setPayrollFrequency(savedInsurance.getBenefitPackage().getPayrollFrequency());
    enrolment.setLastProcessedDate(LocalDate.now());
    enrolment.setDueDate(
        LocalDate.now()
            .plusDays(savedInsurance.getBenefitPackage().getPayrollFrequency().getDays()));
    enrolment.setStatus(EnrolmentStatus.ACTIVE);
    return enrolment;
  }

  public Enrolment saveToDatabase(Enrolment enrolment) {
    return enrolmentRepository.save(enrolment);
  }

  public Enrolment get(String enrolmentId) {
    return enrolmentRepository.findById(enrolmentId).get();
  }

  public void clearDatabase() {
    enrolmentRepository.deleteAll();
  }
}
