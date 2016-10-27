package com.xplatform.base.develop.codeconfig.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.lang.String;
import java.lang.Double;
import java.lang.Integer;
import java.math.BigDecimal;

import javax.xml.soap.Text;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.xplatform.base.framework.core.common.entity.IdEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

import javax.persistence.SequenceGenerator;

/**
 * @Title: Entity
 * @Description: 生成页面模型记录主表
 * @author xiaqiang
 * @date 2014-05-13 20:09:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_dev_generate")
@SuppressWarnings("serial")
public class GenerateEntity extends OperationEntity implements
		java.io.Serializable {
	/** 模型名称 */
	private String name;
	/** 表单code */
	private String code;
	/** 所属模版类型名称 */
	private String typeName;
	/** 所属模版类型ID */
	private String typeID;
	/** 描述 */
	private String description;

	public GenerateEntity() {
	}

	@Column(name = "typeName", nullable = true, length = 100)
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = true, length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "typeID", nullable = true, length = 100)
	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	@Column(name = "description", nullable = true, length = 4000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
