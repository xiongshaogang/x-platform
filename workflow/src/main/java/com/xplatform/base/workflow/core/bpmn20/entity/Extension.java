package com.xplatform.base.workflow.core.bpmn20.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tExtension", propOrder={"documentation"})
public class Extension
{
  protected List<Documentation> documentation;

  @XmlAttribute
  protected QName definition;

  @XmlAttribute
  protected Boolean mustUnderstand;

  public List<Documentation> getDocumentation()
  {
    if (this.documentation == null) {
      this.documentation = new ArrayList();
    }
    return this.documentation;
  }

  public QName getDefinition()
  {
    return this.definition;
  }

  public void setDefinition(QName value)
  {
    this.definition = value;
  }

  public boolean isMustUnderstand()
  {
    if (this.mustUnderstand == null) {
      return false;
    }
    return this.mustUnderstand.booleanValue();
  }

  public void setMustUnderstand(Boolean value)
  {
    this.mustUnderstand = value;
  }
}