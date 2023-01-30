package com.pot.users.entity.company;

import com.pot.common.enums.CompanyStatus;
import com.pot.database.entity.BaseEntity;
import com.pot.users.entity.contact.ContactInfo;
import com.pot.users.entity.function.Function;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Company extends BaseEntity {
  private String companyName;

  private String countryCode;

  private String email;

  private String site;

  @Enumerated(EnumType.STRING)
  private CompanyStatus status;

  @OneToOne
  @JoinColumn(name = "contact_id")
  private ContactInfo contactInfo;

  @ManyToMany
  @JoinTable(
      name = "company_function",
      joinColumns = @JoinColumn(name = "company_id"),
      inverseJoinColumns = @JoinColumn(name = "function_id"))
  private List<Function> functions = new ArrayList<>();
}
