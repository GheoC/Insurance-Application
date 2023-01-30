package com.pot.benefits.repository;

import com.pot.benefits.entity.Enrolment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EnrolmentRepository extends JpaRepository<Enrolment, String> {

  @Query(
      "SELECT e FROM Enrolment e WHERE e.userProfileId = :userProfileId AND e.insurance.benefitPackage.companyId =:companyId")
  List<Enrolment> findByUserProfileIdAndCompanyId(
      @Param("userProfileId") String userProfileId, @Param("companyId") String companyId);

  @Query(
      "SELECT e FROM Enrolment e WHERE "
          + "e.id = :enrolmentId AND "
          + "e.userProfileId = :userProfileId AND "
          + "e.insurance.benefitPackage.companyId =:companyId")
  Optional<Enrolment> findByIdAndUserProfileIdAndCompanyId(
      @Param("enrolmentId") String enrolmentId,
      @Param("userProfileId") String userProfileId,
      @Param("companyId") String companyId);

  List<Enrolment> findByDueDate(LocalDate dueDate);
}
