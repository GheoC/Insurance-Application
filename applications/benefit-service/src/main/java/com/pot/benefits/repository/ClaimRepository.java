package com.pot.benefits.repository;

import com.pot.benefits.entity.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClaimRepository extends JpaRepository<Claim, String>, JpaSpecificationExecutor<Claim> {
  @Query(
      "SELECT c FROM Claim c WHERE "
          + "c.id = :claimId AND "
          + "c.enrolment.userProfileId = :userProfileId AND "
          + "c.enrolment.insurance.benefitPackage.companyId =:companyId")
  Optional<Claim> findByIdAndUserProfileIdAndCompanyId(
      @Param("claimId") String claimId,
      @Param("userProfileId") String userProfileId,
      @Param("companyId") String companyId);
}
