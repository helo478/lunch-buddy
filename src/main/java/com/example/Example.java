package com.example;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
public class Example {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  private String name;

  public long getId() {
    return id;
  }

  public void setId(final long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Example: { id: " + id + ", name: " + name + " }";
  }

  @Override
  public boolean equals(final Object rhs) {
    if (rhs == null || !(rhs instanceof Example)) {
      return false;
    }

    final Example castedRhs = (Example) rhs;
    if (id == castedRhs.getId()) {
      if (name == null && castedRhs.getName() == null) {
        return true;
      } else if (name == null) {
        return false;
      } else if (name.equals(castedRhs.getName())) {
        return true;
      }
    }

    return false;
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder(13, 37) //@formatter:off
        .append(id)
        .append(id < 0)
        .append(name != null ? name.hashCode() : null)
        .toHashCode(); //@formatter:on
  }
}
