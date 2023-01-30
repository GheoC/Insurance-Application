package com.pot.users.entity.user;

import com.pot.common.enums.FunctionName;
import com.pot.database.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientRole extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private FunctionName name;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof ClientRole)) return false;
    ClientRole that = (ClientRole) o;
    return name == that.name;
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }
}
