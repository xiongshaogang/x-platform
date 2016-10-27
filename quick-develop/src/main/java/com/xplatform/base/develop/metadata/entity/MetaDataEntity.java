package com.xplatform.base.develop.metadata.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * @Title: Entity
 * @Description:   代码生成器所用表  源数据管理
 * @author 宾勇
 * @date 2014-05-29 
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_dev_metadata")
@SuppressWarnings("serial")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "columns" })
public class MetaDataEntity extends OperationEntity implements
		java.io.Serializable {
	/** 表名*/
	private String tableName;
	/** 表描述 */
	private String content;
	/** 是否同步 */
	private String isDbsynch;
	/**  */
	private String jformPkSequence;
	/** 主键策略 */
	private String jformPkType;
	/** 表类型 */
	private String jformType;
	/** 版本 */
	private Integer jformVersion;
	/** 子表 */
	private String subTableStr;
	
	private String typeId;//分类id
	
	private String typeName;//分类名称
	
	private List<MetaDataFieldEntity> columns;
	
	@Column(name = "table_name", nullable = false, length = 64)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	@Column(name = "content", nullable = true, length = 200)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	@Column(name = "is_dbsynch", nullable = true, length = 5)
	public String getIsDbsynch() {
		return isDbsynch;
	}
	public void setIsDbsynch(String isDbsynch) {
		this.isDbsynch = isDbsynch;
	}
	@Column(name = "jform_pk_sequence", nullable = true, length = 200)
	public String getJformPkSequence() {
		return jformPkSequence;
	}
	public void setJformPkSequence(String jformPkSequence) {
		this.jformPkSequence = jformPkSequence;
	}
	@Column(name = "jform_pk_type", nullable = true, length = 20)
	public String getJformPkType() {
		return jformPkType;
	}
	public void setJformPkType(String jformPkType) {
		this.jformPkType = jformPkType;
	}
	@Column(name = "jform_type", nullable = true, length = 1)
	public String getJformType() {
		return jformType;
	}
	public void setJformType(String jformType) {
		this.jformType = jformType;
	}
	@Column(name = "jform_version", nullable = true, length = 20)
	public Integer getJformVersion() {
		return jformVersion;
	}
	public void setJformVersion(Integer jformVersion) {
		this.jformVersion = jformVersion;
	}
	@Column(name = "sub_table_str", nullable = true, length = 100)
	public String getSubTableStr() {
		return subTableStr;
	}
	public void setSubTableStr(String subTableStr) {
		this.subTableStr = subTableStr;
	}
	
	
	@OneToMany(cascade=CascadeType.REMOVE,mappedBy="table")
	@OrderBy(clause="orderNum asc")
	public List<MetaDataFieldEntity> getColumns() {
		return columns;
	}

	public void setColumns(List<MetaDataFieldEntity> columns) {
		this.columns = columns;
	}
	
	@Column(name = "typeId", nullable = true, length = 32)
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	@Column(name = "typeName", nullable = true, length = 100)
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	


}
