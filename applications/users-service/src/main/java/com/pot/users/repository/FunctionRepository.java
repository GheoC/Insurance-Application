package com.pot.users.repository;

import com.pot.common.enums.FunctionName;
import com.pot.users.entity.function.Function;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FunctionRepository extends JpaRepository<Function, String> {

    Optional<Function> findByName(FunctionName name);
}
