package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tBusinessRuleTask")
public class BusinessRuleTask extends Task
{

  @XmlAttribute
  protected String implementation;

  public String getImplementation()
  {
    if (this.implementation == null) {
      return "##unspecified";
    }
    return this.implementation;
  }

  public void setImplementation(String value)
  {
    this.implementation = value;
  }
}