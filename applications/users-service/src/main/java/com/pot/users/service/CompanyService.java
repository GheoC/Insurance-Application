package com.pot.users.service;

import com.pot.common.enums.CompanyStatus;
import com.pot.common.enums.FunctionName;
import com.pot.error.exceptions.ResourceNotFoundException;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.entity.function.Function;
import com.pot.users.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {

  private final CompanyRepository companyRepository;
  private final FunctionService functionService;
  private final ContactInfoService contactInfoService;
  @Lazy private final UserProfileService userProfileService;

  @Transactional
  public Company create(Company company) {
    company.setStatus(CompanyStatus.ACTIVE);
    ContactInfo savedContactInfo = contactInfoService.save(company.getContactInfo());
    company.setContactInfo(savedContactInfo);
    company.getFunctions().addAll(functionService.getDefaultFunctions());

    return companyRepository.save(company);
  }

  public Page<Company> searchCompanies(Specification<Company> specification, Pageable pageable) {
    return companyRepository.findAll(specification, pageable);
  }

  public Company getCompanyById(String id) {
    return companyRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Company NOT found"));
  }

  @Transactional
  public Company switchStatus(String companyId) {
    Company company = getCompanyById(companyId);
    company.setStatus(
        company.getStatus() == CompanyStatus.ACTIVE
            ? CompanyStatus.INACTIVE
            : CompanyStatus.ACTIVE);
    if (company.getStatus() == CompanyStatus.INACTIVE) {
      userProfileService.deactivateUsers(companyId);
    }

    return companyRepository.save(company);
  }

  @Transactional
  public void editFunctions(String id, List<Function> newFunctions) {
    Company company = getCompanyById(id);
    List<FunctionName> removedFunctionNames =
        company.getFunctions().stream()
            .filter(it -> !newFunctions.contains(it))
            .map(Function::getName)
            .toList();
    userProfileService.removeFunctionsForCompanyUsers(removedFunctionNames, company.getId());
    company.setFunctions(newFunctions);

    companyRepository.save(company);
  }

  @Transactional
  public Company update(Company company) {
    Company companyToUpdate = getCompanyById(company.getId());
    company.setStatus(companyToUpdate.getStatus());
    company.setContactInfo(contactInfoService.save(company.getContactInfo()));
    company.setFunctions(companyToUpdate.getFunctions());

    return companyRepository.save(company);
  }

  public List<Function> getFunctions(String id) {
    return getCompanyById(id).getFunctions();
  }
}
