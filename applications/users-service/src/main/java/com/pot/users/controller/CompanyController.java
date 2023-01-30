package com.pot.users.controller;

import com.pot.common.enums.FunctionName;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import com.pot.database.specification.CompositeType;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.validators.NotContainsSuperAdmin;
import com.pot.users.dto.CompanyDto;
import com.pot.users.dto.searchparams.CompanySearchParams;
import com.pot.users.mapper.CompanyMapper;
import com.pot.users.mapper.FunctionMapper;
import com.pot.users.service.CompanyService;
import com.pot.users.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users-service")
@Validated
public class CompanyController {
  private final CompanyService companyService;
  private final CompanyMapper companyMapper;
  private final FunctionMapper functionMapper;
  private final UserProfileService userProfileService;

  @PostMapping("/companies")
  @PreAuthorize("hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN)")
  public CompanyDto createCompany(@RequestBody @Validated(OnCreate.class) CompanyDto companyDto) {
    return companyMapper.toDto(companyService.create(companyMapper.toEntity(companyDto)));
  }

  @PostMapping("/companies/search")
  @PreAuthorize("hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN)")
  public Page<CompanyDto> searchCompanies(
          @RequestBody @Valid CompanySearchParams searchParams, Pageable pageable) {
    return companyService
        .searchCompanies(searchParams.buildSpecification(CompositeType.AND), pageable)
        .map(companyMapper::toDto);
  }

  @GetMapping("/companies/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#id)")
  public CompanyDto getCompanyById(@PathVariable String id) {
    return companyMapper.toDto(companyService.getCompanyById(id));
  }

  @PostMapping("/companies/{id}/status")
  @PreAuthorize("hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN)")
  public CompanyDto switchStatus(@PathVariable String id) {
    return companyMapper.toDto(companyService.switchStatus(id));
  }

  @PostMapping("/companies/{id}/functions")
  @PreAuthorize(
      "hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN) or @authorizationEvaluator.isCompanySettingManager(#id)")
  public void editFunctions(
      @PathVariable String id, @RequestBody @NotContainsSuperAdmin List<FunctionName> functions) {
    companyService.editFunctions(id, functionMapper.toEntities(functions));
  }

  @GetMapping("/companies/{id}/functions")
  @PreAuthorize(
      "hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN) or @authorizationEvaluator.isCompanySettingManager(#id)")
  public List<FunctionName> getFunctions(@PathVariable String id) {
    return functionMapper.toFunctionNames(companyService.getFunctions(id));
  }

  @PutMapping("/companies/{id}")
  @PreAuthorize("hasRole(T(com.pot.common.enums.FunctionName).SUPER_ADMIN)")
  public CompanyDto update(
      @PathVariable String id, @RequestBody @Validated(OnUpdate.class) CompanyDto company) {
    if (!id.equals(company.getId())) {
      throw new InconsistentDataException("IDs must match!");
    }
    return companyMapper.toDto(companyService.update(companyMapper.toEntity(company)));
  }
}
