package com.pot.users.entity.function;

import com.pot.common.enums.FunctionName;
import com.pot.database.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Function extends BaseEntity {
  @Enumerated(EnumType.STRING)
  private FunctionName name;

  public Function(FunctionName name) {
    this.name = name;
  }
}