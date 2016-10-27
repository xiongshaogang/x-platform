package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tTransaction")
public class Transaction extends SubProcess
{

  @XmlAttribute
  protected String method;

  public String getMethod()
  {
    if (this.method == null) {
      return "##Compensate";
    }
    return this.method;
  }

  public void setMethod(String value)
  {
    this.method = value;
  }
}