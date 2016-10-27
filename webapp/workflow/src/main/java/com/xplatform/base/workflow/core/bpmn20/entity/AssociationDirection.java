package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tAssociationDirection")
@XmlEnum
public enum AssociationDirection
{
  NONE("None"), 

  ONE("One"), 

  BOTH("Both");

  private final String value;

  private AssociationDirection(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static AssociationDirection fromValue(String v) {
    for (AssociationDirection c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}