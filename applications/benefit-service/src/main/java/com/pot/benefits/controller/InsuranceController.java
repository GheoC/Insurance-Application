package com.pot.benefits.controller;

import com.pot.benefits.dto.InsuranceDto;
import com.pot.benefits.mapper.InsuranceMapper;
import com.pot.benefits.service.InsuranceService;
import com.pot.common.validations.OnCreate;
import com.pot.common.validations.OnUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/benefit-service")
public class InsuranceController {
  private final InsuranceService insuranceService;
  private final InsuranceMapper insuranceMapper;

  @PostMapping("/companies/{companyId}/plan-package/{packageId}/insurances")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public InsuranceDto createInsurance(
      @PathVariable String companyId,
      @PathVariable String packageId,
      @RequestBody @Validated(OnCreate.class) InsuranceDto insuranceDto) {
    return insuranceMapper.toDto(
        insuranceService.create(insuranceMapper.toEntity(insuranceDto), companyId, packageId));
  }

  @PutMapping("/companies/{companyId}/plan-package/{packageId}/insurances/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public InsuranceDto updateInsurance(
      @PathVariable String companyId,
      @PathVariable String packageId,
      @PathVariable String id,
      @RequestBody @Validated(OnUpdate.class) InsuranceDto insuranceDto) {
    return insuranceMapper.toDto(
        insuranceService.update(insuranceMapper.toEntity(insuranceDto), packageId,companyId));
  }
}
