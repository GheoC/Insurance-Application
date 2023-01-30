package com.pot.users.mapper;

import com.pot.common.enums.FunctionName;
import com.pot.common.mapper.base.BaseMapper;
import com.pot.users.entity.function.Function;
import com.pot.users.repository.FunctionRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(config = BaseMapper.class)
public abstract class FunctionMapper {
  @Autowired private FunctionRepository functionRepository;

  @Mapping(target = "id", source = "name", qualifiedByName = "getIdFromDatabase")
  @Mapping(target = "name", source = "name")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  public abstract Function toEntity(FunctionName name);

  public abstract List<Function> toEntities(List<FunctionName> names);

  public FunctionName mapFunctionToFunctionName(Function function) {
    return function.getName();
  }

  public abstract List<FunctionName> toFunctionNames(List<Function> functions);

  @Named("getIdFromDatabase")
  public String getIdFromDatabase(FunctionName name) {
    return functionRepository.findByName(name).get().getId();
  }
}
