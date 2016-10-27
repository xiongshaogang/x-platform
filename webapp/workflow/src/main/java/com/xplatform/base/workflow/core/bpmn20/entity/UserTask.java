package com.xplatform.base.workflow.core.bpmn20.entity;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tUserTask", propOrder = { "rendering" })
public class UserTask extends Task {
	protected List<Rendering> rendering;

	@XmlAttribute
	protected String implementation;

	public List<Rendering> getRendering() {
		if (this.rendering == null) {
			this.rendering = new ArrayList();
		}
		return this.rendering;
	}

	public String getImplementation() {
		if (this.implementation == null) {
			return "##unspecified";
		}
		return this.implementation;
	}

	public void setImplementation(String value) {
		this.implementation = value;
	}
}