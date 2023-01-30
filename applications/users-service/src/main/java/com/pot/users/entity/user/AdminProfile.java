package com.pot.users.entity.user;

import com.pot.users.entity.function.Function;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class AdminProfile extends BaseProfile {
  @ManyToMany
  @JoinTable(
      name = "admin_role",
      joinColumns = @JoinColumn(name = "admin_id"),
      inverseJoinColumns = @JoinColumn(name = "function_id"))
  private List<Function> functions;
}