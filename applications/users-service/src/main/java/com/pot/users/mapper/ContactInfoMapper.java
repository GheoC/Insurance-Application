package com.pot.users.mapper;

import com.pot.users.dto.ContactInfoDto;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.common.mapper.base.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
    config = BaseMapper.class,
    uses = {AddressMapper.class, PhoneMapper.class})
public interface ContactInfoMapper extends BaseMapper<ContactInfo, ContactInfoDto> {
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  ContactInfo toEntity(ContactInfoDto contactInfoDto);
}
