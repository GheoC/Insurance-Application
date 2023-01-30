package com.pot.users.helpers;

import com.pot.common.enums.UserStatus;
import com.pot.common.enums.UserType;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.entity.user.UserProfile;
import com.pot.users.mapper.UserProfileMapper;
import com.pot.users.repository.UserProfileRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Getter
public class UserProfileHelper {
  private final UserHelper userHelper;
  private final UserDetailsHelper userDetailsHelper;
  private final ClientRoleHelper clientRoleHelper;
  private final ContactInfoHelper contactInfoHelper;
  private final UserProfileMapper userProfileMapper;
  private final UserProfileRepository userProfileRepository;

  public UserProfile create() {
    UserProfile userProfile = new UserProfile();
    userProfile.setUser(userHelper.create());
    userProfile.setUserDetails(userDetailsHelper.create());
    userProfile.setContactInfo(contactInfoHelper.create());
    userProfile.setRoles(clientRoleHelper.create());
    userProfile.setStatus(UserStatus.ACTIVE);

    return userProfile;
  }

  public UserProfile saveToDatabase(UserProfile userProfile) {
    ContactInfo savedContactInfo =
        contactInfoHelper.saveContactInfoToDB(userProfile.getContactInfo());
    userProfile.setContactInfo(savedContactInfo);
    userProfile.setType(UserType.USER);
    userProfile.setStatus(UserStatus.ACTIVE);

    return userProfileRepository.save(userProfile);
  }

  public void clearDatabase() {
    userProfileRepository.deleteAll();
  }
}
