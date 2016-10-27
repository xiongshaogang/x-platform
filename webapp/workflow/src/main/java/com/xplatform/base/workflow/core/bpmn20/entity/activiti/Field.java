package com.xplatform.base.workflow.core.bpmn20.entity.activiti;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="", propOrder={"string", "expression"})
@XmlRootElement(name="field")
public class Field
{
  protected String string;
  protected String expression;

  @XmlAttribute(required=true)
  protected String name;

  @XmlAttribute
  protected String stringValue;

  @XmlAttribute(name="expression")
  protected String attrExpression;

  public String getString()
  {
    return this.string;
  }

  public void setString(String value)
  {
    this.string = value;
  }

  public String getExpression()
  {
    return this.expression;
  }

  public void setExpression(String value)
  {
    this.expression = value;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String value)
  {
    this.name = value;
  }

  public String getStringValue()
  {
    return this.stringValue;
  }

  public void setStringValue(String value)
  {
    this.stringValue = value;
  }

  public String getAttrExpression()
  {
    return this.attrExpression;
  }

  public void setAttrExpression(String value)
  {
    this.attrExpression = value;
  }
}