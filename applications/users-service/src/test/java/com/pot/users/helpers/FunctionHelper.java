package com.pot.users.helpers;

import com.pot.common.enums.FunctionName;
import com.pot.error.exceptions.ResourceNotFoundException;
import com.pot.users.entity.function.Function;
import com.pot.users.mapper.FunctionMapper;
import com.pot.users.repository.FunctionRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Getter
public class FunctionHelper {
  private final FunctionMapper functionMapper;

  private final FunctionRepository functionRepository;

  public List<Function> getFunctions() {
    return functionMapper.toEntities(createFunctionNames());
  }

  public List<FunctionName> createFunctionNames() {
    return List.of(
        FunctionName.CONSUMER,
        FunctionName.CONSUMER_CLAIM_MANAGER,
        FunctionName.COMPANY_MANAGER,
        FunctionName.COMPANY_CLAIM_MANAGER,
        FunctionName.COMPANY_SETTING_MANAGER,
        FunctionName.COMPANY_REPORT_MANAGER);
  }

  public Function getFunctionFromDB(FunctionName name)
  {
    return functionRepository.findByName(name).orElseThrow(()->new ResourceNotFoundException("not found"));
  }
}
