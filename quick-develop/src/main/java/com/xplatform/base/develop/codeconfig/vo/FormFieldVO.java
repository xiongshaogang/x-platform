package com.xplatform.base.develop.codeconfig.vo;

import java.util.Date;

/**
 * @Title: VO
 * @Description: 页面实体类字段配置页面临时对象
 * @author xiaqiang
 * @date 2014-05-17 20:09:32
 * @version V1.0
 *
 */
public class FormFieldVO implements java.io.Serializable {
	private String id;
	/** 创建时间 **/
	private Date createTime;
	/** 创建人Id **/
	private String createUserId;
	/** 创建人名称 **/
	private String createUserName;
	/** 上一次更新时间 **/
	private Date updateTime;
	/** 上一次更新人Id **/
	private String updateUserId;
	/** 上一次更新人名称 **/
	private String updateUserName;

	/** 预设验证实体类(Id) */
	private String formRuleEntityId;
	/** 预设验证实体类(名称) */
	private String formRuleEntityName;
	/** 所配置的实体类(Id) */
	private String formEntityId;
	/** 所配置的字段对应的实体字段表对象(Id) */
	private String fieldEntityId;
	/** 字段名称 */
	private String fieldName;
	/** 字段显示名 */
	private String fieldLabel;
	/** 字段类型 */
	private String fieldType;
	/** 字段长度 */
	private Integer fieldLength;
	/** 字段精度 */
	private Integer fieldPersion;
	/** 字段控件类型 */
	private String showType;
	/** 字段样式 */
	private String style;
	/** 字段描述 */
	private String description;
	/** 表格是否显示 */
	private String listShow;
	/** 查询是否显示 */
	private String queryShow;
	/** 编辑是否显示 */
	private String editShow;
	/** 是否只读 */
	private String isReadonly;
	/** 是否唯一 */
	private String isUnique;
	/** 自定义校验规则 */
	private String reCheckRule;
	/** 数据字典 */
	private String dict;
	/** 远程数据url */
	private String dataUrl;

	public FormFieldVO() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getCreateUserName() {
		return createUserName;
	}

	public void setCreateUserName(String createUserName) {
		this.createUserName = createUserName;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public String getUpdateUserName() {
		return updateUserName;
	}

	public void setUpdateUserName(String updateUserName) {
		this.updateUserName = updateUserName;
	}

	public String getFormRuleEntityId() {
		return formRuleEntityId;
	}

	public void setFormRuleEntityId(String formRuleEntityId) {
		this.formRuleEntityId = formRuleEntityId;
	}

	public String getFormRuleEntityName() {
		return formRuleEntityName;
	}

	public void setFormRuleEntityName(String formRuleEntityName) {
		this.formRuleEntityName = formRuleEntityName;
	}

	public String getFormEntityId() {
		return formEntityId;
	}

	public void setFormEntityId(String formEntityId) {
		this.formEntityId = formEntityId;
	}

	public String getFieldEntityId() {
		return fieldEntityId;
	}

	public void setFieldEntityId(String fieldEntityId) {
		this.fieldEntityId = fieldEntityId;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	public Integer getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}

	public Integer getFieldPersion() {
		return fieldPersion;
	}

	public void setFieldPersion(Integer fieldPersion) {
		this.fieldPersion = fieldPersion;
	}

	public String getShowType() {
		return showType;
	}

	public void setShowType(String showType) {
		this.showType = showType;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getListShow() {
		return listShow;
	}

	public void setListShow(String listShow) {
		this.listShow = listShow;
	}

	public String getQueryShow() {
		return queryShow;
	}

	public void setQueryShow(String queryShow) {
		this.queryShow = queryShow;
	}

	public String getEditShow() {
		return editShow;
	}

	public void setEditShow(String editShow) {
		this.editShow = editShow;
	}

	public String getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}

	public String getReCheckRule() {
		return reCheckRule;
	}

	public void setReCheckRule(String reCheckRule) {
		this.reCheckRule = reCheckRule;
	}

	public String getDict() {
		return dict;
	}

	public void setDict(String dict) {
		this.dict = dict;
	}

	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	public String getIsReadonly() {
		return isReadonly;
	}

	public void setIsReadonly(String isReadonly) {
		this.isReadonly = isReadonly;
	}

}
