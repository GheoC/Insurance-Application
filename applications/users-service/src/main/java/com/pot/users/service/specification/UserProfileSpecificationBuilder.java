package com.pot.users.service.specification;

import com.pot.common.enums.FunctionName;
import com.pot.common.enums.UserStatus;
import com.pot.database.specification.BaseSpecification;
import com.pot.database.specification.CompositeType;
import com.pot.users.entity.company.Company;

import com.pot.users.entity.company.Company_;
import com.pot.users.entity.function.Function;
import com.pot.users.entity.function.Function_;
import com.pot.users.entity.user.*;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

public class UserProfileSpecificationBuilder extends BaseSpecification<BaseProfile> {
  public UserProfileSpecificationBuilder(CompositeType compositeType) {
    super(compositeType);
  }

  public UserProfileSpecificationBuilder hasCompanyId(String companyId) {
    addSpecificationIfNotEmptyOrNull(
        companyId,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, Company> companiesUsers = castedRoot.join(UserProfile_.COMPANY);
          return criteriaBuilder.equal(companiesUsers.get(Company_.ID), companyId);
        });
    return this;
  }

  public UserProfileSpecificationBuilder likeFirstName(String firstName) {
    addSpecificationIfNotEmptyOrNull(
        firstName,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, UserDetails> profileDetails =
              castedRoot.join(UserProfile_.USER_DETAILS);
          return criteriaBuilder.like(
              profileDetails.get(UserDetails_.firstName), "%" + firstName + "%");
        });
    return this;
  }

  public UserProfileSpecificationBuilder likeLastName(String lastName) {
    addSpecificationIfNotEmptyOrNull(
        lastName,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, UserDetails> profileDetails =
              castedRoot.join(UserProfile_.USER_DETAILS);
          return criteriaBuilder.like(
              profileDetails.get(UserDetails_.lastName), "%" + lastName + "%");
        });
    return this;
  }

  public UserProfileSpecificationBuilder likeEmail(String email) {
    addSpecificationIfNotEmptyOrNull(
        email,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, UserDetails> profileDetails =
              castedRoot.join(UserProfile_.USER_DETAILS);
          return criteriaBuilder.like(profileDetails.get(UserDetails_.email), "%" + email + "%");
        });
    return this;
  }

  public UserProfileSpecificationBuilder hasBirthday(LocalDate birthday) {
    addSpecificationIfNotEmptyOrNull(
        birthday,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, UserDetails> profileDetails =
              castedRoot.join(UserProfile_.USER_DETAILS);
          return criteriaBuilder.equal(profileDetails.get(UserDetails_.birthday), birthday);
        });
    return this;
  }

  public UserProfileSpecificationBuilder hasStatus(UserStatus status) {
    specifications.add(
        (root, query, criteriaBuilder) -> root.get(UserProfile_.status).in(List.of(status)));
    // criteriaBuilder.equal(root.get(UserProfile_.STATUS), status));
    return this;
  }

  public UserProfileSpecificationBuilder likeSnn(String snn) {
    addSpecificationIfNotEmptyOrNull(
        snn,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> castedRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, UserDetails> profileDetails =
              castedRoot.join(UserProfile_.USER_DETAILS);
          return criteriaBuilder.like(profileDetails.get(UserDetails_.snn), "%" + snn + "%");
        });
    return this;
  }

  public UserProfileSpecificationBuilder containsFunction(List<FunctionName> functions) {
    addSpecificationIfNotEmptyOrNull(
        functions,
        (root, query, criteriaBuilder) -> {
          Root<UserProfile> userRoot = criteriaBuilder.treat(root, UserProfile.class);
          Join<UserProfile, ClientRole> profileRoles =
              userRoot.join(UserProfile_.roles, JoinType.LEFT);
          Predicate usersPredicate = profileRoles.get(ClientRole_.name).in(functions);

          Root<AdminProfile> adminRoot = criteriaBuilder.treat(root, AdminProfile.class);
          Join<AdminProfile, Function> adminFunctions =
              adminRoot.join(AdminProfile_.functions, JoinType.LEFT);
          Predicate adminPredicate = adminFunctions.get(Function_.name).in(functions);

          return criteriaBuilder.or(usersPredicate, adminPredicate);
        });
    return this;
  }
}
