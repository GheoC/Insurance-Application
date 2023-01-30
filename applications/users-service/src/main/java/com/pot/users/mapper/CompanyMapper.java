package com.pot.users.mapper;

import com.pot.users.dto.CompanyDto;
import com.pot.users.entity.company.Company;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = BaseMapper.class,
    uses = {ContactInfoMapper.class, FunctionMapper.class})
public interface CompanyMapper extends BaseMapper<Company, CompanyDto> {
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "functions", ignore = true)
  Company toEntity(CompanyDto companyDto);
}
