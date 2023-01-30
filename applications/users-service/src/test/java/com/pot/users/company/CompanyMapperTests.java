package com.pot.users.company;

import com.pot.common.enums.FunctionName;
import com.pot.users.BaseUsersServiceTestClass;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.contact.Address;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.entity.contact.Phone;
import com.pot.users.entity.function.Function;
import com.pot.users.dto.AddressDto;
import com.pot.users.dto.CompanyDto;
import com.pot.users.dto.ContactInfoDto;
import com.pot.users.dto.PhoneDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class CompanyMapperTests extends BaseUsersServiceTestClass {

  private Address address;
  private Phone phone;
  private ContactInfo contactInfo;
  private Company company;

  @BeforeEach
  public void setUp() {
    address = addressHelper.create();
    phone = phoneHelper.create();
    contactInfo = contactInfoHelper.create();
    company = companyHelper.create();
    //functionDtos = functionHelper.createListDto();
  }

  @Test
  public void whenMappingAddressToDtoAndBackToEntity_ShouldGetSameAddressAttributes() {
    AddressDto addressDto = addressHelper.getAddressMapper().toDto(address);
    Address addressFromDto = addressHelper.getAddressMapper().toEntity(addressDto);

    assertEquals("Romania", addressFromDto.getCountry());
    assertEquals("Brasov", addressFromDto.getCity());
    assertEquals("23", addressFromDto.getRoom());
    assertEquals("Brasov County", addressFromDto.getState());
    assertEquals("9", addressFromDto.getBuilding());
    assertEquals("Carpatilor", addressFromDto.getStreet());
  }

  @Test
  public void whenMappingPhoneToDtoAndBackToEntity_ShouldGetSamePhoneAttributes() {
    PhoneDto phoneDto = phoneHelper.getPhoneMapper().toDto(phone);
    Phone phoneFromDto = phoneHelper.getPhoneMapper().toEntity(phoneDto);

    assertEquals("07771112", phoneFromDto.getPhoneNumber());
  }

  @Test
  public void whenMappingContactInfoToDtoAndBackToEntity_ShouldGetSameContactInfoAttributes() {
    ContactInfoDto contactInfoDto = contactInfoHelper.getContactInfoMapper().toDto(contactInfo);
    ContactInfo contactInfoFromDto =
        contactInfoHelper.getContactInfoMapper().toEntity(contactInfoDto);

    assertEquals(1, contactInfoDto.getAddresses().size());
    assertEquals("Carpatilor", contactInfoFromDto.getAddresses().get(0).getStreet());

    assertEquals(1, contactInfoDto.getPhones().size());
    assertEquals("07771112", contactInfoFromDto.getPhones().get(0).getPhoneNumber());
  }

  @Test
  public void whenMappingCompanyToDtoAndBackToEntity_ShouldGetSameCompanyAttributes() {
    CompanyDto companyDto = companyHelper.getCompanyMapper().toDto(company);
    companyDto.setStatus(null);
    Company companyFromDto = companyHelper.getCompanyMapper().toEntity(companyDto);

    assertEquals(companyDto.getCompanyName(), companyFromDto.getCompanyName());
    assertEquals("RO", companyFromDto.getCountryCode());
    assertEquals(companyDto.getEmail(), companyFromDto.getEmail());
    assertEquals(companyDto.getSite(), companyFromDto.getSite());

    Assertions.assertEquals("Carpatilor", companyFromDto.getContactInfo().getAddresses().get(0).getStreet());
    Assertions.assertEquals("07771112", companyFromDto.getContactInfo().getPhones().get(0).getPhoneNumber());
  }

  @Test
  public void whenMappingFunctionsNamesToFunctionsAbdBack_ShouldGetFunctionsName() {
    List<FunctionName> functionNames = functionHelper.createFunctionNames();
    List<Function> functions = functionHelper.getFunctionMapper().toEntities(functionNames);
    functions.forEach(
            function -> {
              Function functionFromDb =
                      functionHelper.getFunctionRepository().findById(function.getId()).get();
              assertEquals(functionFromDb.getName(), function.getName());
            });

    functions.forEach(function -> {
      FunctionName functionName = functionHelper.getFunctionMapper().mapFunctionToFunctionName(function);
      assertEquals(functionName, function.getName());
    });
  }
}
