package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tBoundaryEvent")
public class BoundaryEvent extends CatchEvent
{

  @XmlAttribute
  protected Boolean cancelActivity;

  @XmlAttribute(required=true)
  protected QName attachedToRef;

  public boolean isCancelActivity()
  {
    if (this.cancelActivity == null) {
      return true;
    }
    return this.cancelActivity.booleanValue();
  }

  public void setCancelActivity(Boolean value)
  {
    this.cancelActivity = value;
  }

  public QName getAttachedToRef()
  {
    return this.attachedToRef;
  }

  public void setAttachedToRef(QName value)
  {
    this.attachedToRef = value;
  }
}