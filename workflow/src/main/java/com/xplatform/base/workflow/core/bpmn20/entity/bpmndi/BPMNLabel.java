package com.xplatform.base.workflow.core.bpmn20.entity.bpmndi;

import com.xplatform.base.workflow.core.bpmn20.entity.omgdi.Label;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BPMNLabel")
public class BPMNLabel extends Label {

	@XmlAttribute
	protected QName labelStyle;

	public QName getLabelStyle() {
		return this.labelStyle;
	}

	public void setLabelStyle(QName value) {
		this.labelStyle = value;
	}
}