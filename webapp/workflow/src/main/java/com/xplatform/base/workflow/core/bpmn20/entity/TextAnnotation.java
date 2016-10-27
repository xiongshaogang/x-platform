package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tTextAnnotation", propOrder={"text"})
public class TextAnnotation extends Artifact
{
  protected Text text;

  @XmlAttribute
  protected String textFormat;

  public Text getText()
  {
    return this.text;
  }

  public void setText(Text value)
  {
    this.text = value;
  }

  public String getTextFormat()
  {
    if (this.textFormat == null) {
      return "text/plain";
    }
    return this.textFormat;
  }

  public void setTextFormat(String value)
  {
    this.textFormat = value;
  }
}