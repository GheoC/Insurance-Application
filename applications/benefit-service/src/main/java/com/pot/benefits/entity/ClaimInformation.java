package com.pot.benefits.entity;

import com.pot.common.enums.ClaimStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
@Getter
@Setter
public class ClaimInformation {
  @Enumerated(value = EnumType.STRING)
  private ClaimStatus status;

  private String comment;
}
