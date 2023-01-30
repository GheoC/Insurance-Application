package com.pot.benefits.helpers;

import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.ClaimInformation;
import com.pot.benefits.mapper.ClaimMapper;
import com.pot.benefits.repository.ClaimRepository;
import com.pot.common.enums.ClaimStatus;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
@Setter
public class ClaimHelper {
  private final ClaimMapper claimMapper;
  private final ClaimRepository claimRepository;

  public Claim create() {
    Claim claim = new Claim();
    ClaimInformation claimInformation = new ClaimInformation();
    claimInformation.setStatus(ClaimStatus.PENDING);
    claim.setClaimInformation(claimInformation);
    claim.setAmount(BigDecimal.valueOf(200));
    claim.setServiceDate(LocalDate.now().minusDays(5));
    claim.setSerialNumber("random " + UUID.randomUUID());
    return claim;
  }

  public Claim saveToDatabase(Claim claim) {
    return claimRepository.save(claim);
  }

  public void clearDatabase() {
    claimRepository.deleteAll();
  }
}
