package com.pot.users.helpers;

import com.pot.common.enums.FunctionName;
import com.pot.users.entity.user.ClientRole;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Getter
public class ClientRoleHelper {
  public List<ClientRole> create() {
    ClientRole clientRole1 = new ClientRole(FunctionName.CONSUMER);
    ClientRole clientRole2 = new ClientRole(FunctionName.CONSUMER_CLAIM_MANAGER);

    return List.of(clientRole1, clientRole2);
  }
}
