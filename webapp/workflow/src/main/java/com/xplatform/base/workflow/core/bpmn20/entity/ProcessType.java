package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tProcessType")
@XmlEnum
public enum ProcessType
{
  NONE("None"), 

  PUBLIC("Public"), 

  PRIVATE("Private");

  private final String value;

  private ProcessType(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static ProcessType fromValue(String v) {
    for (ProcessType c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}