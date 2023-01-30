package com.pot.users.controller;

import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import com.pot.database.specification.CompositeType;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.users.dto.UserProfileDto;
import com.pot.users.dto.searchparams.UserProfileSearchParams;
import com.pot.users.dto.UserViewDto;
import com.pot.users.mapper.ProfileMapper;
import com.pot.users.mapper.UserProfileMapper;
import com.pot.users.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users-service")
public class UserProfileController {
  private final UserProfileService userProfileService;
  private final UserProfileMapper userProfileMapper;
  private final ProfileMapper profileMapper;

  @PostMapping("/companies/{companyId}/users")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public UserProfileDto createUser(
      @PathVariable(name = "companyId") String companyId,
      @RequestBody @Validated(OnCreate.class) UserProfileDto userProfileDto) {
    return userProfileMapper.toDto(
        userProfileService.create(companyId, userProfileMapper.toEntity(userProfileDto)));
  }

  @PutMapping("/companies/{companyId}/users/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public UserProfileDto updateUser(
      @PathVariable String companyId,
      @PathVariable String id,
      @RequestBody @Validated(OnUpdate.class) UserProfileDto userProfileDto) {
    if (!id.equals(userProfileDto.getId())) {
      throw new InconsistentDataException("IDs must match!");
    }
    return userProfileMapper.toDto(
        userProfileService.update(companyId, userProfileMapper.toEntity(userProfileDto)));
  }

  @GetMapping("/companies/{companyId}/users/{id}")
  @PreAuthorize(
      "@authorizationEvaluator.isAdminOrCompanyManager(#companyId) or @authorizationEvaluator.isAccountOwner(#id)")
  public UserProfileDto getUserProfile(@PathVariable String companyId, @PathVariable String id) {
    return userProfileMapper.toDto(userProfileService.getUserProfile(companyId, id));
  }

  @PostMapping("/companies/{companyId}/users/{id}/status")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public void switchStatus(@PathVariable String companyId, @PathVariable String id) {
    userProfileService.switchStatus(companyId, id);
  }

  @PostMapping("/users/search")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#userProfileSearchParams.companyId)")
  public Page<UserViewDto> searchUsersProfiles(
          @RequestBody UserProfileSearchParams userProfileSearchParams, Pageable pageable) {
    return userProfileService.searchUsers(
        userProfileSearchParams.buildSpecification(CompositeType.AND), pageable)
        .map(profileMapper::toDto);
  }
}
