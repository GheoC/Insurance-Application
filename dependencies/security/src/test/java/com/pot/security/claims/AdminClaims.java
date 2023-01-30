package com.pot.security.claims;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class AdminClaims extends CustomTestClaims
{
    public AdminClaims(String username, String userId, UserStatus status, List<FunctionName> roles) {
        super(username, userId, status, roles);
    }
}
