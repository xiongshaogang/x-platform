package com.xplatform.base.workflow.core.bpmn20.entity.omgdi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DiagramElement", propOrder = { "extension" })
@XmlSeeAlso({ Node.class, Edge.class })
public abstract class DiagramElement {
	protected Extension extension;

	@XmlAttribute
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;

	@XmlAnyAttribute
	private Map<QName, String> otherAttributes;

	public DiagramElement() {
		this.otherAttributes = new HashMap();
	}

	public Extension getExtension() {
		return this.extension;
	}

	public void setExtension(Extension value) {
		this.extension = value;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String value) {
		this.id = value;
	}

	public Map<QName, String> getOtherAttributes() {
		return this.otherAttributes;
	}

	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "", propOrder = { "any" })
	public static class Extension {

		@XmlAnyElement(lax = true)
		protected List<Object> any;

		public List<Object> getAny() {
			if (this.any == null) {
				this.any = new ArrayList();
			}
			return this.any;
		}
	}
}