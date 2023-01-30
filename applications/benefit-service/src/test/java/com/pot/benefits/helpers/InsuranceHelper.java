package com.pot.benefits.helpers;

import com.pot.benefits.entity.Insurance;
import com.pot.benefits.mapper.InsuranceMapper;
import com.pot.benefits.repository.InsuranceRepository;
import com.pot.common.enums.InsuranceType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class InsuranceHelper {
  private final InsuranceRepository insuranceRepository;
  private final InsuranceMapper insuranceMapper;

  public Insurance create() {
    Insurance insurance = new Insurance();
    insurance.setName("Insurance " + UUID.randomUUID());
    insurance.setType(InsuranceType.MEDICAL);
    insurance.setContribution(BigDecimal.valueOf(2000));
    return insurance;
  }

  public Insurance saveToDatabase(Insurance insurance) {
    return insuranceRepository.save(insurance);
  }

  public void clearDatabase() {
    insuranceRepository.deleteAll();
  }
}
