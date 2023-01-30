package com.pot.benefits.mapper;

import com.pot.benefits.dto.InsuranceDto;
import com.pot.benefits.entity.Insurance;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface InsuranceMapper extends BaseMapper<Insurance, InsuranceDto> {
  @Override
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "benefitPackage", ignore = true)
  Insurance toEntity(InsuranceDto insuranceDto);
}
