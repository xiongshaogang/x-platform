package com.xplatform.base.develop.codeconfig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.IdEntity;


/**
 * @Title: Entity
 * @Description: 页面模型与实体类数据联系表
 * @author by
 * @date 2014-05-14 15:10:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_base_con_form")
@SuppressWarnings("serial")
public class ConBaseFormEntity extends IdEntity implements java.io.Serializable {
	/** 模板实体数据类型名 */
	private String name;
	/** 模板实体数据类型 */
	private String type;
	/** 父节点ID */
	private String parent_id;
	/** 树层级 */
	private Integer level;
	/** 是否叶子节点 */
	private Integer isleaf;
	/** 模板类型ID */
	private String base_model_id;
	/**表类型：主表，单表，附表 */
	private String table_type;
	
	public ConBaseFormEntity() {
	}
	
	
	@Column(name = "NAME", nullable = false, length = 30)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "TYPE")
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "PARENT_ID")
	public String getParent_id() {
		return parent_id;
	}

	public void setParent_id(String parent_id) {
		this.parent_id = parent_id;
	}

	@Column(name = "LEVEL")
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Column(name = "ISLEAF")
	public Integer getIsleaf() {
		return isleaf;
	}

	public void setIsleaf(Integer isleaf) {
		this.isleaf = isleaf;
	}


	@Column(name = "BASE_MODEL_ID")
	public String getBase_model_id() {
		return base_model_id;
	}


	public void setBase_model_id(String base_model_id) {
		this.base_model_id = base_model_id;
	}

	@Column(name = "TABLE_TYPE")
	public String getTable_type() {
		return table_type;
	}


	public void setTable_type(String table_type) {
		this.table_type = table_type;
	}
	



}
