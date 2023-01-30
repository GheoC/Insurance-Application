package com.pot.users.mapper;

import com.pot.common.mapper.base.BaseMapper;
import com.pot.users.dto.UserDto;
import com.pot.users.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public interface UserMapper extends BaseMapper<User, UserDto> {
  @Override
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  User toEntity(UserDto userDto);

  @Mapping(target = "password", constant = "CONFIDENTIAL")
  UserDto toDto(User user);
}
