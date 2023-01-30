package com.pot.benefits.helpers;

import com.pot.benefits.repository.TransactionRepository;
import com.pot.benefits.service.EnrolmentProccesingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class TransactionHelper {

  private final TransactionRepository transactionRepository;
  private final EnrolmentProccesingService enrolmentProccesingService;

  public void clearDatabase() {
    transactionRepository.deleteAll();
  }
}
