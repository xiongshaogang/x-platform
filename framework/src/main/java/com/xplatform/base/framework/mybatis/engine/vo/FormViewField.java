package com.xplatform.base.framework.mybatis.engine.vo;

public class FormViewField {
	/**
     * 查询展示类型ID
     */
	private String typeId;
	/**
     * 表单项ID
     */
	private String fieldId;
	/**
     * 表单项标签
     */
	private String fieldLabel;
	/**
	 * 表单项引用字段ID
	 */
	private String columnId;
	/**
	 * 表单项引用字段NAME
	 */
	private String columnName;
	/**
	 * 表单项引用字段别名
	 */
	private String columnAlias;
	/**
	 * 表单项类型
	 */
	private String columnType;
	/**
	 * 实体类型：MAIN,TYPE,EXT,PAGE,RELATION,STATE
	 */
	private String entityType;
	/**
	 * 表单类型ID
	 */
	private String formTypeId;
	/**
	 * 是否为排序字段，0代表否，1代表是
	 */
	private String isOrderby;
	/**
	 * 排序方向：ASC,DESC
	 */
	private String direction;
	/**
	 * 非数据库字段，是否为需要删除的字段
	 */
	private String disabled;
	/**
	 * 非数据库字段，是否为修改过的字段
	 */
	private String modified;
	
	public FormViewField() {
	}
	
	public FormViewField(String columnId,String columnName, String columnAlias) {
		this.columnId = columnId;
		this.columnName = columnName;
		this.columnAlias = columnAlias;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getFieldId() {
		return fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}
	public String getFieldLabel() {
		return fieldLabel;
	}
	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}
	public String getColumnId() {
		return columnId;
	}
	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getColumnAlias() {
		return columnAlias;
	}
	public void setColumnAlias(String columnAlias) {
		this.columnAlias = columnAlias;
	}
	public String getFormTypeId() {
		return formTypeId;
	}
	public void setFormTypeId(String formTypeId) {
		this.formTypeId = formTypeId;
	}
	public String getDisabled() {
		return disabled;
	}
	public void setDisabled(String disabled) {
		this.disabled = disabled;
	}
	public String getModified() {
		return modified;
	}
	public void setModified(String modified) {
		this.modified = modified;
	}

	
	
	@Override
	public int hashCode() {
		return  columnId.hashCode();
	}
	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getIsOrderby() {
		return isOrderby;
	}

	public void setIsOrderby(String isOrderby) {
		this.isOrderby = isOrderby;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getColumnType() {
		return columnType;
	}

	public void setColumnType(String columnType) {
		this.columnType = columnType;
	}
	
	
}
