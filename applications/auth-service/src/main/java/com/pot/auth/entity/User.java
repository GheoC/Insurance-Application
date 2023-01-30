package com.pot.auth.entity;

import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User extends BaseEntity {
  private String username;
  private String password;

  @OneToMany(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  List<BaseProfile> baseProfiles = new ArrayList<>();
}
