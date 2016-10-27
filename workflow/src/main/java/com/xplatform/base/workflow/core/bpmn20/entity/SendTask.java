package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tSendTask")
public class SendTask extends Task
{

  @XmlAttribute
  protected String implementation;

  @XmlAttribute
  protected QName messageRef;

  @XmlAttribute
  protected QName operationRef;

  public String getImplementation()
  {
    if (this.implementation == null) {
      return "##WebService";
    }
    return this.implementation;
  }

  public void setImplementation(String value)
  {
    this.implementation = value;
  }

  public QName getMessageRef()
  {
    return this.messageRef;
  }

  public void setMessageRef(QName value)
  {
    this.messageRef = value;
  }

  public QName getOperationRef()
  {
    return this.operationRef;
  }

  public void setOperationRef(QName value)
  {
    this.operationRef = value;
  }
}