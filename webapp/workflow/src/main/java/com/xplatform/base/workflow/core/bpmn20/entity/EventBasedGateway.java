package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tEventBasedGateway")
public class EventBasedGateway extends Gateway
{

  @XmlAttribute
  protected Boolean instantiate;

  @XmlAttribute
  protected EventBasedGatewayType eventGatewayType;

  public boolean isInstantiate()
  {
    if (this.instantiate == null) {
      return false;
    }
    return this.instantiate.booleanValue();
  }

  public void setInstantiate(Boolean value)
  {
    this.instantiate = value;
  }

  public EventBasedGatewayType getEventGatewayType()
  {
    if (this.eventGatewayType == null) {
      return EventBasedGatewayType.EXCLUSIVE;
    }
    return this.eventGatewayType;
  }

  public void setEventGatewayType(EventBasedGatewayType value)
  {
    this.eventGatewayType = value;
  }
}