package com.pot.users.user;

import com.pot.common.enums.ErrorCategory;
import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.pot.security.claims.UserClaims;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.UserProfileDto;
import com.pot.users.dto.searchparams.UserProfileSearchParams;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.user.UserProfile;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserProfileIntegrationTests extends BaseUsersServiceTestClass {
  private final String BASE_URL = "/users-service/companies/";

  @BeforeEach
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    userProfileHelper.clearDatabase();
  }

  @Test
  public void whenCreatingUserProfileWithoutToken_ShouldReceiveUnauthorized() throws Exception {
    url = BASE_URL + "RANDOM_COMPANY_ID/users";

    UserProfile userProfile = userProfileHelper.create();
    mockMvc
        .perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void whenCreatingUserProfileWithUserRole_ShouldReceiveForbidden() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultUserClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenCreatingUserProfileWithAdminRole_ShouldReceiveStatusOk() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenCreatingUserProfileWIthCompanyManager_ShouldReceiveStatusOk() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users";

    UserClaims userClaimsWithCompanyManagerRole = authUserHelper.createDefaultUserClaims();
    userClaimsWithCompanyManagerRole.setCompanyId(savedCompanyToDB.getId());
    userClaimsWithCompanyManagerRole.getAuthorities().add(FunctionName.COMPANY_MANAGER);

    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());

    mockMvc
        .perform(
            authorized(post(url), userClaimsWithCompanyManagerRole)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUpdatingUserProfileWithoutToken_ShouldReceiveUnauthorized() throws Exception {
    url = BASE_URL + "RANDOM_COMPANY_ID/users/RANDOM_USER_ID";

    UserProfile userProfile = userProfileHelper.create();
    mockMvc
        .perform(
            put(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfile))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void whenUpdatingUserProfileWithUserRole_ShouldReceiveForbidden() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    url = BASE_URL + savedCompanyToDB.getId() + "/users/RANDOM_ID";
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());
    userProfileDto.setId("RANDOM_ID");
    userProfileDto.setUser(null);
    userProfileDto.getContactInfo().setId("RANDOM_ID");
    userProfileDto.getUserDetails().setId("RANDOM_ID");

    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultUserClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenUpdatingUserProfileWithAdminRole_ShouldReceiveStatusOk() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);

    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();

    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenUpdatingUserProfileWithCompanyManager_ShouldReceiveStatusOk() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);
    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();

    UserClaims userClaimsWithCompanyManagerRole = authUserHelper.createDefaultUserClaims();
    userClaimsWithCompanyManagerRole.setCompanyId(savedCompanyToDB.getId());
    userClaimsWithCompanyManagerRole.getAuthorities().add(FunctionName.COMPANY_MANAGER);

    mockMvc
        .perform(
            authorized(put(url), userClaimsWithCompanyManagerRole)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenGettingUserProfileWithUserRole_ShouldReceiveForbidden() throws Exception {
    url = BASE_URL + "RANDOM_COMPANY_ID" + "/users/RANDOM_ID";
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultUserClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenGettingUserProfileWithAdminRole_ShouldReceiveStatusOk() throws Exception {
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
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(userProfileDto.getId()));
  }

  @Test
  public void
      whenGettingUserProfileFromDifferentCompanyWithCompanyManagerRole_shouldReceiveException()
          throws Exception {
    Company ourCompany = companyHelper.saveCompanyToDB(companyHelper.create());
    Company otherCompany = companyHelper.saveCompanyToDB(companyHelper.create());

    UserProfile userProfileFromOtherCompany = userProfileHelper.create();
    userProfileFromOtherCompany.setCompany(otherCompany);
    UserProfileDto userProfileDtoFromOtherCompany =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfileFromOtherCompany));

    url = BASE_URL + ourCompany.getId() + "/users/" + userProfileDtoFromOtherCompany.getId();

    UserClaims companyManagerForOurCompany = authUserHelper.createDefaultUserClaims();
    companyManagerForOurCompany.setCompanyId(ourCompany.getId());
    companyManagerForOurCompany.getAuthorities().add(FunctionName.COMPANY_MANAGER);

    mockMvc
        .perform(
            authorized(get(url), companyManagerForOurCompany).accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void whenGettingOwnUserProfile_shouldReceiveStatusOk() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);
    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();

    UserClaims userClaimsWithCompanyManagerRole = authUserHelper.createDefaultUserClaims();
    userClaimsWithCompanyManagerRole.setCompanyId(savedCompanyToDB.getId());
    userClaimsWithCompanyManagerRole.setUserProfileId(userProfileDto.getId());

    mockMvc
        .perform(
            authorized(get(url), userClaimsWithCompanyManagerRole)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void
      givenMoreRolesThanCompanyHas_whenCreatingUserProfileWithAdminRole_ShouldReceiveStatusOk()
          throws Exception {
    Company company = companyHelper.create();
    company.getFunctions().clear();
    company.getFunctions().add(functionHelper.getFunctionFromDB(FunctionName.CONSUMER));
    company
        .getFunctions()
        .add(functionHelper.getFunctionFromDB(FunctionName.CONSUMER_CLAIM_MANAGER));
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(company);

    url = BASE_URL + savedCompanyToDB.getId() + "/users";
    UserProfileDto userProfileDto =
        userProfileHelper.getUserProfileMapper().toDto(userProfileHelper.create());
    userProfileDto.getRoles().add(FunctionName.COMPANY_MANAGER);

    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCategory").value(ErrorCategory.CLIENT_ERROR.name()));
  }

  @Test
  public void
      givenMoreRolesThanCompanyHas_whenUpdatingUserProfileWithAdminRole_ShouldReceiveException()
          throws Exception {
    Company company = companyHelper.create();
    company.getFunctions().clear();
    company.getFunctions().add(functionHelper.getFunctionFromDB(FunctionName.CONSUMER));
    company
        .getFunctions()
        .add(functionHelper.getFunctionFromDB(FunctionName.CONSUMER_CLAIM_MANAGER));
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(company);
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);

    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    userProfileDto.setUser(null);

    // add function to user profile, that company doesn't have
    userProfileDto.getRoles().add(FunctionName.COMPANY_MANAGER);

    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId();

    mockMvc
        .perform(
            authorized(put(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userProfileDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCategory").value(ErrorCategory.CLIENT_ERROR.name()));
  }

  @Test
  public void whenSwitchingUserProfileStatus_StatusShouldBeChangedInDatabase() throws Exception {
    Company savedCompanyToDB = companyHelper.saveCompanyToDB(companyHelper.create());
    UserProfile userProfile = userProfileHelper.create();
    userProfile.setCompany(savedCompanyToDB);
    UserProfileDto userProfileDto =
        userProfileHelper
            .getUserProfileMapper()
            .toDto(userProfileHelper.saveToDatabase(userProfile));
    url = BASE_URL + savedCompanyToDB.getId() + "/users/" + userProfileDto.getId() + "/status";

    // switch to INACTIVE
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    UserProfile userProfileFromDB =
        userProfileHelper.getUserProfileRepository().findById(userProfileDto.getId()).get();
    assertEquals(userProfileFromDB.getStatus(), UserStatus.INACTIVE);

    // switch to ACTIVE
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
    userProfileFromDB =
        userProfileHelper.getUserProfileRepository().findById(userProfileDto.getId()).get();
    assertEquals(userProfileFromDB.getStatus(), UserStatus.ACTIVE);
  }

  @Test
  @Transactional
  public void
      whenAddingNewFunctionsToCompanyWithAdminRole_shouldUpdateFunctionsForCompanyUsersAlso()
          throws Exception {
    Company companyToSave = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToSave);

    List<FunctionName> newFunctions = List.of(FunctionName.COMPANY_MANAGER);

    UserProfile userAtC = userProfileHelper.create();
    userAtC.getUserDetails().setEmail("a@c.ro");
    userAtC.getUserDetails().setSnn("1234778");
    userAtC.setCompany(savedCompany);
    UserProfile savedUserProfile = userProfileHelper.saveToDatabase(userAtC);
    UserProfile userAtB = userProfileHelper.create();
    userAtB.getUserDetails().setEmail("a@b.ro");
    userAtB.getUserDetails().setSnn("1234777");
    userAtB.setCompany(savedCompany);
    UserProfile savedUserProfile2 = userProfileHelper.saveToDatabase(userAtB);

    assertEquals(2, savedUserProfile.getRoles().size());
    assertEquals(2, savedUserProfile2.getRoles().size());

    mockMvc
        .perform(
            authorized(
                    post(BASE_URL + savedCompany.getId() + "/functions"),
                    authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFunctions))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    List<UserProfile> companyUsers =
        userProfileHelper.getUserProfileRepository().findByCompanyId(savedCompany.getId());
    assertEquals(0, companyUsers.get(0).getRoles().size());
    assertEquals(0, companyUsers.get(1).getRoles().size());
  }

  @Test
  public void whenSearchingUsersWithNoCompanyId_shouldBePermittedToAdminOnly() throws Exception {
    UserProfileSearchParams searchParam =
        new UserProfileSearchParams("", "", "", "", null, UserStatus.ACTIVE, "", null);

    UserClaims customUserClaimsWithCompanyManagerRole =
        authUserHelper.createCustomUserClaims(
            "random",
            "random",
            UserStatus.ACTIVE,
            List.of(FunctionName.COMPANY_MANAGER),
            "random@random.com",
            "random",
            "random");

    mockMvc
        .perform(
            authorized(post("/users-service/users/search?page=0&size=5"), customUserClaimsWithCompanyManagerRole)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParam))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void compareCompanies()
  {
    Company company = companyHelper.saveCompanyToDB(companyHelper.create());

    Company company1 = companyHelper.getCompany(company.getId());
    Company company2 = companyHelper.getCompany(company.getId());

    boolean b = company1 == company2;
    boolean equals = company1.equals(company2);
    assertEquals(company1, company2);
  }
}
