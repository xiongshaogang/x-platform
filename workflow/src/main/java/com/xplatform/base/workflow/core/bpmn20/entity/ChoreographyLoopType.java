package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tChoreographyLoopType")
@XmlEnum
public enum ChoreographyLoopType
{
  NONE("None"), 

  STANDARD("Standard"), 

  MULTI_INSTANCE_SEQUENTIAL("MultiInstanceSequential"), 

  MULTI_INSTANCE_PARALLEL("MultiInstanceParallel");

  private final String value;

  private ChoreographyLoopType(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static ChoreographyLoopType fromValue(String v) {
    for (ChoreographyLoopType c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}