package com.pot.benefits.controller;

import com.pot.benefits.dto.BenefitPackageDto;
import com.pot.benefits.dto.searchparams.BenefitPackageSearchParams;
import com.pot.benefits.mapper.BenefitPackageMapper;
import com.pot.benefits.service.BenefitPackageService;

import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import com.pot.database.specification.CompositeType;
import com.pot.error.exceptions.InconsistentDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/benefit-service")
public class BenefitPackageController {
  private final BenefitPackageService benefitPackageService;
  private final BenefitPackageMapper benefitPackageMapper;

  @PostMapping("/companies/{companyId}/plan-package")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public BenefitPackageDto addPackage(
      @PathVariable String companyId,
      @RequestBody @Validated(OnCreate.class) BenefitPackageDto benefitPackageDto,
      @RequestHeader("Authorization") String bearerToken) {
    if (!companyId.equals(benefitPackageDto.getCompanyId())) {
      throw new InconsistentDataException("IDs must match!");
    }
    return benefitPackageMapper.toDto(
        benefitPackageService.create(benefitPackageMapper.toEntity(benefitPackageDto)));
  }

  @GetMapping("/companies/{companyId}/plan-package/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public BenefitPackageDto getPackage(@PathVariable String companyId, @PathVariable String id) {
    return benefitPackageMapper.toDto(benefitPackageService.get(companyId, id));
  }

  @GetMapping("/plan-packages/search")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#searchParams.companyId)")
  public Page<BenefitPackageDto> searchPackages(
      @RequestBody BenefitPackageSearchParams searchParams, Pageable pageable) {
    return benefitPackageService
        .search(searchParams.buildSpecification(CompositeType.AND), pageable)
        .map(benefitPackageMapper::toDto);
  }

  @PutMapping("/companies/{companyId}/plan-package/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#benefitPackageDto.companyId)")
  public BenefitPackageDto updatePackage(
      @PathVariable String id,
      @RequestBody @Validated(OnUpdate.class) BenefitPackageDto benefitPackageDto) {
    if (!id.equals(benefitPackageDto.getId())) {
      throw new InconsistentDataException("IDs must match!");
    }
    return benefitPackageMapper.toDto(
        benefitPackageService.update(benefitPackageMapper.toEntity(benefitPackageDto)));
  }

  @PostMapping("/companies/{companyId}/plan-package/{id}/status")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public void deactivatePackage(@PathVariable String companyId, @PathVariable String id) {
    benefitPackageService.deactivate(companyId, id);
  }
}
