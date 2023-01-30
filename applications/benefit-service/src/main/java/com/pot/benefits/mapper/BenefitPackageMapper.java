package com.pot.benefits.mapper;

import com.pot.benefits.dto.BenefitPackageDto;
import com.pot.benefits.entity.BenefitPackage;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface BenefitPackageMapper extends BaseMapper<BenefitPackage, BenefitPackageDto>
{
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    BenefitPackage toEntity(BenefitPackageDto benefitPackageDto);
}
