package com.pot.users.entity.contact;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
public class Phone extends BaseEntity {
  private String countryCode;

  private String phoneNumber;
}
