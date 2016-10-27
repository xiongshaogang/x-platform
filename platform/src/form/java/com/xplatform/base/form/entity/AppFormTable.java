package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

@Entity
@Table(name = "t_app_form_table", schema = "")
public class AppFormTable extends OperationEntity {
	private String tableName;// 表名
	// private String formId;// 表单模版id
	private String cId;// 明细控件的cId
	private Integer tableType; // 表格类型(1-单表,2-主表,3-从表)
	private String mainTable; // 主表名
	private String subTables;// 子表表名字符串(存最新，不包含删除从表)
	private Integer isOverdue;// 是否过期字段（0过期，1正在使用）
	private String formCode;// 表单code
	private Integer isProveEdit;// 是否可审批编辑

	@Column(name = "tableName", length = 100)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/*
	 * @Column(name = "formId", length = 32) public String getFormId() { return
	 * formId; }
	 * 
	 * public void setFormId(String formId) { this.formId = formId; }
	 */

	@Column(name = "tableType")
	public Integer getTableType() {
		return tableType;
	}

	public void setTableType(Integer tableType) {
		this.tableType = tableType;
	}

	@Column(name = "subTables", columnDefinition = "VARCHAR(500)")
	public String getSubTables() {
		return subTables;
	}

	public void setSubTables(String subTables) {
		this.subTables = subTables;
	}

	@Column(name = "mainTable", length = 32)
	public String getMainTable() {
		return mainTable;
	}

	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}

	@Column(name = "isOverdue")
	public Integer getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(Integer isOverdue) {
		this.isOverdue = isOverdue;
	}

	@Column(name = "cId", length = 32)
	public String getCId() {
		return cId;
	}

	public void setCId(String cId) {
		this.cId = cId;
	}

	@Column(name = "formCode", length = 32)
	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	@Column(name = "isProveEdit")
	public Integer getIsProveEdit() {
		return isProveEdit;
	}

	public void setIsProveEdit(Integer isProveEdit) {
		this.isProveEdit = isProveEdit;
	}

}
