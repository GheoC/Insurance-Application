package com.pot.users.repository;

import com.pot.common.enums.UserStatus;
import com.pot.users.entity.user.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, String>, JpaSpecificationExecutor<UserProfile> {
  Optional<UserProfile> findByIdAndCompanyId(String id, String companyId);

  @Modifying
  @Query("UPDATE UserProfile u SET u.status =:status WHERE u.company.id =:companyId")
  void updateUsersProfileStatusFromCompany(
      @Param("companyId") String companyId, @Param("status") UserStatus status);

  List<UserProfile> findByCompanyId(String companyId);
}
