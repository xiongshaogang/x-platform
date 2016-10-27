package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tConversationLink")
public class ConversationLink extends BaseElement
{

  @XmlAttribute
  protected String name;

  @XmlAttribute(required=true)
  protected QName sourceRef;

  @XmlAttribute(required=true)
  protected QName targetRef;

  public String getName()
  {
    return this.name;
  }

  public void setName(String value)
  {
    this.name = value;
  }

  public QName getSourceRef()
  {
    return this.sourceRef;
  }

  public void setSourceRef(QName value)
  {
    this.sourceRef = value;
  }

  public QName getTargetRef()
  {
    return this.targetRef;
  }

  public void setTargetRef(QName value)
  {
    this.targetRef = value;
  }
}