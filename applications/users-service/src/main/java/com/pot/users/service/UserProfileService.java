package com.pot.users.service;

import com.pot.common.enums.CompanyStatus;
import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.pot.common.enums.UserType;
import com.pot.error.exceptions.InconsistentDataException;
import com.pot.error.exceptions.ResourceNotFoundException;
import com.pot.users.entity.company.Company;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.entity.function.Function;
import com.pot.users.entity.user.BaseProfile;
import com.pot.users.entity.user.ClientRole;
import com.pot.users.entity.user.UserProfile;
import com.pot.users.repository.BaseProfileRepository;
import com.pot.users.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {
  private final UserProfileRepository userProfileRepository;
  private final BaseProfileRepository baseProfileRepository;
  private final ContactInfoService contactInfoService;
  private final CompanyService companyService;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  public UserProfile create(String companyId, UserProfile userProfile) {
    Company company = companyService.getCompanyById(companyId);
    if (!areRolesAllowedForUser(userProfile, company)) {
      throw new InconsistentDataException(
          "This user cannot have all this roles! Check company functions!");
    }
    userProfile.getUser().setPassword(passwordEncoder.encode(userProfile.getUser().getPassword()));
    ContactInfo savedContactInfo = contactInfoService.save(userProfile.getContactInfo());
    userProfile.setContactInfo(savedContactInfo);
    userProfile.setCompany(company);
    userProfile.setStatus(UserStatus.ACTIVE);
    userProfile.setType(UserType.USER);

    return userProfileRepository.save(userProfile);
  }

  @Transactional
  public UserProfile update(String companyId, UserProfile userProfile) {
    Company company = companyService.getCompanyById(companyId);
    if (!areRolesAllowedForUser(userProfile, company)) {
      throw new InconsistentDataException(
          "This user cannot have all this roles! Check company functions!");
    }
    UserProfile userProfileFromDb = getUserProfile(companyId, userProfile.getId());
    ContactInfo savedContactInfo = contactInfoService.save(userProfile.getContactInfo());
    userProfile.setContactInfo(savedContactInfo);
    userProfile.setUser(userProfileFromDb.getUser());
    userProfile.setCompany(userProfileFromDb.getCompany());
    userProfile.setStatus(userProfileFromDb.getStatus());
    userProfile.setType(userProfileFromDb.getType());

    return userProfileRepository.save(userProfile);
  }

  public void switchStatus(String companyId, String id) {
    UserProfile userProfile = getUserProfile(companyId, id);
    userProfile.setStatus(
        userProfile.getStatus() == UserStatus.ACTIVE ? UserStatus.INACTIVE : UserStatus.ACTIVE);
    if (userProfile.getStatus() == UserStatus.ACTIVE
        && userProfile.getCompany().getStatus() == CompanyStatus.INACTIVE) {
      throw new InconsistentDataException("User cannot be Activated! Company is NOT Active");
    }

    userProfileRepository.save(userProfile);
  }

  public UserProfile getUserProfile(String companyId, String id) {
    return userProfileRepository
        .findByIdAndCompanyId(id, companyId)
        .orElseThrow(() -> new ResourceNotFoundException("User profile NOT found"));
  }

  @Transactional
  public void deactivateUsers(String companyId) {
    userProfileRepository.updateUsersProfileStatusFromCompany(companyId, UserStatus.INACTIVE);
  }

  @Transactional
  public void removeFunctionsForCompanyUsers(List<FunctionName> functionNames, String companyId) {
    List<UserProfile> companyUsersProfile = userProfileRepository.findByCompanyId(companyId);
    List<ClientRole> removedClientRoles = functionNames.stream().map(ClientRole::new).toList();

    companyUsersProfile.forEach(
        userProfile ->
            userProfile.setRoles(
                userProfile.getRoles().stream()
                    .filter(clientRole -> !removedClientRoles.contains(clientRole))
                    .collect(Collectors.toList())));

    userProfileRepository.saveAll(companyUsersProfile);
  }

  public Page<BaseProfile> searchUsers(
      Specification<BaseProfile> specification, Pageable pageable) {
    return baseProfileRepository.findAll(specification, pageable);
  }

  private boolean areRolesAllowedForUser(UserProfile userProfile, Company company) {
    List<FunctionName> companyFunctions =
        company.getFunctions().stream().map(Function::getName).toList();
    List<FunctionName> userprofileFunctions =
        userProfile.getRoles().stream().map(ClientRole::getName).toList();

    return companyFunctions.containsAll(userprofileFunctions);
  }
}
