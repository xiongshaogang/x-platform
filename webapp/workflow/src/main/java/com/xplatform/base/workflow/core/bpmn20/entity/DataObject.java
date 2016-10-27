package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tDataObject", propOrder={"dataState"})
public class DataObject extends FlowElement
{
  protected DataState dataState;

  @XmlAttribute
  protected QName itemSubjectRef;

  @XmlAttribute
  protected Boolean isCollection;

  public DataState getDataState()
  {
    return this.dataState;
  }

  public void setDataState(DataState value)
  {
    this.dataState = value;
  }

  public QName getItemSubjectRef()
  {
    return this.itemSubjectRef;
  }

  public void setItemSubjectRef(QName value)
  {
    this.itemSubjectRef = value;
  }

  public boolean isIsCollection()
  {
    if (this.isCollection == null) {
      return false;
    }
    return this.isCollection.booleanValue();
  }

  public void setIsCollection(Boolean value)
  {
    this.isCollection = value;
  }
}