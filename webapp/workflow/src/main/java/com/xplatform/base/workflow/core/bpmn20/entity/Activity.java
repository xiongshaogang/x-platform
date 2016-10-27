package com.xplatform.base.workflow.core.bpmn20.entity;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tActivity", propOrder = { "ioSpecification", "property",
		"dataInputAssociation", "dataOutputAssociation", "resourceRole",
		"loopCharacteristics" })
@XmlSeeAlso( { SubProcess.class, Task.class, CallActivity.class })
public abstract class Activity extends FlowNode {
	protected InputOutputSpecification ioSpecification;
	protected List<Property> property;
	protected List<DataInputAssociation> dataInputAssociation;
	protected List<DataOutputAssociation> dataOutputAssociation;

	@XmlElementRef(name = "resourceRole", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", type = JAXBElement.class)
	protected List<JAXBElement<? extends ResourceRole>> resourceRole;

	@XmlElementRef(name = "loopCharacteristics", namespace = "http://www.omg.org/spec/BPMN/20100524/MODEL", type = JAXBElement.class)
	protected JAXBElement<? extends LoopCharacteristics> loopCharacteristics;

	@XmlAttribute
	protected Boolean isForCompensation;

	@XmlAttribute
	protected BigInteger startQuantity;

	@XmlAttribute
	protected BigInteger completionQuantity;

	@XmlAttribute(name = "default")
	@XmlIDREF
	@XmlSchemaType(name = "IDREF")
	protected Object _default;

	public InputOutputSpecification getIoSpecification() {
		return this.ioSpecification;
	}

	public void setIoSpecification(InputOutputSpecification value) {
		this.ioSpecification = value;
	}

	public List<Property> getProperty() {
		if (this.property == null) {
			this.property = new ArrayList();
		}
		return this.property;
	}

	public List<DataInputAssociation> getDataInputAssociation() {
		if (this.dataInputAssociation == null) {
			this.dataInputAssociation = new ArrayList();
		}
		return this.dataInputAssociation;
	}

	public List<DataOutputAssociation> getDataOutputAssociation() {
		if (this.dataOutputAssociation == null) {
			this.dataOutputAssociation = new ArrayList();
		}
		return this.dataOutputAssociation;
	}

	public List<JAXBElement<? extends ResourceRole>> getResourceRole() {
		if (this.resourceRole == null) {
			this.resourceRole = new ArrayList();
		}
		return this.resourceRole;
	}

	public JAXBElement<? extends LoopCharacteristics> getLoopCharacteristics() {
		return this.loopCharacteristics;
	}

	public void setLoopCharacteristics(
			JAXBElement<? extends LoopCharacteristics> value) {
		this.loopCharacteristics = value;
	}

	public boolean isIsForCompensation() {
		if (this.isForCompensation == null) {
			return false;
		}
		return this.isForCompensation.booleanValue();
	}

	public void setIsForCompensation(Boolean value) {
		this.isForCompensation = value;
	}

	public BigInteger getStartQuantity() {
		if (this.startQuantity == null) {
			return new BigInteger("1");
		}
		return this.startQuantity;
	}

	public void setStartQuantity(BigInteger value) {
		this.startQuantity = value;
	}

	public BigInteger getCompletionQuantity() {
		if (this.completionQuantity == null) {
			return new BigInteger("1");
		}
		return this.completionQuantity;
	}

	public void setCompletionQuantity(BigInteger value) {
		this.completionQuantity = value;
	}

	public Object getDefault() {
		return this._default;
	}

	public void setDefault(Object value) {
		this._default = value;
	}
}