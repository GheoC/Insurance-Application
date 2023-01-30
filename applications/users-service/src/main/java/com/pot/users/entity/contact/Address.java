package com.pot.users.entity.contact;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Address extends BaseEntity {
  private String country;
  private String city;
  private String street;
  private String building;
  private String state;
  private String room;
}
