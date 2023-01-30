package com.pot.benefits.claim;

import com.pot.benefits.BaseBenefitServiceTestClass;
import com.pot.benefits.dto.ClaimDto;
import com.pot.benefits.dto.ClaimInformationDto;
import com.pot.benefits.dto.searchparams.ClaimSearchParams;
import com.pot.benefits.entity.Claim;
import com.pot.benefits.entity.Enrolment;
import com.pot.benefits.entity.Transaction;
import com.pot.common.enums.ClaimStatus;
import com.pot.common.enums.FunctionName;
import com.pot.common.enums.TransactionType;
import com.pot.common.enums.UserStatus;
import com.pot.security.claims.UserClaims;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ClaimIntegrationTests extends BaseBenefitServiceTestClass {
  private final String BASE_URL = "/benefit-service/companies/";

  @BeforeEach
  public void setUp() {
    transactionHelper.clearDatabase();
    objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  public void whenCreateClaimWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());

    ClaimDto claimDto = new ClaimDto();
    claimDto.setSerialNumber("abc");
    claimDto.setAmount(BigDecimal.valueOf(50));
    claimDto.setServiceDate(LocalDate.of(2022, 10, 28));
    claimDto.setEnrolmentId(enrolment.getId());

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims";
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenCreateClaimWithOwnerClaims_ShouldReceiveStatusOk() throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());

    ClaimDto claimDto = new ClaimDto();
    claimDto.setSerialNumber("abc2");
    claimDto.setAmount(BigDecimal.valueOf(50));
    claimDto.setServiceDate(LocalDate.of(2022, 10, 28));
    claimDto.setEnrolmentId(enrolment.getId());

    UserClaims owner =
        authUserHelper.createCustomUserClaims(
            "random",
            "random",
            UserStatus.ACTIVE,
            List.of(FunctionName.CONSUMER),
            "random@random.com",
            enrolment.getUserProfileId(),
            enrolment.getInsurance().getBenefitPackage().getCompanyId());

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims";
    mockMvc
        .perform(
            authorized(post(url), owner)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenGettingClaimWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());
    Claim claim = claimHelper.create();
    claim.setEnrolment(enrolment);
    Claim savedClaim = claimHelper.saveToDatabase(claim);

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims/"
            + savedClaim.getId();
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(savedClaim.getId()))
        .andExpect(jsonPath("$.serialNumber").value(savedClaim.getSerialNumber()))
        .andExpect(jsonPath("$.amount").value(savedClaim.getAmount().doubleValue()))
        .andExpect(jsonPath("$.serviceDate").value(savedClaim.getServiceDate().toString()))
        .andExpect(jsonPath("$.enrolmentId").value(savedClaim.getEnrolment().getId()))
        .andExpect(
            jsonPath("$.insuranceType")
                .value(savedClaim.getEnrolment().getInsurance().getType().name()))
        .andExpect(jsonPath("$.status").value(savedClaim.getClaimInformation().getStatus().name()));
  }

  @Test
  public void whenSearchingClaimsWithAdminClaims_ShouldReceiveStatusOk() throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    mockMvc
        .perform(
            authorized(get(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenSearchingClaimsWithCompanyManager_ShouldReceiveStatusOk() throws Exception {
    String url = "/benefit-service/claims/search";
    ClaimSearchParams searchParams = new ClaimSearchParams();
    searchParams.setCompanyId("my_company");
    UserClaims companyManager =
        authUserHelper.createCustomUserClaims(
            "random",
            "random",
            UserStatus.ACTIVE,
            List.of(FunctionName.COMPANY_MANAGER),
            "randaom@random.ro",
            "random",
            searchParams.getCompanyId());

    mockMvc
        .perform(
            authorized(get(url), companyManager)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(searchParams))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  public void whenApprovingClaimWithAdminManager_ShouldReceiveStatusOk() throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());
    Claim claim = claimHelper.create();
    claim.setEnrolment(enrolment);
    Claim savedClaim = claimHelper.saveToDatabase(claim);
    Transaction transaction = new Transaction();
    transaction.setEnrolment(savedClaim.getEnrolment());
    transaction.setClaim(null);
    transaction.setAmount(BigDecimal.valueOf(200));
    transaction.setType(TransactionType.COLLECT);
    transactionHelper.getTransactionRepository().save(transaction);

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims/"
            + savedClaim.getId()
            + "/status";

    ClaimInformationDto claimInformationDto =
        new ClaimInformationDto(ClaimStatus.APPROVED, "random comment");
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimInformationDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    savedClaim = claimHelper.getClaimRepository().findById(savedClaim.getId()).get();

    assertEquals(savedClaim.getClaimInformation().getStatus(), claimInformationDto.getStatus());
    assertEquals(savedClaim.getClaimInformation().getComment(), claimInformationDto.getComment());
  }

  @Test
  public void
      givenClaimWithStatusNotPending_whenApprovingClaimWithAdminManager_ShouldReceiveException()
          throws Exception {
    Enrolment enrolment = enrolmentHelper.saveToDatabase(enrolmentHelper.create());
    Claim claim = claimHelper.create();
    claim.setEnrolment(enrolment);
    claim.getClaimInformation().setStatus(ClaimStatus.APPROVED);
    Claim savedClaim = claimHelper.saveToDatabase(claim);

    String url =
        BASE_URL
            + enrolment.getInsurance().getBenefitPackage().getCompanyId()
            + "/users/"
            + enrolment.getUserProfileId()
            + "/claims/"
            + savedClaim.getId()
            + "/status";

    ClaimInformationDto claimInformationDto =
        new ClaimInformationDto(ClaimStatus.APPROVED, "random comment");
    mockMvc
        .perform(
            authorized(post(url), authUserHelper.createDefaultAdminClaims())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(claimInformationDto))
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());
  }
}
