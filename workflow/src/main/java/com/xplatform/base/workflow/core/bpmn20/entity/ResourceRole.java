package com.xplatform.base.workflow.core.bpmn20.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tResourceRole", propOrder={"resourceRef", "resourceParameterBinding", "resourceAssignmentExpression"})
@XmlSeeAlso({Performer.class})
public class ResourceRole extends BaseElement
{
  protected QName resourceRef;
  protected List<ResourceParameterBinding> resourceParameterBinding;
  protected ResourceAssignmentExpression resourceAssignmentExpression;

  @XmlAttribute
  protected String name;

  public QName getResourceRef()
  {
    return this.resourceRef;
  }

  public void setResourceRef(QName value)
  {
    this.resourceRef = value;
  }

  public List<ResourceParameterBinding> getResourceParameterBinding()
  {
    if (this.resourceParameterBinding == null) {
      this.resourceParameterBinding = new ArrayList();
    }
    return this.resourceParameterBinding;
  }

  public ResourceAssignmentExpression getResourceAssignmentExpression()
  {
    return this.resourceAssignmentExpression;
  }

  public void setResourceAssignmentExpression(ResourceAssignmentExpression value)
  {
    this.resourceAssignmentExpression = value;
  }

  public String getName()
  {
    return this.name;
  }

  public void setName(String value)
  {
    this.name = value;
  }
}