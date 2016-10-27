package com.xplatform.base.workflow.core.bpmn20.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tChoreographyActivity", propOrder={"participantRef", "correlationKey"})
@XmlSeeAlso({SubChoreography.class, ChoreographyTask.class, CallChoreography.class})
public abstract class ChoreographyActivity extends FlowNode
{

  @XmlElement(required=true)
  protected List<QName> participantRef;
  protected List<CorrelationKey> correlationKey;

  @XmlAttribute(required=true)
  protected QName initiatingParticipantRef;

  @XmlAttribute
  protected ChoreographyLoopType loopType;

  public List<QName> getParticipantRef()
  {
    if (this.participantRef == null) {
      this.participantRef = new ArrayList();
    }
    return this.participantRef;
  }

  public List<CorrelationKey> getCorrelationKey()
  {
    if (this.correlationKey == null) {
      this.correlationKey = new ArrayList();
    }
    return this.correlationKey;
  }

  public QName getInitiatingParticipantRef()
  {
    return this.initiatingParticipantRef;
  }

  public void setInitiatingParticipantRef(QName value)
  {
    this.initiatingParticipantRef = value;
  }

  public ChoreographyLoopType getLoopType()
  {
    if (this.loopType == null) {
      return ChoreographyLoopType.NONE;
    }
    return this.loopType;
  }

  public void setLoopType(ChoreographyLoopType value)
  {
    this.loopType = value;
  }
}