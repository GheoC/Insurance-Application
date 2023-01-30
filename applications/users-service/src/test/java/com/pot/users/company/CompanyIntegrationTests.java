package com.pot.users.company;

import com.pot.common.enums.CompanyStatus;
import com.pot.common.enums.ErrorCategory;
import com.pot.common.enums.FunctionName;
import com.pot.security.claims.AdminClaims;
import com.pot.security.claims.UserClaims;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.CompanyDto;
import com.pot.users.entity.company.Company;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CompanyIntegrationTests extends BaseUsersServiceTestClass {
  private CompanyDto company;
  private UserClaims userClaims;
  private AdminClaims adminClaims;

  @BeforeEach
  public void setUp() {
    url = "/users-service/companies";
    company = companyHelper.getCompanyMapper().toDto(companyHelper.create());
    userClaims = authUserHelper.createDefaultUserClaims();
    adminClaims = authUserHelper.createDefaultAdminClaims();
  }

  @Test
  public void whenCreatingCompanyWithoutToken_ShouldReceivedUnauthorized() throws Exception {
    mockMvc
        .perform(
            post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isUnauthorized());
  }

  @Test
  public void whenCreatingCompanyWithUserRole_ShouldReceivedForbidden() throws Exception {
    mockMvc
        .perform(
            authorized(post(url), userClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenCreatingCompanyWithSuperAdminRole_ShouldReceivedStatusOk() throws Exception {
    mockMvc
        .perform(
            authorized(post(url), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void givenCorrectID_whenGettingCompanyWithSuperAdminRole_ShouldReceiveCompanyFromDatabase()
      throws Exception {
    Company companyToSave = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToSave);
    mockMvc
        .perform(authorized(get(url + "/" + savedCompany.getId()), adminClaims))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedCompany.getId()))
        .andExpect(jsonPath("$.companyName").value(companyToSave.getCompanyName()))
        .andExpect(jsonPath("$.email").value(companyToSave.getEmail()))
        .andExpect(jsonPath("$.site").value(companyToSave.getSite()));
  }

  @Test
  public void
      givenWrongId_whenGettingCompanyWithSuperAdminRole_ShouldReceiveResourceNotFoundException()
          throws Exception {
    mockMvc
        .perform(authorized(get(url + "/" + "WRONG_ID"), adminClaims))
        .andExpect(status().is(404))
        .andExpect(jsonPath("$.userMessage").value("Company NOT found"));
  }

  @Test
  public void whenGettingCompanyWithUserRole_ShouldBeForbidden() throws Exception {
    mockMvc
        .perform(authorized(get(url + "/" + "RANDOM_ID"), userClaims))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenSwitchingCompanyStatusWithAdminRole_ShouldBeSwitchedInDatabase()
      throws Exception {
    Company companyToSave = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToSave);
    mockMvc
        .perform(authorized(post(url + "/" + savedCompany.getId() + "/status"), adminClaims))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(CompanyStatus.INACTIVE.name()));

    mockMvc
        .perform(authorized(post(url + "/" + savedCompany.getId() + "/status"), adminClaims))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.status").value(CompanyStatus.ACTIVE.name()));
  }

  @Test
  public void whenSwitchingCompanyStatusWithUserRole_ShouldBeForbidden() throws Exception {
    mockMvc
        .perform(authorized(post(url + "/" + "RANDOM_ID/status"), userClaims))
        .andExpect(status().isForbidden());
  }

  @Test
  @Transactional
  public void whenAddingFunctionsToCompanyWithAdminRole_ShouldBeUpdatedInDatabase()
      throws Exception {
    Company companyToSave = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToSave);

    List<FunctionName> newFunctions = List.of(FunctionName.COMPANY_MANAGER, FunctionName.CONSUMER);

    mockMvc
        .perform(
            authorized(post(url + "/" + savedCompany.getId() + "/functions"), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFunctions))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    savedCompany = companyHelper.getCompany(savedCompany.getId());
    assertEquals(2, savedCompany.getFunctions().size());
    assertTrue(
        savedCompany.getFunctions().get(0).getName().equals(FunctionName.COMPANY_MANAGER)
            || savedCompany.getFunctions().get(0).getName().equals(FunctionName.CONSUMER));
    assertTrue(
        savedCompany.getFunctions().get(1).getName().equals(FunctionName.COMPANY_MANAGER)
            || savedCompany.getFunctions().get(1).getName().equals(FunctionName.CONSUMER));
  }

  @Test
  public void whenAddingFunctionsToCompanyWithUserRole_ShouldBeForbidden() throws Exception {
    List<FunctionName> newFunctions = List.of(FunctionName.COMPANY_MANAGER, FunctionName.CONSUMER);
    mockMvc
        .perform(
            authorized(
                post(url + "/" + "RANDOM_ID/functions")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newFunctions))
                    .accept(MediaType.APPLICATION_JSON),
                userClaims))
        .andExpect(status().isForbidden());
  }

  @Test
  public void whenUpdatingCompanyWithAdminRole_ShouldUpdateCompanyInDatabase() throws Exception {
    Company companyToUpdate = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToUpdate);

    savedCompany.setCompanyName("updated");
    savedCompany.setEmail("updated@updated.com");
    savedCompany.setSite("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setStreet("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setCountry("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setCity("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setBuilding("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setState("updated");
    savedCompany.getContactInfo().getAddresses().get(0).setRoom("123");
    savedCompany.getContactInfo().getPhones().get(0).setCountryCode("XX");
    savedCompany.getContactInfo().getPhones().get(0).setPhoneNumber("111111111");

    CompanyDto companyDto = companyHelper.getCompanyMapper().toDto(savedCompany);
    // ADD also new address
    companyDto
        .getContactInfo()
        .getAddresses()
        .add(addressHelper.getAddressMapper().toDto(addressHelper.create()));
    // ADD also new phone
    companyDto
        .getContactInfo()
        .getPhones()
        .add(phoneHelper.getPhoneMapper().toDto(phoneHelper.create()));

    companyDto.setStatus(null);

    mockMvc
        .perform(
            authorized(put(url + "/" + savedCompany.getId()), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.companyName").value(companyDto.getCompanyName()))
        .andExpect(jsonPath("$.countryCode").value(companyDto.getCountryCode()))
        .andExpect(jsonPath("$.email").value(companyDto.getEmail()))
        .andExpect(jsonPath("$.site").value(companyDto.getSite()))
        .andExpect(
            jsonPath(
                "$.contactInfo.addresses[*].country", containsInAnyOrder("updated", "Romania")))
        .andExpect(
            jsonPath("$.contactInfo.addresses[*].city", containsInAnyOrder("updated", "Brasov")))
        .andExpect(
            jsonPath(
                "$.contactInfo.addresses[*].street", containsInAnyOrder("updated", "Carpatilor")))
        .andExpect(
            jsonPath("$.contactInfo.addresses[*].building", containsInAnyOrder("updated", "9")))
        .andExpect(
            jsonPath(
                "$.contactInfo.addresses[*].state", containsInAnyOrder("updated", "Brasov County")))
        .andExpect(jsonPath("$.contactInfo.addresses[*].room", containsInAnyOrder("123", "23")))
        .andExpect(jsonPath("$.contactInfo.phones[*].countryCode", containsInAnyOrder("XX", "RO")))
        .andExpect(
            jsonPath(
                "$.contactInfo.phones[*].phoneNumber",
                containsInAnyOrder("111111111", "07771112")));
  }

  @Test
  public void
      givenPathIdDifferentThanDTOId_whenUpdatingCompanyWithAdminRole_ShouldExpectInconsistentDataException()
          throws Exception {
    Company companyToUpdate = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(companyToUpdate);

    CompanyDto companyDto = companyHelper.getCompanyMapper().toDto(savedCompany);
    companyDto.setStatus(null);

    companyDto.setId("DIFFERENT_ID");
    mockMvc
        .perform(
            authorized(put(url + "/" + savedCompany.getId()), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errorCategory").value(ErrorCategory.CLIENT_ERROR.name()));
  }

  @Test
  public void whenUpdatingCompanyWithUserRole_ShouldBeForbidden() throws Exception {
    CompanyDto companyDto = companyHelper.getCompanyMapper().toDto(companyHelper.create());
    companyDto.setId("random");
    companyDto.getContactInfo().setId("random");
    companyDto.getContactInfo().getAddresses().get(0).setId("random");
    companyDto.getContactInfo().getPhones().get(0).setId("random");
    mockMvc
        .perform(
            authorized(put(url + "/RANDOM_ID"), userClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }
}
