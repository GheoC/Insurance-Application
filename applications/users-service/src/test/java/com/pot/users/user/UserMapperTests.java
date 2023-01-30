package com.pot.users.user;

import com.pot.common.enums.FunctionName;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.UserDetailsDto;
import com.pot.users.dto.UserDto;
import com.pot.users.dto.UserProfileDto;
import com.pot.users.entity.user.User;
import com.pot.users.entity.user.UserDetails;
import com.pot.users.entity.user.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserMapperTests extends BaseUsersServiceTestClass {

  @Test
  public void whenMappingUserDetailsToDtoAndBackToEntity_ShouldGetSameUserDetailsAttributes() {
    UserDetails userDetails = userDetailsHelper.create();

    UserDetailsDto dto = userDetailsHelper.getUserDetailsMapper().toDto(userDetails);
    UserDetails entity = userDetailsHelper.getUserDetailsMapper().toEntity(dto);
    assertEquals(dto.getFirstName(), entity.getFirstName());
    assertEquals(dto.getLastName(), entity.getLastName());
    assertEquals(dto.getEmail(), entity.getEmail());
    assertEquals(dto.getSnn(), entity.getSnn());
    assertEquals(LocalDate.of(2000, 12, 15), entity.getBirthday());
  }

  @Test
  public void whenMappingUserToDtoAndBackToEntity_ShouldGetSameUserDetailsAttributes() {
    User user = userHelper.create();
    UserDto dto = userHelper.getUserMapper().toDto(user);
    assertEquals(user.getUsername(), dto.getUsername());
    assertEquals("CONFIDENTIAL", dto.getPassword());

    dto.setPassword("asd");
    User entity = userHelper.getUserMapper().toEntity(dto);

    assertEquals(dto.getUsername(), entity.getUsername());
    assertEquals(dto.getPassword(), entity.getPassword());
  }

  @Test
  public void whenMappingUserProfileToDtoAndBackToEntity_ShouldGetSameUserProfileAttributes() {
    UserProfile userProfile = userProfileHelper.create();

    UserProfileDto dto = userProfileHelper.getUserProfileMapper().toDto(userProfile);

    assertEquals("CONFIDENTIAL", dto.getUser().getPassword());
    dto.getUser().setPassword("asd");

    UserProfile entity = userProfileHelper.getUserProfileMapper().toEntity(dto);

    assertEquals(dto.getUserDetails().getFirstName(), entity.getUserDetails().getFirstName());
    assertEquals(dto.getUserDetails().getLastName(), entity.getUserDetails().getLastName());
    assertEquals(dto.getUserDetails().getEmail(), entity.getUserDetails().getEmail());
    assertEquals(dto.getUserDetails().getSnn(), entity.getUserDetails().getSnn());
    assertEquals(LocalDate.of(2000, 12, 15), entity.getUserDetails().getBirthday());
    assertEquals(dto.getUser().getUsername(), entity.getUser().getUsername());
    assertEquals(dto.getUser().getPassword(), entity.getUser().getPassword());
    assertTrue(
        entity.getRoles().stream()
            .anyMatch(clientRole -> clientRole.getName().equals(FunctionName.CONSUMER)));
    assertTrue(
        entity.getRoles().stream()
            .anyMatch(
                clientRole -> clientRole.getName().equals(FunctionName.CONSUMER_CLAIM_MANAGER)));
  }
}
