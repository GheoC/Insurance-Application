package com.pot.benefits.controller;

import com.pot.benefits.dto.EnrolmentDto;
import com.pot.benefits.mapper.EnrolmentMapper;
import com.pot.benefits.service.EnrolmentService;
import com.pot.error.exceptions.InconsistentDataException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/benefit-service")
public class EnrolmentController {
  private final EnrolmentService enrolmentService;
  private final EnrolmentMapper enrolmentMapper;

  @PostMapping("/companies/{companyId}/users/{userId}/enrolment")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public EnrolmentDto enrollInsuranceToUser(
      @RequestBody @Valid EnrolmentDto enrolmentDto,
      @PathVariable String companyId,
      @PathVariable String userId) {
    if (!userId.equals(enrolmentDto.getUserProfileId())) {
      throw new InconsistentDataException("IDs must match!");
    }
    return enrolmentMapper.toDto(
        enrolmentService.enrollUser(enrolmentMapper.toEntity(enrolmentDto), companyId));
  }

  @PostMapping("/companies/{companyId}/users/{userId}/enrolment/{id}")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#companyId)")
  public void cancelEnrolment(
      @PathVariable String id, @PathVariable String userId, @PathVariable String companyId) {
    enrolmentService.cancel(id, userId, companyId);
  }

  @GetMapping("/companies/{companyId}/users/{userId}/enrolment")
  @PreAuthorize(
      "@authorizationEvaluator.isAdminOrCompanyManager(#companyId) or @authorizationEvaluator.isAccountOwner(#userId)")
  public List<EnrolmentDto> getEnrolmentsForUser(
      @PathVariable String companyId, @PathVariable String userId) {
    return enrolmentService.getEnrolments(companyId, userId).stream()
        .map(enrolmentMapper::toDto)
        .toList();
  }
}
