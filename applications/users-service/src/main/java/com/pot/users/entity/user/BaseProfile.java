package com.pot.users.entity.user;

import com.pot.common.enums.UserStatus;
import com.pot.common.enums.UserType;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class BaseProfile extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private UserType type;

  @Enumerated(EnumType.STRING)
  private UserStatus status;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id")
  private User user;
}
