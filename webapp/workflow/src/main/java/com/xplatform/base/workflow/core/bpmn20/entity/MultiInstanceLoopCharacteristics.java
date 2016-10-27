package com.xplatform.base.workflow.core.bpmn20.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tMultiInstanceLoopCharacteristics", propOrder={"loopCardinality", "loopDataInputRef", "loopDataOutputRef", "inputDataItem", "outputDataItem", "complexBehaviorDefinition", "completionCondition"})
public class MultiInstanceLoopCharacteristics extends LoopCharacteristics
{
  protected Expression loopCardinality;
  protected QName loopDataInputRef;
  protected QName loopDataOutputRef;
  protected DataInput inputDataItem;
  protected DataOutput outputDataItem;
  protected List<ComplexBehaviorDefinition> complexBehaviorDefinition;
  protected Expression completionCondition;

  @XmlAttribute
  protected Boolean isSequential;

  @XmlAttribute
  protected MultiInstanceFlowCondition behavior;

  @XmlAttribute
  protected QName oneBehaviorEventRef;

  @XmlAttribute
  protected QName noneBehaviorEventRef;

  public Expression getLoopCardinality()
  {
    return this.loopCardinality;
  }

  public void setLoopCardinality(Expression value)
  {
    this.loopCardinality = value;
  }

  public QName getLoopDataInputRef()
  {
    return this.loopDataInputRef;
  }

  public void setLoopDataInputRef(QName value)
  {
    this.loopDataInputRef = value;
  }

  public QName getLoopDataOutputRef()
  {
    return this.loopDataOutputRef;
  }

  public void setLoopDataOutputRef(QName value)
  {
    this.loopDataOutputRef = value;
  }

  public DataInput getInputDataItem()
  {
    return this.inputDataItem;
  }

  public void setInputDataItem(DataInput value)
  {
    this.inputDataItem = value;
  }

  public DataOutput getOutputDataItem()
  {
    return this.outputDataItem;
  }

  public void setOutputDataItem(DataOutput value)
  {
    this.outputDataItem = value;
  }

  public List<ComplexBehaviorDefinition> getComplexBehaviorDefinition()
  {
    if (this.complexBehaviorDefinition == null) {
      this.complexBehaviorDefinition = new ArrayList();
    }
    return this.complexBehaviorDefinition;
  }

  public Expression getCompletionCondition()
  {
    return this.completionCondition;
  }

  public void setCompletionCondition(Expression value)
  {
    this.completionCondition = value;
  }

  public boolean isIsSequential()
  {
    if (this.isSequential == null) {
      return false;
    }
    return this.isSequential.booleanValue();
  }

  public void setIsSequential(Boolean value)
  {
    this.isSequential = value;
  }

  public MultiInstanceFlowCondition getBehavior()
  {
    if (this.behavior == null) {
      return MultiInstanceFlowCondition.ALL;
    }
    return this.behavior;
  }

  public void setBehavior(MultiInstanceFlowCondition value)
  {
    this.behavior = value;
  }

  public QName getOneBehaviorEventRef()
  {
    return this.oneBehaviorEventRef;
  }

  public void setOneBehaviorEventRef(QName value)
  {
    this.oneBehaviorEventRef = value;
  }

  public QName getNoneBehaviorEventRef()
  {
    return this.noneBehaviorEventRef;
  }

  public void setNoneBehaviorEventRef(QName value)
  {
    this.noneBehaviorEventRef = value;
  }
}