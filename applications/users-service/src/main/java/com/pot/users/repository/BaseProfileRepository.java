package com.pot.users.repository;

import com.pot.users.entity.user.BaseProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BaseProfileRepository extends JpaRepository<BaseProfile, String>, JpaSpecificationExecutor<BaseProfile> {
}
