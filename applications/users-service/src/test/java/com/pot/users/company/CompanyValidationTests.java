package com.pot.users.company;

import com.pot.common.enums.ErrorCategory;
import com.pot.common.enums.FunctionName;
import com.pot.error.payload.ComplexExceptionPayload;
import com.pot.error.payload.model.Error;
import com.pot.security.claims.AdminClaims;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.dto.AddressDto;
import com.pot.users.dto.CompanyDto;
import com.pot.users.dto.PhoneDto;
import com.pot.users.entity.company.Company;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CompanyValidationTests extends BaseUsersServiceTestClass {

  private AdminClaims adminClaims;

  @BeforeEach
  public void setUp() {
    url = "/users-service/companies";
    objectMapper.registerModule(new JavaTimeModule());
    adminClaims = authUserHelper.createDefaultAdminClaims();
    userProfileHelper.clearDatabase();
    companyHelper.clearDatabase();
  }

  @Test
  public void givenCorrectInputs_whenCreatingCompany_ShouldSaveSuccessfullyCompanyInDatabase()
      throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    List<Company> all = companyHelper.getCompanyRepository().findAll();
    assertEquals(0, companyHelper.getCompanyRepository().findAll().size());

    mockMvc
        .perform(
            authorized(post(url), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").isNotEmpty())
        .andExpect(jsonPath("$.companyName").value(company.getCompanyName()))
        .andExpect(jsonPath("$.countryCode").value(company.getCountryCode()))
        .andExpect(jsonPath("$.email").value(company.getEmail()))
        .andExpect(jsonPath("$.site").value(company.getSite()))
        .andExpect(jsonPath("$.contactInfo.id").isNotEmpty())
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].country")
                .value(company.getContactInfo().getAddresses().get(0).getCountry()))
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].city")
                .value(company.getContactInfo().getAddresses().get(0).getCity()))
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].street")
                .value(company.getContactInfo().getAddresses().get(0).getStreet()))
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].building")
                .value(company.getContactInfo().getAddresses().get(0).getBuilding()))
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].state")
                .value(company.getContactInfo().getAddresses().get(0).getState()))
        .andExpect(
            jsonPath("$.contactInfo.addresses[0].room")
                .value(company.getContactInfo().getAddresses().get(0).getRoom()))
        .andExpect(
            jsonPath("$.contactInfo.phones[0].countryCode")
                .value(company.getContactInfo().getPhones().get(0).getCountryCode()))
        .andExpect(
            jsonPath("$.contactInfo.phones[0].phoneNumber")
                .value(company.getContactInfo().getPhones().get(0).getPhoneNumber()))
        .andReturn();
    assertEquals(1, companyHelper.getCompanyRepository().findAll().size());
  }

  @Test
  public void givenNullInputs_whenCreatingCompany_ShouldExpectExceptions() throws Exception {
    // GIVEN company with NULL fields
    CompanyDto company = new CompanyDto();

    // WHEN we try to create company we EXPECT ComplexExceptionPayload JSON
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN what field validation errors we expected should be the following:
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals(4, exceptionPayload.getErrors().size());
    assertEquals("countryCode", errorFields.get("countryCode"));
    assertEquals("site", errorFields.get("site"));
    assertEquals("companyName", errorFields.get("companyName"));
    assertEquals("contactInfo", errorFields.get("contactInfo"));
  }

  @Test
  public void givenWrongAddressInputs_whenCreatingCompany_ShouldExpectExceptions()
      throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    // GIVEN company that contains address with fields with wrong size string
    company.getContactInfo().getAddresses().get(0).setRoom("OVER_5_CHARACTERS_LONG");
    company.getContactInfo().getAddresses().get(0).setBuilding("OVER_10_CHARACTERS_LONG");

    // WHEN...
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN
    assertEquals(2, exceptionPayload.getErrors().size());
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals("contactInfo.addresses[0].room", errorFields.get("contactInfo.addresses[0].room"));
    assertEquals(
        "contactInfo.addresses[0].building", errorFields.get("contactInfo.addresses[0].building"));
  }

  @Test
  public void givenAddressWithNullFields_whenCreatingCompany_ShouldExpectExceptions()
      throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    // GIVEN company that contains Address with NULL fields
    AddressDto address = new AddressDto();
    company.getContactInfo().getAddresses().clear();
    company.getContactInfo().getAddresses().add(address);

    // WHEN...
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN
    assertEquals(6, exceptionPayload.getErrors().size());
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals(
        "contactInfo.addresses[0].building", errorFields.get("contactInfo.addresses[0].building"));
    assertEquals("contactInfo.addresses[0].room", errorFields.get("contactInfo.addresses[0].room"));
    assertEquals("contactInfo.addresses[0].city", errorFields.get("contactInfo.addresses[0].city"));
    assertEquals(
        "contactInfo.addresses[0].street", errorFields.get("contactInfo.addresses[0].street"));
    assertEquals(
        "contactInfo.addresses[0].state", errorFields.get("contactInfo.addresses[0].state"));
    assertEquals(
        "contactInfo.addresses[0].country", errorFields.get("contactInfo.addresses[0].country"));
  }

  @Test
  public void givenWrongPhoneInputs_whenCreatingCompany_ShouldExpectExceptions() throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    // GIVEN company that contains phone with fields with wrong size string
    company
        .getContactInfo()
        .getPhones()
        .get(0)
        .setCountryCode("OVER_3_CHARACTERS_LONG_CONTAINING_DIGITS");
    company
        .getContactInfo()
        .getPhones()
        .get(0)
        .setPhoneNumber("OVER_11_CHARACTERS_LONG_CONTAINING_LETTERS");

    // WHEN
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN
    assertEquals(4, exceptionPayload.getErrors().size());
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals(
        "contactInfo.phones[0].phoneNumber", errorFields.get("contactInfo.phones[0].phoneNumber"));
    assertEquals(
        "contactInfo.phones[0].countryCode", errorFields.get("contactInfo.phones[0].countryCode"));
  }

  @Test
  public void givenPhoneWithNullFields_whenCreatingCompany_ShouldExpectExceptions()
      throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    // GIVEN company that contains Phone with NULL fields
    PhoneDto phone = new PhoneDto();
    company.getContactInfo().getPhones().clear();
    company.getContactInfo().getPhones().add(phone);

    // WHEN
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN
    assertEquals(2, exceptionPayload.getErrors().size());
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals(
        "contactInfo.phones[0].phoneNumber", errorFields.get("contactInfo.phones[0].phoneNumber"));
    assertEquals(
        "contactInfo.phones[0].countryCode", errorFields.get("contactInfo.phones[0].countryCode"));
  }

  @Test
  public void
      givenContactInfoWithEmptyAddressesAndPhones_whenCreatingCompany_ShouldExpectExceptions()
          throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    // GIVEN a company with no Address and no Phone
    company.getContactInfo().getAddresses().clear();
    company.getContactInfo().getPhones().clear();

    // WHEN
    ComplexExceptionPayload exceptionPayload = getExceptionPayloadWhenCreateWrongCompany(company);
    Map<String, String> errorFields = getErrorFields(exceptionPayload);

    // THEN
    assertEquals(2, exceptionPayload.getErrors().size());
    assertEquals(ErrorCategory.VALIDATION_ERROR, exceptionPayload.getErrorCategory());
    assertEquals("contactInfo.addresses", errorFields.get("contactInfo.addresses"));
    assertEquals("contactInfo.phones", errorFields.get("contactInfo.phones"));
  }

  @Test
  public void givenNullIDs_whenUpdatingCompany_ShouldExpectExceptions() throws Exception {
    CompanyDto company = companyHelper.getCompanyMapper().toDto(companyHelper.create());

    mockMvc
        .perform(
            authorized(put(url + "/RANDOM_ID"), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors.[*].field", containsInAnyOrder("id", "contactInfo.id")));
  }

  @Test
  public void
      givenFunctionsThatContainsSUPER_ADMIN_whenUpdatingCompanyFunctions_ShouldExpectConstraintException()
          throws Exception {
    Company company = companyHelper.create();
    Company savedCompany = companyHelper.saveCompanyToDB(company);
    url = "/users-service/companies/" + savedCompany.getId() + "/functions";
    List<FunctionName> newFunctions = List.of(FunctionName.SUPER_ADMIN);

    mockMvc
        .perform(
            authorized(
                post(url)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(newFunctions))
                    .accept(MediaType.APPLICATION_JSON),
                adminClaims))
        .andExpect(status().isBadRequest())
        .andExpect(
            jsonPath("$.errors[0].field").value(List.of(FunctionName.SUPER_ADMIN).toString()));
  }

  private ComplexExceptionPayload getExceptionPayloadWhenCreateWrongCompany(CompanyDto company)
      throws Exception {
    return objectMapper.readValue(getMvcResult(company), ComplexExceptionPayload.class);
  }

  private String getMvcResult(CompanyDto company) throws Exception {
    return mockMvc
        .perform(
            authorized(post(url), adminClaims)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(company))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn()
        .getResponse()
        .getContentAsString();
  }

  private Map<String, String> getErrorFields(ComplexExceptionPayload exceptionPayload) {
    return exceptionPayload.getErrors().stream()
        .collect(Collectors.toMap(Error::getField, Error::getField, (a, b) -> b));
  }
}
