package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tItemDefinition")
public class ItemDefinition extends RootElement
{

  @XmlAttribute
  protected QName structureRef;

  @XmlAttribute
  protected Boolean isCollection;

  @XmlAttribute
  protected ItemKind itemKind;

  public QName getStructureRef()
  {
    return this.structureRef;
  }

  public void setStructureRef(QName value)
  {
    this.structureRef = value;
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

  public ItemKind getItemKind()
  {
    if (this.itemKind == null) {
      return ItemKind.INFORMATION;
    }
    return this.itemKind;
  }

  public void setItemKind(ItemKind value)
  {
    this.itemKind = value;
  }
}