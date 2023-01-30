package com.pot.users.entity.user;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import java.time.LocalDate;

@Entity
@Getter
@Setter
public class UserDetails extends BaseEntity {
  private String firstName;

  private String lastName;

  private String email;

  private String snn;

  private LocalDate birthday;
}
