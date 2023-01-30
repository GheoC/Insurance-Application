package com.pot.users.entity.contact;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class ContactInfo extends BaseEntity {
  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "contact_info_address",
      joinColumns = {@JoinColumn(name = "contact_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "address_id", referencedColumnName = "id")})
  @Fetch(value = FetchMode.SUBSELECT)
  private List<Address> addresses = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "contact_info_phone",
      joinColumns = {@JoinColumn(name = "contact_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "phone_id", referencedColumnName = "id")})
  @Fetch(value = FetchMode.SUBSELECT)
  private List<Phone> phones = new ArrayList();
}
