package com.pot.benefits.service;

import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Insurance;
import com.pot.benefits.repository.EnrolmentRepository;
import com.pot.common.enums.EnrolmentStatus;
import com.pot.common.enums.PackageStatus;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrolmentService {
  private final EnrolmentRepository enrolmentRepository;
  private final InsuranceService insuranceService;
  private final EnrolmentProccesingService enrolmentProccesingService;

  @Transactional
  public Enrolment enrollUser(Enrolment enrolment, String companyId) {
    Insurance insurance = insuranceService.get(enrolment.getInsurance().getId());
    if (!insurance.getBenefitPackage().getCompanyId().equals(companyId)) {
      throw new InconsistentDataException("Insurance not found");
    }
    if (insurance.getBenefitPackage().getStatus() != PackageStatus.ACTIVE) {
      throw new InconsistentDataException(
          "Package is NOT active; Cannot enroll on Benefit that is not Active");
    }
    enrolment.setInsurance(insurance);
    enrolment.setContribution(insurance.getContribution());
    enrolment.setPayrollFrequency(insurance.getBenefitPackage().getPayrollFrequency());
    enrolment.setLastProcessedDate(LocalDate.now());
    enrolment.setDueDate(
        enrolmentProccesingService.addToStartDate(
            insurance.getBenefitPackage().getStartDate(),
            1,
            insurance.getBenefitPackage().getPayrollFrequency()));
    enrolment.setStatus(EnrolmentStatus.ACTIVE);

    Enrolment savedEnrolment = enrolmentRepository.save(enrolment);
    enrolmentProccesingService.processEnrolment(savedEnrolment);

    return savedEnrolment;
  }

  public void cancel(String id, String userId, String companyId) {
    Enrolment enrolment = getEnrolment(id, userId, companyId);
    enrolment.setStatus(EnrolmentStatus.CANCELED);
    enrolmentRepository.save(enrolment);
  }

  public List<Enrolment> getEnrolments(String companyId, String userId) {
    return enrolmentRepository.findByUserProfileIdAndCompanyId(userId, companyId);
  }

  public Enrolment getEnrolment(String enrolmentId, String userProfileId, String companyId) {
    return enrolmentRepository
        .findByIdAndUserProfileIdAndCompanyId(enrolmentId, userProfileId, companyId)
        .orElseThrow(() -> new ResourceNotFoundException("Enrolment NOT found"));
  }

  public List<Enrolment> getEnrolmentsWithDue() {
    return enrolmentRepository.findByDueDate(LocalDate.now());
  }

  public Enrolment update(Enrolment enrolment) {
    return enrolmentRepository.save(enrolment);
  }
}
