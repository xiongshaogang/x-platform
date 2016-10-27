package com.xplatform.base.develop.metadata.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * @Title: Entity
 * @Description: 代码生成器所用表 所属字段 源数据管理
 * @author 宾勇
 * @date 2014-05-29
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_dev_metadata_field")
@DynamicUpdate(true)
@DynamicInsert(true)
@JsonAutoDetect
@SuppressWarnings("serial")
public class MetaDataFieldEntity extends OperationEntity implements Serializable {

	/** 表名 */
	private MetaDataEntity table;

	/** 字段名称 */
	private String fieldName;

	/** 修改前字段名称 */
	private String oldFieldName;

	/** 字段描述 */
	private String content;

	/** 字段长度 */
	private Integer length;

	/** 小数点长度 */
	private Integer pointLength;

	/** 字段默认值 */
	private String fieldDefault;

	/** 字段类型 */
	private String type;

	/** 字段是否主键 */
	private String isKey;

	/** 字段是否可为空 */
	private String isNull;

	/** 主表ID */
	private String mainField;

	/** 主表name */
	private String mainTable;

	/** 字段序号 */
	private Integer orderNum;

	/**
	 * 方法: 取得TablePropertyEntity
	 *
	 * @return: TablePropertyEntity 关联的表ＩＤ
	 */
	@ManyToOne
	@JoinColumn(name = "table_id", nullable = false, referencedColumnName = "id")
	@JsonIgnore
	@ForeignKey(name = "null")
	public MetaDataEntity getTable() {
		return this.table;
	}

	/**
	 * 方法: 设置TablePropertyEntity
	 *
	 * @param: TablePropertyEntity 关联的表ID
	 */
	public void setTable(MetaDataEntity table) {
		this.table = table;
	}

	@Column(name = "field_name", nullable = false, length = 64)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "old_field_name", nullable = true, length = 64)
	public String getOldFieldName() {
		return oldFieldName;
	}

	public void setOldFieldName(String oldFieldName) {
		this.oldFieldName = oldFieldName;
	}

	@Column(name = "content", nullable = true, length = 200)
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name = "length", nullable = false, length = 11)
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "point_length", nullable = true, length = 11)
	public Integer getPointLength() {
		return pointLength;
	}

	public void setPointLength(Integer pointLength) {
		this.pointLength = pointLength;
	}

	@Column(name = "field_default", nullable = true, length = 20)
	public String getFieldDefault() {
		return fieldDefault;
	}

	public void setFieldDefault(String fieldDefault) {
		this.fieldDefault = fieldDefault;
	}

	@Column(name = "type", nullable = false, length = 32)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "is_key", nullable = true, length = 2)
	public String getIsKey() {
		return isKey;
	}

	public void setIsKey(String isKey) {
		this.isKey = isKey;
	}

	@Column(name = "is_null", nullable = true, length = 5)
	public String getIsNull() {
		return isNull;
	}

	public void setIsNull(String isNull) {
		this.isNull = isNull;
	}

	@Column(name = "main_field", nullable = true, length = 64)
	public String getMainField() {
		return mainField;
	}

	public void setMainField(String mainField) {
		this.mainField = mainField;
	}

	@Column(name = "main_table", nullable = true, length = 64)
	public String getMainTable() {
		return mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	@Column(name = "order_num", nullable = true, length = 11)
	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

}
