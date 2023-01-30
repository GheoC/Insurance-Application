package com.pot.users.mapper;

import com.pot.users.dto.AddressDto;
import com.pot.users.entity.contact.Address;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface AddressMapper extends BaseMapper<Address, AddressDto> {
  @Override
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  Address toEntity(AddressDto addressDto);
}
