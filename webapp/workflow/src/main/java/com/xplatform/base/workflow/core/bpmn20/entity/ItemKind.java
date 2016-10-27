package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;

@XmlType(name="tItemKind")
@XmlEnum
public enum ItemKind
{
  INFORMATION("Information"), 

  PHYSICAL("Physical");

  private final String value;

  private ItemKind(String v) { this.value = v; }

  public String value()
  {
    return this.value;
  }

  public static ItemKind fromValue(String v) {
    for (ItemKind c : values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }
}