package com.pot.users.mapper;

import com.pot.common.enums.FunctionName;
import com.pot.common.mapper.base.BaseMapper;
import com.pot.users.dto.UserProfileDto;
import com.pot.users.entity.user.ClientRole;
import com.pot.users.entity.user.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
    config = BaseMapper.class,
    uses = {UserMapper.class, UserDetailsMapper.class, ContactInfoMapper.class})
public abstract class UserProfileMapper {

  @Mapping(target = "roles", source = "roles")
  public abstract UserProfileDto toDto(UserProfile userProfile);

  @Mapping(target = "type", ignore = true)
  @Mapping(target = "roles", source = "roles", qualifiedByName = "createClientRoles")
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  @Mapping(target = "status", ignore = true)
  @Mapping(target = "company", ignore = true)
  public abstract UserProfile toEntity(UserProfileDto userProfileDto);

  protected FunctionName mapClientRoleToFunctionName(ClientRole clientRole) {
    return clientRole.getName();
  }

  @Named("createClientRoles")
  protected List<ClientRole> createClientRoles(List<FunctionName> roles) {
    return roles.stream().map(ClientRole::new).collect(Collectors.toList());
  }
}
