package com.pot.benefits.service;

import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Transaction;
import com.pot.benefits.repository.TransactionRepository;
import com.pot.common.enums.TransactionType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

  private final TransactionRepository transactionRepository;

  public void createTransaction(
      Enrolment enrolment,
      Claim claim,
      BigDecimal contributionInstallment,
      TransactionType transactionType) {
    Transaction transaction = new Transaction();
    transaction.setAmount(contributionInstallment);
    transaction.setEnrolment(enrolment);
    transaction.setClaim(claim);
    transaction.setType(transactionType);
    Transaction save = transactionRepository.save(transaction);
  }

  public BigDecimal calculateAccumulatedContribution(String enrolmentId) {
    List<Transaction> transactionsForEnrolment =
        transactionRepository.findByEnrolmentId(enrolmentId);
    BigDecimal sumCollected =
        transactionsForEnrolment.stream()
            .filter(transaction -> transaction.getType() == TransactionType.COLLECT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    BigDecimal totalDeductions =
        transactionsForEnrolment.stream()
            .filter(transaction -> transaction.getType() == TransactionType.PAYMENT)
            .map(Transaction::getAmount)
            .reduce(BigDecimal.ZERO, BigDecimal::add);

    return sumCollected.subtract(totalDeductions);
  }
}
