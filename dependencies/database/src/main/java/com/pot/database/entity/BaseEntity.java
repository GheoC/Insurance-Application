package com.pot.database.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.proxy.HibernateProxyHelper;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @Id
  @GeneratedValue(generator = "uuid2")
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  private String id;

  @Column(name = "created_at", updatable = false)
  @CreatedDate
  private LocalDateTime createdAt;

  @Column(name = "updated_at")
  @LastModifiedDate
  private LocalDateTime updatedAt;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null) return false;
    Class classWithoutInitializingProxy = HibernateProxyHelper.getClassWithoutInitializingProxy(this);
    String s = classWithoutInitializingProxy.getName().toString();
    if (HibernateProxyHelper.getClassWithoutInitializingProxy(this)
        != HibernateProxyHelper.getClassWithoutInitializingProxy(o)) return false;
    BaseEntity that = (BaseEntity) o;
    return id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
