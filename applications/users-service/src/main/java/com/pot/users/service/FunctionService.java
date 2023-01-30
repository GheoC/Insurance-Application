package com.pot.users.service;

import com.pot.common.enums.FunctionName;
import com.pot.users.entity.function.Function;
import com.pot.users.repository.FunctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FunctionService {
  private final FunctionRepository functionRepository;

  private static final List<FunctionName> DEFAULT_FUNCTIONS =
      List.of(
          FunctionName.CONSUMER,
          FunctionName.CONSUMER_CLAIM_MANAGER,
          FunctionName.COMPANY_MANAGER,
          FunctionName.COMPANY_CLAIM_MANAGER,
          FunctionName.COMPANY_REPORT_MANAGER,
          FunctionName.COMPANY_SETTING_MANAGER);

  public List<Function> getDefaultFunctions() {
    return DEFAULT_FUNCTIONS.stream()
        .map(name -> functionRepository.findByName(name).get())
        .collect(Collectors.toList());
  }
}
