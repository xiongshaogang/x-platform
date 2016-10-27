package com.xplatform.base.workflow.core.bpmn20.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name="tLoopCharacteristics")
@XmlSeeAlso({StandardLoopCharacteristics.class, MultiInstanceLoopCharacteristics.class})
public abstract class LoopCharacteristics extends BaseElement
{
}