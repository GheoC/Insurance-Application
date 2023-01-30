package com.pot.users.mapper;

import com.pot.common.mapper.base.BaseMapper;
import com.pot.users.dto.UserDetailsDto;
import com.pot.users.entity.user.UserDetails;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface UserDetailsMapper extends BaseMapper<UserDetails, UserDetailsDto> {
  @Override
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  UserDetails toEntity(UserDetailsDto userDetailsDto);
}
