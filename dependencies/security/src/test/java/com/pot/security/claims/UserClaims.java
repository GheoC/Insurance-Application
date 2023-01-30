package com.pot.security.claims;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserClaims extends CustomTestClaims
{
    private String userProfileId;
    private String companyId;
    private String userEmail;

    public UserClaims(String username, String userId, UserStatus status, List<FunctionName> roles, String userEmail, String userProfileId, String companyId) {
        super(username, userId, status, roles);
        this.userProfileId = userProfileId;
        this.companyId = companyId;
        this.userEmail = userEmail;
    }
}
