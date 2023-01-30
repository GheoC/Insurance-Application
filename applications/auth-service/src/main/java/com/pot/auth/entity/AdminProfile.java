package com.pot.auth.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class AdminProfile extends BaseProfile {
  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "admin_role",
      joinColumns = @JoinColumn(name = "admin_id"),
      inverseJoinColumns = @JoinColumn(name = "function_id"))
  private List<Function> roles;
}