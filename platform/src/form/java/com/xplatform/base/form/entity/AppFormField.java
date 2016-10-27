package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 自定义表单字段配置表
 * 
 * @author Administrator
 *
 */
@Entity
@Table(name = "t_app_form_field", schema = "")
public class AppFormField extends OperationEntity {
	private String name;// 字段label含义
	private String code;// 字段数据库名称
	private Integer length;// 字段长度
	private Integer scale;// 小数点后几位
	private String type;// 字段数据库类型
	private String tableId;// 所属表
	private String tableName;// 所属表名
	// private String json;//构造字段的json数据
	private String parentId;// 控件的cid
	private String myCid;// 控件的cid
	//private String formId;// 表单id
	private String formCode;// 表单code
	private Integer notNull;// 是否必填(0-非必填,1-必填)
	private Integer isShow;// 是否显示列表字段
	private Integer isOverdue;// 是否过期字段（1过期，0正在使用）
	private String fieldKey; //地址控件存
	private Integer isTitle; //是否标题
	private Integer isProveEdit;//是否可审批编辑
	private Integer dateType; //时间类型，用于判断是否包含时分秒(0-date,1-datetime)
	private Integer isDB;
	private String cType;//控件类型
	private Integer isConnectionField; //是否是连接主模板的字段

	@Column(name = "name", length = 300)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "code", length = 100)
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "length", length = 10)
	public Integer getLength() {
		return length;
	}

	public void setLength(Integer length) {
		this.length = length;
	}

	@Column(name = "type", length = 30)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "tableName", length = 100)
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	@Column(name = "tableId", length = 32)
	public String getTableId() {
		return tableId;
	}

	public void setTableId(String tableId) {
		this.tableId = tableId;
	}

	@Column(name = "parentId", length = 32)
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

/*	@Column(name = "formId", length = 32)
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}*/

	@Column(name = "isShow")
	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	@Column(name = "scale")
	public Integer getScale() {
		return scale;
	}

	public void setScale(Integer scale) {
		this.scale = scale;
	}

	@Column(name = "notNull")
	public Integer getNotNull() {
		return notNull;
	}

	public void setNotNull(Integer notNull) {
		this.notNull = notNull;
	}

	@Column(name = "isOverdue")
	public Integer getIsOverdue() {
		return isOverdue;
	}

	public void setIsOverdue(Integer isOverdue) {
		this.isOverdue = isOverdue;
	}

	@Column(name = "myCid", length = 32)
	public String getMyCid() {
		return myCid;
	}

	public void setMyCid(String myCid) {
		this.myCid = myCid;
	}

	@Column(name = "fieldKey", length = 32)
	public String getFieldKey() {
		return fieldKey;
	}

	public void setFieldKey(String fieldKey) {
		this.fieldKey = fieldKey;
	}

	@Column(name = "isTitle", length = 10)
	public Integer getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(Integer isTitle) {
		this.isTitle = isTitle;
	}

	@Column(name = "formCode", length = 32)
	public String getFormCode() {
		return formCode;
	}

	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}

	@Column(name = "isProveEdit", length = 10)
	public Integer getIsProveEdit() {
		return isProveEdit;
	}

	public void setIsProveEdit(Integer isProveEdit) {
		this.isProveEdit = isProveEdit;
	}

	@Column(name = "dateType", length = 20)
	public Integer getDateType() {
		return dateType;
	}

	public void setDateType(Integer dateType) {
		this.dateType = dateType;
	}

	@Column(name = "isDB", length = 6)
	public Integer getIsDB() {
		return isDB;
	}

	public void setIsDB(Integer isDB) {
		this.isDB = isDB;
	}

	@Column(name = "cType", length = 20)
	public String getcType() {
		return cType;
	}

	public void setcType(String cType) {
		this.cType = cType;
	}

	@Column(name = "isConnectionField", length = 6)
	public Integer getIsConnectionField() {
		return isConnectionField;
	}

	public void setIsConnectionField(Integer isConnectionField) {
		this.isConnectionField = isConnectionField;
	}	
	

}
