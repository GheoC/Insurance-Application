package com.pot.benefits.controller;

import com.pot.benefits.dto.ClaimDto;
import com.pot.benefits.dto.ClaimInformationDto;
import com.pot.benefits.dto.searchparams.ClaimSearchParams;
import com.pot.benefits.entity.Claim;
import com.pot.benefits.mapper.ClaimInformationMapper;
import com.pot.benefits.mapper.ClaimMapper;
import com.pot.benefits.service.ClaimService;
import com.pot.database.specification.CompositeType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/benefit-service")
public class ClaimController {

  private final ClaimService claimService;
  private final ClaimMapper claimMapper;
  private final ClaimInformationMapper claimInformationMapper;

  @PostMapping("/companies/{companyId}/users/{userId}/claims")
  @PreAuthorize(
      "@authorizationEvaluator.isAdminOrCompanyManager(#companyId) or @authorizationEvaluator.isAccountOwner(#userId)")
  public ClaimDto fileClaim(
      @PathVariable String companyId,
      @PathVariable String userId,
      @RequestBody @Valid ClaimDto claimDto) {
    return claimMapper.toDto(
        claimService.create(claimMapper.toEntity(claimDto), companyId, userId));
  }

  @GetMapping("/companies/{companyId}/users/{userId}/claims/{claimId}")
  @PreAuthorize(
      "@authorizationEvaluator.isAdminOrCompanyManager(#companyId) or @authorizationEvaluator.isAccountOwner(#userId)")
  public ClaimDto getClaim(
      @PathVariable String companyId, @PathVariable String userId, @PathVariable String claimId) {
    return claimMapper.toDto(claimService.get(claimId, companyId, userId));
  }

  @GetMapping("/claims/search")
  @PreAuthorize("@authorizationEvaluator.isAdminOrCompanyManager(#searchParams.companyId)")
  public Page<ClaimDto> searchClaims(
          @RequestBody ClaimSearchParams searchParams, Pageable pageable) {
    Page<Claim> search =
        claimService.search(searchParams.buildSpecification(CompositeType.AND), pageable);
    return search.map(claimMapper::toDto);
  }

  @PostMapping("/companies/{companyId}/users/{userId}/claims/{claimId}/status")
  @PreAuthorize(
      "@authorizationEvaluator.isAdminOrCompanyManager(#companyId) or @authorizationEvaluator.isAccountOwner(#userId)")
  public void changeClaimStatus(
      @PathVariable String companyId,
      @PathVariable String userId,
      @PathVariable String claimId,
      @RequestBody ClaimInformationDto claimInformationDto) {
    claimService.changeStatus(
        claimId, companyId, userId, claimInformationMapper.toEntity(claimInformationDto));
  }
}
