package com.xplatform.base.system.dict.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;

/**
 * 
 * description :数据字典值实体
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年6月10日 上午8:52:04
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年6月10日 上午8:52:04
 *
 */
@Entity
@Table(name = "t_sys_dict_value", schema = "")
public class DictValueEntity extends BaseTreeEntity {

	private String name; //字典值名称
	private String code; //字典值code
	private String value; //字典值
	private String typeValue; //字典类型值
	private String typeCode; //字典类型code
	private Integer orderby; //排序号
	private String description; //描述
	private String extend1; //扩展字段1
	private String extend2; //扩展字段2
	private String extend3; //扩展字段3

	private DictTypeEntity dictType;

	private DictValueEntity parent; // 父节点
	private List<DictValueEntity> children = new ArrayList<DictValueEntity>(); //子节点

	@Column(name = "name", nullable = true, length = 100)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", nullable = true, length = 50)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "value", nullable = true, length = 50)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "orderby", nullable = true, length = 8)
	public Integer getOrderby() {
		return orderby;
	}

	public void setOrderby(Integer orderby) {
		this.orderby = orderby;
	}

	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "typeValue", nullable = true, length = 50)
	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	@Column(name = "typeCode", nullable = true, length = 50)
	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "typeId")
	public DictTypeEntity getDictType() {
		return dictType;
	}

	public void setDictType(DictTypeEntity dictType) {
		this.dictType = dictType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@NotFound(action = NotFoundAction.IGNORE)
	@JoinColumn(name = "parentId")
	public DictValueEntity getParent() {
		return parent;
	}

	public void setParent(DictValueEntity parent) {
		this.parent = parent;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
	public List<DictValueEntity> getChildren() {
		return children;
	}

	public void setChildren(List<DictValueEntity> children) {
		this.children = children;
	}
	@Column(name = "extend1", nullable = true, length = 4000)
	public String getExtend1() {
		return extend1;
	}

	public void setExtend1(String extend1) {
		this.extend1 = extend1;
	}
	@Column(name = "extend2", nullable = true, length = 4000)
	public String getExtend2() {
		return extend2;
	}

	public void setExtend2(String extend2) {
		this.extend2 = extend2;
	}
	
	@Column(name = "extend3", nullable = true, length = 4000)
	public String getExtend3() {
		return extend3;
	}
	public void setExtend3(String extend3) {
		this.extend3 = extend3;
	}
	
	

}
