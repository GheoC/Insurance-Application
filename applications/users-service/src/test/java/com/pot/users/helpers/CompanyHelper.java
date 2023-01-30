package com.pot.users.helpers;

import com.pot.common.enums.CompanyStatus;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.mapper.CompanyMapper;
import com.pot.users.repository.CompanyRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Getter
public class CompanyHelper {
  private final ContactInfoHelper contactInfoHelper;
  private final CompanyMapper companyMapper;
  private final CompanyRepository companyRepository;
  private final FunctionHelper functionHelper;

  public Company create() {
    Company company = new Company();
    company.setCompanyName("coherent" + UUID.randomUUID());
    company.setCountryCode("RO");
    company.setEmail("coherent@gmail.com" + UUID.randomUUID());
    company.setSite("www.coherent.com" + UUID.randomUUID());
    company.setContactInfo(contactInfoHelper.create());
    company.setFunctions(functionHelper.getFunctions());

    return company;
  }

  public Company saveCompanyToDB(Company company) {
    ContactInfo savedContactInfo = contactInfoHelper.saveContactInfoToDB(company.getContactInfo());
    company.setContactInfo(savedContactInfo);
    company.setStatus(CompanyStatus.ACTIVE);
    return companyRepository.save(company);
  }

  public Company getCompany(String id) {
    return companyRepository.findById(id).get();
  }

  public void clearDatabase() {
    companyRepository.deleteAll();
  }
}
