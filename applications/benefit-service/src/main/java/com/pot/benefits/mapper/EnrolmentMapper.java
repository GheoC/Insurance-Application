package com.pot.benefits.mapper;

import com.pot.benefits.dto.EnrolmentDto;
import com.pot.benefits.entity.Enrolment;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface EnrolmentMapper extends BaseMapper<Enrolment, EnrolmentDto> {

  @Override
  @Mapping(target = "insurance.id", source = "insuranceId")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "payrollFrequency", ignore = true)
  @Mapping(target = "lastProcessedDate", ignore = true)
  @Mapping(target = "dueDate", ignore = true)
  @Mapping(target = "status", ignore = true)
  Enrolment toEntity(EnrolmentDto enrolmentDto);

  @Override
  @Mapping(target = "insuranceId", source = "insurance.id")
  @Mapping(target = "insuranceName", source = "insurance.name")
  @Mapping(target = "insuranceType", source = "insurance.type")
  EnrolmentDto toDto(Enrolment enrolment);
}
