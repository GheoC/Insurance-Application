package com.pot.benefits.service;

import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.ClaimInformation;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.repository.ClaimRepository;
import com.pot.common.enums.ClaimStatus;
import com.pot.common.enums.TransactionType;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ClaimService {
  private final EnrolmentService enrolmentService;
  private final ClaimRepository claimRepository;
  private final TransactionService transactionService;

  @Transactional
  public Claim create(Claim claim, String companyId, String userProfileId) {
    Enrolment enrolment =
        enrolmentService.getEnrolment(claim.getEnrolment().getId(), userProfileId, companyId);
    ClaimInformation claimInformation = new ClaimInformation();
    claimInformation.setStatus(ClaimStatus.PENDING);
    claim.setClaimInformation(claimInformation);
    claim.setEnrolment(enrolment);
    BigDecimal accumulatedContribution =
        transactionService.calculateAccumulatedContribution(claim.getEnrolment().getId());

    if ((accumulatedContribution.compareTo(claim.getAmount()) == 1)
        & (claim.getAmount().compareTo(BigDecimal.valueOf(500)) == -1)) {
      claim.getClaimInformation().setStatus(ClaimStatus.APPROVED);
      transactionService.createTransaction(
          enrolment, claim, claim.getAmount(), TransactionType.PAYMENT);
    }
    if (accumulatedContribution.compareTo(claim.getAmount()) == -1) {
      claim.getClaimInformation().setStatus(ClaimStatus.HOLD);
    }
    return claimRepository.save(claim);
  }

  public Claim get(String claimId, String companyId, String userId) {
    return claimRepository
        .findByIdAndUserProfileIdAndCompanyId(claimId, userId, companyId)
        .orElseThrow(() -> new ResourceNotFoundException("Claim NOT found"));
  }

  public Page<Claim> search(Specification<Claim> specification, Pageable pageable) {
    return claimRepository.findAll(specification, pageable);
  }

  @Transactional
  public void changeStatus(
      String claimId, String companyId, String userId, ClaimInformation claimInformation) {
    Claim claimFromDatabase = get(claimId, companyId, userId);
    if (claimFromDatabase.getClaimInformation().getStatus() != ClaimStatus.PENDING
        && claimFromDatabase.getClaimInformation().getStatus() != ClaimStatus.HOLD) {
      throw new InconsistentDataException("Decision has already been taken for this claim");
    }

    BigDecimal accumulatedContribution =
        transactionService.calculateAccumulatedContribution(
            claimFromDatabase.getEnrolment().getId());

    if (accumulatedContribution.compareTo(claimFromDatabase.getAmount()) == -1) {
      claimFromDatabase.getClaimInformation().setStatus(ClaimStatus.HOLD);
      claimRepository.save(claimFromDatabase);
      throw new InconsistentDataException("Not enough money collected; Claim put on Hold");
    }
    if (claimInformation.getStatus() == ClaimStatus.APPROVED) {
      transactionService.createTransaction(
          claimFromDatabase.getEnrolment(),
          claimFromDatabase,
          claimFromDatabase.getAmount(),
          TransactionType.PAYMENT);
    }
    claimFromDatabase.setClaimInformation(claimInformation);
    claimRepository.save(claimFromDatabase);
  }
}
