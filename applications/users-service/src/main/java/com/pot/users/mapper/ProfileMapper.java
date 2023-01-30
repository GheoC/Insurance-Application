package com.pot.users.mapper;

import com.pot.common.enums.FunctionName;
import com.pot.common.mapper.base.BaseMapper;
import com.pot.users.dto.UserViewDto;
import com.pot.users.entity.function.Function;
import com.pot.users.entity.user.AdminProfile;
import com.pot.users.entity.user.BaseProfile;
import com.pot.users.entity.user.ClientRole;
import com.pot.users.entity.user.UserProfile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = BaseMapper.class)
public abstract class ProfileMapper {
  @Mapping(target = "companyId", source = "userProfile.company.id")
  @Mapping(target = "firstName", source = "userProfile.userDetails.firstName")
  @Mapping(target = "lastName", source = "userProfile.userDetails.lastName")
  @Mapping(target = "email", source = "userProfile.userDetails.email")
  @Mapping(target = "birthday", source = "userProfile.userDetails.birthday")
  @Mapping(target = "snn", source = "userProfile.userDetails.snn")
  @Mapping(target = "roles", source = "roles")
  abstract UserViewDto toDto(UserProfile userProfile);

  @Mapping(target = "roles", source = "functions")
  @Mapping(target = "companyId", ignore = true)
  @Mapping(target = "firstName", ignore = true)
  @Mapping(target = "lastName", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "birthday", ignore = true)
  @Mapping(target = "snn", ignore = true)
  abstract UserViewDto toDto(AdminProfile adminProfileProfile);

  public UserViewDto toDto(BaseProfile baseProfile) {
    return baseProfile instanceof UserProfile
        ? this.toDto((UserProfile) baseProfile)
        : this.toDto((AdminProfile) baseProfile);
  }

  protected FunctionName mapClientRoleToFunctionName(ClientRole clientRole) {
    return clientRole.getName();
  }

  protected FunctionName mapFunctionToFunctionName(Function function) {
    return function.getName();
  }
}
