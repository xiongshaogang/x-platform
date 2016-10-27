package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tGatewayDirection")
@XmlEnum
public enum GatewayDirection
{
  UNSPECIFIED("Unspecified"), 

  CONVERGING("Converging"), 

  DIVERGING("Diverging"), 

  MIXED("Mixed");

  private final String value;

  private GatewayDirection(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static GatewayDirection fromValue(String v) {
    for (GatewayDirection c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}