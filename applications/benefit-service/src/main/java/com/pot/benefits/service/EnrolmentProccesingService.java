package com.pot.benefits.service;

import com.pot.benefits.entity.Enrolment;
import com.pot.common.enums.PayrollFrequency;
import com.pot.common.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrolmentProccesingService {
  private final TransactionService transactionService;
  @Lazy private final EnrolmentService enrolmentService;

  @Transactional
  @Scheduled(cron = "* * 6 * * *")
  public void calculateContribution() {
    List<Enrolment> enrolments = enrolmentService.getEnrolmentsWithDue();
    enrolments.forEach(this::processEnrolment);
  }

  public long numberOfInstallments(
      LocalDate startDate, LocalDate endDate, PayrollFrequency frequency) {
    if (frequency == PayrollFrequency.MONTHLY) {
      return ChronoUnit.MONTHS.between(startDate, endDate);
    }
    return ChronoUnit.WEEKS.between(startDate, endDate);
  }

  public void processEnrolment(Enrolment enrolment) {
    long timePassed =
        numberOfInstallments(
            enrolment.getInsurance().getBenefitPackage().getStartDate(),
            enrolment.getCreatedAt().toLocalDate(),
            enrolment.getPayrollFrequency());

    long numberOfInstallments =
        numberOfInstallments(
            enrolment.getInsurance().getBenefitPackage().getStartDate(),
            enrolment.getInsurance().getBenefitPackage().getEndDate(),
            enrolment.getPayrollFrequency());

    BigDecimal contributionInstallment =
        enrolment
            .getContribution()
            .divide(BigDecimal.valueOf(numberOfInstallments), 2, RoundingMode.HALF_EVEN);

    processEnrolmentForPastTime(enrolment, timePassed, contributionInstallment);
    processEnrolmentForPresentTime(enrolment, numberOfInstallments, contributionInstallment);
  }

  private void processEnrolmentForPresentTime(
      Enrolment enrolment, long numberOfInstallments, BigDecimal contributionInstallment) {
    if (enrolment.getDueDate().equals(LocalDate.now())) {
      LocalDate nextDueDate =
          enrolment.getDueDate().plusDays(enrolment.getPayrollFrequency().getDays());
      if (enrolment.getInsurance().getBenefitPackage().getEndDate().isBefore(nextDueDate)) {
        nextDueDate = enrolment.getInsurance().getBenefitPackage().getEndDate();
        contributionInstallment =
            enrolment
                .getContribution()
                .subtract(
                    contributionInstallment.multiply(
                        BigDecimal.valueOf((numberOfInstallments - 1))));
      }
      enrolment.setLastProcessedDate(LocalDate.now());
      enrolment.setDueDate(nextDueDate);
      enrolmentService.update(enrolment);
      transactionService.createTransaction(
          enrolment, null, contributionInstallment, TransactionType.COLLECT);
    }
  }

  private void processEnrolmentForPastTime(
      Enrolment enrolment, long timePassed, BigDecimal contributionInstallment) {
    if (enrolment.getCreatedAt().toLocalDate().equals(LocalDate.now()) & timePassed > 1) {
      enrolment.setDueDate(
          addToStartDate(
              enrolment.getInsurance().getBenefitPackage().getStartDate(),
              (timePassed + 1),
              enrolment.getPayrollFrequency()));

      enrolmentService.update(enrolment);

      BigDecimal contributionForPastTime =
          contributionInstallment.multiply(BigDecimal.valueOf(timePassed));
      transactionService.createTransaction(
          enrolment, null, contributionForPastTime, TransactionType.COLLECT);
    }
  }

  public LocalDate addToStartDate(LocalDate startDate, long units, PayrollFrequency frequency) {
    if (frequency == PayrollFrequency.MONTHLY) {
      return startDate.plusMonths(units);
    }
    return startDate.plusWeeks(units);
  }
}
