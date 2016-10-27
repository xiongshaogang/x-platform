package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tComplexBehaviorDefinition", propOrder={"condition", "event"})
public class ComplexBehaviorDefinition extends BaseElement
{

  @XmlElement(required=true)
  protected FormalExpression condition;
  protected ImplicitThrowEvent event;

  public FormalExpression getCondition()
  {
    return this.condition;
  }

  public void setCondition(FormalExpression value)
  {
    this.condition = value;
  }

  public ImplicitThrowEvent getEvent()
  {
    return this.event;
  }

  public void setEvent(ImplicitThrowEvent value)
  {
    this.event = value;
  }
}