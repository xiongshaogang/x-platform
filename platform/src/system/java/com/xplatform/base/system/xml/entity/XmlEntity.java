package com.xplatform.base.system.xml.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.IdEntity;



@Entity
@Table(name = "t_sys_xml_test", schema = "")
@SuppressWarnings("serial")
public class XmlEntity extends IdEntity implements java.io.Serializable {

	private String xmldata;//xml

	@Column(name ="xmldata",nullable=false)
	public String getXmldata() {
		return xmldata;
	}

	public void setXmldata(String xmldata) {
		this.xmldata = xmldata;
	}
	
	
}
