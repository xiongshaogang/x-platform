package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tComplexGateway", propOrder={"activationCondition"})
public class ComplexGateway extends Gateway
{
  protected Expression activationCondition;

  @XmlAttribute(name="default")
  @XmlIDREF
  @XmlSchemaType(name="IDREF")
  protected Object _default;

  public Expression getActivationCondition()
  {
    return this.activationCondition;
  }

  public void setActivationCondition(Expression value)
  {
    this.activationCondition = value;
  }

  public Object getDefault()
  {
    return this._default;
  }

  public void setDefault(Object value)
  {
    this._default = value;
  }
}