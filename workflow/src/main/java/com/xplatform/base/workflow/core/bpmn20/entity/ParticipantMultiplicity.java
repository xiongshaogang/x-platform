package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tParticipantMultiplicity")
public class ParticipantMultiplicity extends BaseElement
{

  @XmlAttribute
  protected Integer minimum;

  @XmlAttribute
  protected Integer maximum;

  public int getMinimum()
  {
    if (this.minimum == null) {
      return 0;
    }
    return this.minimum.intValue();
  }

  public void setMinimum(Integer value)
  {
    this.minimum = value;
  }

  public int getMaximum()
  {
    if (this.maximum == null) {
      return 1;
    }
    return this.maximum.intValue();
  }

  public void setMaximum(Integer value)
  {
    this.maximum = value;
  }
}