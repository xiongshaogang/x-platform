package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tRelationshipDirection")
@XmlEnum
public enum RelationshipDirection
{
  NONE("None"), 

  FORWARD("Forward"), 

  BACKWARD("Backward"), 

  BOTH("Both");

  private final String value;

  private RelationshipDirection(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static RelationshipDirection fromValue(String v) {
    for (RelationshipDirection c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}