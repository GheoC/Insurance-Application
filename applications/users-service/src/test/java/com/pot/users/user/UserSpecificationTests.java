package com.pot.users.user;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.searchparams.UserProfileSearchParams;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.user.ClientRole;
import com.pot.users.entity.user.UserProfile;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserSpecificationTests extends BaseUsersServiceTestClass {

  private Company savedCompany;

  @BeforeEach
  public void setUp() {
    objectMapper.registerModule(new JavaTimeModule());
    userProfileHelper.clearDatabase();
    companyHelper.clearDatabase();

    savedCompany = companyHelper.saveCompanyToDB(companyHelper.create());

    UserProfile frodo = userProfileHelper.create();
    frodo.getUser().setUsername("frodo");
    frodo.getUserDetails().setFirstName("Frodo");
    frodo.getUserDetails().setLastName("Baggins");
    frodo.getUserDetails().setEmail("frodo@gmail.com");
    frodo.getUserDetails().setSnn("111222");
    frodo.getUserDetails().setBirthday(LocalDate.of(2000, 02, 15));
    frodo.setCompany(savedCompany);
    frodo.setRoles(List.of(new ClientRole(FunctionName.COMPANY_MANAGER)));
    userProfileHelper.saveToDatabase(frodo);

    UserProfile bilbo = userProfileHelper.create();
    bilbo.getUser().setUsername("bilbo");
    bilbo.getUserDetails().setFirstName("Bilbo");
    bilbo.getUserDetails().setLastName("Baggins");
    bilbo.getUserDetails().setEmail("bilbo@yahoo.com");
    bilbo.getUserDetails().setSnn("121212");
    bilbo.getUserDetails().setBirthday(LocalDate.of(2000, 03, 15));
    bilbo.setCompany(savedCompany);
    userProfileHelper.saveToDatabase(bilbo);

    UserProfile sam = userProfileHelper.create();
    sam.getUser().setUsername("sam");
    sam.getUserDetails().setFirstName("Samwise");
    sam.getUserDetails().setLastName("Gamgee");
    sam.getUserDetails().setEmail("sam@gmail.com");
    sam.getUserDetails().setSnn("111333");
    sam.getUserDetails().setBirthday(LocalDate.of(2000, 03, 15));
    sam.setCompany(savedCompany);
    userProfileHelper.saveToDatabase(sam);
  }

  @Test
  public void givenAdminClaims_whenSearchingUsersWithParameters01_ShouldGetExpectedValue()
      throws Exception {
    UserProfileSearchParams searchParam =
        new UserProfileSearchParams(
            savedCompany.getId(), "", "Baggins", "", null, UserStatus.ACTIVE, "", null);
    mockMvc
        .perform(
            authorized(
                    post("/users-service/users/search?page=0&size=5"),
                    authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParam))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(
            jsonPath("$.content.[*].firstName", containsInAnyOrder("Frodo", "Bilbo")));
  }

  @Test
  public void givenAdminClaims_whenSearchingUsersWithParameters02_ShouldGetExpectedValue()
      throws Exception {
    UserProfileSearchParams searchParam =
        new UserProfileSearchParams(
            savedCompany.getId(), "", "", "gmail", null, UserStatus.ACTIVE, "", null);
    mockMvc
        .perform(
            authorized(
                    post("/users-service/users/search?page=0&size=5"),
                    authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParam))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(
            jsonPath(
                "$.content.[*].firstName", containsInAnyOrder("Samwise", "Frodo")));
  }

  @Test
  public void givenAdminClaims_whenSearchingUsersWithParameters03_ShouldGetExpectedValue()
      throws Exception {
    UserProfileSearchParams searchParam =
        new UserProfileSearchParams(
            savedCompany.getId(),
            "",
            "",
            "gmail",
            null,
            UserStatus.ACTIVE,
            "",
            List.of(FunctionName.COMPANY_MANAGER));
    mockMvc
        .perform(
            authorized(
                    post("/users-service/users/search?page=0&size=5"),
                    authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParam))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(1))
        .andExpect(jsonPath("$.content.[*].firstName", containsInAnyOrder("Frodo")));
  }

  @Test
  public void givenAdminClaims_whenSearchingUsersWithParameters04_ShouldGetExpectedValue()
      throws Exception {
    UserProfileSearchParams searchParam =
        new UserProfileSearchParams(
            savedCompany.getId(),
            "",
            "",
            "",
            LocalDate.of(2000, 03, 15),
            UserStatus.ACTIVE,
            "",
            null);
    mockMvc
        .perform(
            authorized(
                    post("/users-service/users/search?page=0&size=5"),
                    authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParam))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.totalElements").value(2))
        .andExpect(
            jsonPath(
                "$.content.[*].firstName", containsInAnyOrder("Bilbo", "Samwise")));
  }
}
