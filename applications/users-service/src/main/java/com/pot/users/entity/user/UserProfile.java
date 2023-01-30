package com.pot.users.entity.user;

import com.pot.users.entity.company.Company;
import com.pot.users.entity.contact.ContactInfo;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserProfile extends BaseProfile {
  @OneToOne
  @JoinColumn(name = "contact_id")
  private ContactInfo contactInfo;

  @ManyToOne
  @JoinColumn(name = "company_id")
  private Company company;

  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_details_id")
  private UserDetails userDetails;

  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_profile_id")
  private List<ClientRole> roles = new ArrayList<>();
}
