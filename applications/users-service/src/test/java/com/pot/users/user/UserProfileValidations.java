package com.pot.users.user;

import com.pot.common.enums.FunctionName;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.ContactInfoDto;
import com.pot.users.dto.UserDetailsDto;
import com.pot.users.dto.UserDto;
import com.pot.users.dto.UserProfileDto;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.user.UserProfile;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserProfileValidations extends BaseUsersServiceTestClass {
  private final String BASE_URL = "/users-service/companies/";

  @BeforeEach
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    userProfileHelper.clearDatabase();
  }

  @Test
  public void givenUserProfileWithNullFields_whenCreatingUserProfile_shouldExpectExceptions()
      throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto = new UserProfileDto();

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder("roles", "user", "userDetails", "contactInfo")));
  }

  @Test
  public void giveNullSubAttributes_whenCreatingUserProfile_shouldExpectExceptions()
      throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto = new UserProfileDto();
    UserDto userDto = new UserDto();
    UserDetailsDto userDetailsDto = new UserDetailsDto();
    ContactInfoDto contactInfoDto = new ContactInfoDto();
    userProfileDto.setUser(userDto);
    userProfileDto.setUserDetails(userDetailsDto);
    userProfileDto.setContactInfo(contactInfoDto);
    userProfileDto.getRoles().add(FunctionName.CONSUMER);

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder(
                    "user.username",
                    "user.password",
                    "contactInfo.phones",
                    "contactInfo.addresses",
                    "userDetails.firstName",
                    "userDetails.lastName",
                    "userDetails.email",
                    "userDetails.snn",
                    "userDetails.birthday")));
  }

  @Test
  public void
      givenUserProfileThatContainsSUPER_ADMINRole_whenCreatingUserProfile_shouldExpectExceptions()
          throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());
    userProfileDto.getRoles().add(FunctionName.SUPER_ADMIN);

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("roles"));
  }

  @Test
  public void givenNotNullUser_whenUpdatingUserProfile_shouldExpectException() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("user"));
  }

  @Test
  public void givenUserProfileDtoWithNullIDs_whenUpdatingUserProfile_shouldExpectException()
      throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());
    userProfileDto.setUser(null);

    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + "RANDOM_ID";
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath(
                "$.errors.[*].field",
                containsInAnyOrder("userDetails.id", "contactInfo.id", "id")));
  }

  @Test
  public void givenBirthDayInTheFuture_whenUpdatingUserProfile_shouldExpectException()
      throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);
    userProfileDto.getUserDetails().setBirthday(LocalDate.of(2030, 11, 11));

    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("userDetails.birthday"));
  }

  @Test
  public void
      givenUserProfileThatContainsSUPER_ADMINRole_whenUpdatingUserProfile_shouldExpectExceptions()
          throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);
    userProfileDto.getRoles().add(FunctionName.SUPER_ADMIN);

    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();
    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors[0].field").value("roles"));
  }
}
