package com.pot.users.mapper;

import com.pot.users.dto.PhoneDto;
import com.pot.users.entity.contact.Phone;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface PhoneMapper extends BaseMapper<Phone, PhoneDto> {
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Phone toEntity(PhoneDto phoneDto);
}
