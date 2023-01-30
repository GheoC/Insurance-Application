package com.pot.users.dto.searchparams;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.pot.database.specification.CompositeType;
import com.pot.users.entity.user.BaseProfile;
import com.pot.users.service.specification.UserProfileSpecificationBuilder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileSearchParams {
  private String companyId;
  private String firstName;
  private String lastName;
  private String email;
  private LocalDate birthday;
  private UserStatus status = UserStatus.ACTIVE;
  private String snn;
  private List<FunctionName> function;

  public Specification<BaseProfile> buildSpecification(CompositeType compositeType) {
    return new UserProfileSpecificationBuilder(compositeType)
        .hasCompanyId(companyId)
        .likeFirstName(firstName)
        .likeLastName(lastName)
        .likeEmail(email)
        .hasBirthday(birthday)
        .hasStatus(status)
        .likeSnn(snn)
        .containsFunction(function)
        .build();
  }
}
