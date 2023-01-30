package com.pot.benefits.mapper;

import com.pot.benefits.dto.ClaimDto;
import com.pot.benefits.entity.Claim;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface ClaimMapper extends BaseMapper<Claim, ClaimDto> {
  @Override
  @Mapping(target = "enrolment.id", source = "enrolmentId")
  @Mapping(target = "claimInformation", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Claim toEntity(ClaimDto claimDto);

  @Override
  @Mapping(target = "insuranceType", source = "enrolment.insurance.type")
  @Mapping(target = "enrolmentId", source = "enrolment.id")
  @Mapping(target = "status", source = "claimInformation.status")
  ClaimDto toDto(Claim source);
}
