package com.pot.benefits.mapper;

import com.pot.benefits.dto.ClaimInformationDto;
import com.pot.benefits.entity.ClaimInformation;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(config = BaseMapper.class)
public interface ClaimInformationMapper extends BaseMapper<ClaimInformation, ClaimInformationDto> {}
