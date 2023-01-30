package com.pot.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class UserProfile extends BaseProfile {

  private String companyId;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_profile_id")
  private List<ClientRole> roles = new ArrayList<>();
}
