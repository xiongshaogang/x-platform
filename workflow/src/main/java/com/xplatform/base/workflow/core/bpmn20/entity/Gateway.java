package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tGateway")
@XmlSeeAlso({ComplexGateway.class, InclusiveGateway.class, EventBasedGateway.class, ParallelGateway.class, ExclusiveGateway.class})
public class Gateway extends FlowNode
{

  @XmlAttribute
  protected GatewayDirection gatewayDirection;

  public GatewayDirection getGatewayDirection()
  {
    if (this.gatewayDirection == null) {
      return GatewayDirection.UNSPECIFIED;
    }
    return this.gatewayDirection;
  }

  public void setGatewayDirection(GatewayDirection value)
  {
    this.gatewayDirection = value;
  }
}