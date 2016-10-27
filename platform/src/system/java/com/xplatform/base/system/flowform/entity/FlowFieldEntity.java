package com.xplatform.base.system.flowform.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * @Title: Entity
 * @Description:   流程表单表字段
 * @author 宾勇
 * @date 2015-01-04 
 * @version V1.0
 *
 */
@Entity
@Table(name="t_flow_field")
@SuppressWarnings("serial")
public class FlowFieldEntity extends OperationEntity{

	private FlowTableEntity table;
	private String fieldName;//字段名称
	private String oldFieldName;//修改前字段名
	private String fieldDes;//字段描述
	private String type;//类型
	private Integer length;//长度
	private Integer pointLength;//小数点长度
	private String isrequired;//是否必填
	private String islist;//是否显示到列表
	private String isquery;//是否查询
	private String ishidden;//是否隐藏
	private String isflowvar;//是否流程变量
	private String valueFrom;//值来源
	private String controlType;//控件类型
	private String verifyType;//验证规则
	private String dictCode;//数据字典
	private String jscript;//脚本
	private String idScript;//ID脚本
	private String serialNumber;//流水号
	private String options;//选项
	
	@ManyToOne
	@JoinColumn(name ="table_id",nullable=false,referencedColumnName="id")
	@JsonIgnore
	@ForeignKey(name="null")
	public FlowTableEntity getTable() {
		return table;
	}
	public void setTable(FlowTableEntity table) {
		this.table = table;
	}
	
	@Column(name = "field_name", nullable = false, length = 64)
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	@Column(name = "field_des", nullable = true, length = 200)
	public String getFieldDes() {
		return fieldDes;
	}
	public void setFieldDes(String fieldDes) {
		this.fieldDes = fieldDes;
	}
	
	@Column(name = "type", nullable = true, length = 20)
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Column(name = "length", nullable = true, length = 20)
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	
	@Column(name = "options", nullable = true, length = 50)
	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		this.options = options;
	}
	
	@Column(name = "value_from", nullable = true, length = 20)
	public String getValueFrom() {
		return valueFrom;
	}
	public void setValueFrom(String valueFrom) {
		this.valueFrom = valueFrom;
	}
	
	@Column(name = "control_type", nullable = true, length = 50)
	public String getControlType() {
		return controlType;
	}
	public void setControlType(String controlType) {
		this.controlType = controlType;
	}
	
	@Column(name = "verify_type", nullable = true, length = 50)
	public String getVerifyType() {
		return verifyType;
	}
	public void setVerifyType(String verifyType) {
		this.verifyType = verifyType;
	}
	
	@Column(name = "dict_code", nullable = true, length = 50)
	public String getDictCode() {
		return dictCode;
	}
	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}
	
	@Column(name = "jscript", nullable = true, length = 200)
	public String getJscript() {
		return jscript;
	}
	public void setJscript(String jscript) {
		this.jscript = jscript;
	}
	
	@Column(name = "id_script", nullable = true, length = 200)
	public String getIdScript() {
		return idScript;
	}
	public void setIdScript(String idScript) {
		this.idScript = idScript;
	}
	
	@Column(name = "serial_number", nullable = true, length = 50)
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	
	@Column(name = "is_required", nullable = true, length = 5)
	public String getIsrequired() {
		return isrequired;
	}
	public void setIsrequired(String isrequired) {
		this.isrequired = isrequired;
	}
	
	@Column(name = "is_list", nullable = true, length = 5)
	public String getIslist() {
		return islist;
	}
	public void setIslist(String islist) {
		this.islist = islist;
	}
	
	@Column(name = "is_query", nullable = true, length = 5)
	public String getIsquery() {
		return isquery;
	}
	public void setIsquery(String isquery) {
		this.isquery = isquery;
	}
	
	@Column(name = "is_hidden", nullable = true, length = 5)
	public String getIshidden() {
		return ishidden;
	}
	public void setIshidden(String ishidden) {
		this.ishidden = ishidden;
	}
	
	@Column(name = "is_flowvar", nullable = true, length = 5)
	public String getIsflowvar() {
		return isflowvar;
	}
	public void setIsflowvar(String isflowvar) {
		this.isflowvar = isflowvar;
	}
	
	@Column(name = "point_length", nullable = true, length = 15)
	public Integer getPointLength() {
		return pointLength;
	}
	public void setPointLength(Integer pointLength) {
		this.pointLength = pointLength;
	}
	
	@Column(name = "old_field_name", nullable = true, length = 15)
	public String getOldFieldName() {
		return oldFieldName;
	}
	public void setOldFieldName(String oldFieldName) {
		this.oldFieldName = oldFieldName;
	}
	
	
}
