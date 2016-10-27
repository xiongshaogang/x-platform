package com.xplatform.base.develop.codeconfig.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * @Title: Entity
 * @Description: 页面实体类字段配置表
 * @author xiaqiang
 * @date 2014-05-12 20:09:32
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_dev_generate_entity_field")
@SuppressWarnings("serial")
public class GenerateFieldEntity extends OperationEntity implements
		java.io.Serializable {
	/****************** 常规 ******************/
	/** 所配置的实体类(Id) */
	private GenerateConfigEntity formEntityId;
	/** 所配置的字段对应的实体字段表对象(Id) */
	private MetaDataFieldEntity fieldEntityId;
	/** 字段名称 */
	private String fieldName;
	/** 字段显示名 */
	private String fieldLabel;
	/** 字段类型 */
	private String fieldType;
	/** 字段显示类型字典值(文本输入、日期输入、下拉选择....) */
	private String showtypeDict;
	/** 字段长度 */
	private Integer fieldLength;
	/** 字段精度 */
	private Integer fieldPersion;
	/** 字段描述 */
	private String description;
	/****************** 查询相关 ******************/
	/** 是否作为查询组件字段  Y/N */
	private String queryShow;
	/**
	 * 是否显示在查询框中(比如查询框中固定有一个input隐藏域去存性别的值,当做隐藏查询条件)
	 * (要和queryShow属性区分开,isShowInQuery是作为了查询字段但是不显示)
	 */
	private String isShowInQuery;
	/** 字段查询操作符字典值(存入=,>=,in 等等) */
	private String queryConDict;
	/** 查询模式 */
	private String sQueryMode;
	/** 查询输入框类型*/
	private String sQueryInputType;
	/** 查询数据字典*/
	private String sDictCode;
	/** 查询临时数据*/
	private String sData;
	/** 扩展属性*/
	private String extend;
	/****************** 列表相关 ******************/
	/** 列表是否显示 */
	private String listShow;
	/** 字段顺序(在列表中按此顺序显示) */
	private Integer gridIndex;
	/** 显示位置字典值(居中、左、右....) */
	private String alignDict;
	/** 字段样式(在列表中的) */
	private String gridStyle;
	/** 在列表中是否排序 */
	private String isOrder;
	/****************** 新增/编辑页相关 ******************/
	/** 编辑是否显示 */
	private String editShow;
	/** 是否只读 */
	private String isReadonly;
	/** 编辑页中需要占的列数 */
	private Integer pageRowspan;
	/** 数据字典code(若该字段是显示数据字典的话) */
	private String dictCode;
	/** 调用url */
	private String dataUrl;
	/** 预设验证(name) */
	private String formRuleEntity;
	/** 验证规则错误提示 */
	private String errorMsg;
	/** 验证为空错误提示 */
	private String nullMsg;
	/** 所有验证规则的与、或关系 */
	private String andOrRule;
	/** 自定义校验规则 */
	private String reCheckRule;
	/** 是否唯一 */
	private String isUnique;
	/** 是否可为空 */
	private String isNullable;
	/** 字段样式(在编辑页中的) */
	private String pageStyle;

	/****************** 树形相关 ******************/

	public GenerateFieldEntity() {
	}

	@Column(name = "form_rule_entity", length = 100)
	public String getFormRuleEntity() {
		return formRuleEntity;
	}

	public void setFormRuleEntity(String formRuleEntity) {
		this.formRuleEntity = formRuleEntity;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "field_entity_id")
	public MetaDataFieldEntity getFieldEntityId() {
		return fieldEntityId;
	}

	public void setFieldEntityId(MetaDataFieldEntity fieldEntityId) {
		this.fieldEntityId = fieldEntityId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "form_entity_id")
	public GenerateConfigEntity getFormEntityId() {
		return formEntityId;
	}

	public void setFormEntityId(GenerateConfigEntity formEntityId) {
		this.formEntityId = formEntityId;
	}

	@Column(name = "field_name", nullable = true, length = 100)
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	@Column(name = "field_label", nullable = true, length = 100)
	public String getFieldLabel() {
		return fieldLabel;
	}

	public void setFieldLabel(String fieldLabel) {
		this.fieldLabel = fieldLabel;
	}

	@Column(name = "field_type", nullable = true)
	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	@Column(name = "field_length", nullable = true, length = 4)
	public Integer getFieldLength() {
		return fieldLength;
	}

	public void setFieldLength(Integer fieldLength) {
		this.fieldLength = fieldLength;
	}

	@Column(name = "field_persion", nullable = true, length = 4)
	public Integer getFieldPersion() {
		return fieldPersion;
	}

	public void setFieldPersion(Integer fieldPersion) {
		this.fieldPersion = fieldPersion;
	}

	@Column(name = "showtype_dict", nullable = true, length = 100)
	public String getShowtypeDict() {
		return showtypeDict;
	}

	public void setShowtypeDict(String showtypeDict) {
		this.showtypeDict = showtypeDict;
	}

	@Column(name = "description", columnDefinition = "TEXT", nullable = true)
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "list_show", nullable = true, columnDefinition = "char")
	public String getListShow() {
		return listShow;
	}

	public void setListShow(String listShow) {
		this.listShow = listShow;
	}

	@Column(name = "query_show", nullable = true, columnDefinition = "char")
	public String getQueryShow() {
		return queryShow;
	}

	public void setQueryShow(String queryShow) {
		this.queryShow = queryShow;
	}

	@Column(name = "edit_show", nullable = true, columnDefinition = "char")
	public String getEditShow() {
		return editShow;
	}

	public void setEditShow(String editShow) {
		this.editShow = editShow;
	}

	@Column(name = "is_readonly", nullable = true, columnDefinition = "char")
	public String getIsReadonly() {
		return isReadonly;
	}

	public void setIsReadonly(String isReadonly) {
		this.isReadonly = isReadonly;
	}

	@Column(name = "is_unique", nullable = true, columnDefinition = "char")
	public String getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(String isUnique) {
		this.isUnique = isUnique;
	}

	@Column(name = "re_check_rule", nullable = true, columnDefinition = "TEXT")
	public String getReCheckRule() {
		return reCheckRule;
	}

	public void setReCheckRule(String reCheckRule) {
		this.reCheckRule = reCheckRule;
	}

	@Column(name = "dictCode", nullable = true, length = 100)
	public String getDictCode() {
		return dictCode;
	}

	public void setDictCode(String dictCode) {
		this.dictCode = dictCode;
	}

	@Column(name = "data_url", columnDefinition = "TEXT", nullable = true)
	public String getDataUrl() {
		return dataUrl;
	}

	public void setDataUrl(String dataUrl) {
		this.dataUrl = dataUrl;
	}

	@Column(name = "is_show_in_query", nullable = true, columnDefinition = "char")
	public String getIsShowInQuery() {
		return isShowInQuery;
	}

	public void setIsShowInQuery(String isShowInQuery) {
		this.isShowInQuery = isShowInQuery;
	}

	@Column(name = "grid_index", nullable = true)
	public Integer getGridIndex() {
		return gridIndex;
	}

	public void setGridIndex(Integer gridIndex) {
		this.gridIndex = gridIndex;
	}

	@Column(name = "align_dict", nullable = true, length = 100)
	public String getAlignDict() {
		return alignDict;
	}

	public void setAlignDict(String alignDict) {
		this.alignDict = alignDict;
	}

	@Column(name = "grid_style", nullable = true, columnDefinition = "TEXT")
	public String getGridStyle() {
		return gridStyle;
	}

	public void setGridStyle(String gridStyle) {
		this.gridStyle = gridStyle;
	}

	@Column(name = "is_order", nullable = true, columnDefinition = "char")
	public String getIsOrder() {
		return isOrder;
	}

	public void setIsOrder(String isOrder) {
		this.isOrder = isOrder;
	}

	@Column(name = "page_rowspan", nullable = true)
	public Integer getPageRowspan() {
		return pageRowspan;
	}

	public void setPageRowspan(Integer pageRowspan) {
		this.pageRowspan = pageRowspan;
	}

	@Column(name = "is_nullable", nullable = true, columnDefinition = "char")
	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	@Column(name = "page_style", nullable = true, columnDefinition = "TEXT")
	public String getPageStyle() {
		return pageStyle;
	}

	public void setPageStyle(String pageStyle) {
		this.pageStyle = pageStyle;
	}

	@Column(name = "query_con_dict", nullable = true, length = 100)
	public String getQueryConDict() {
		return queryConDict;
	}

	public void setQueryConDict(String queryConDict) {
		this.queryConDict = queryConDict;
	}
	
	@Column(name = "s_query_mode", nullable = true, length = 30)
	public String getsQueryMode() {
		return sQueryMode;
	}

	public void setsQueryMode(String sQueryMode) {
		this.sQueryMode = sQueryMode;
	}

	@Column(name = "s_query_input_type", nullable = true, length = 20)
	public String getsQueryInputType() {
		return sQueryInputType;
	}

	public void setsQueryInputType(String sQueryInputType) {
		this.sQueryInputType = sQueryInputType;
	}

	@Column(name = "s_dict_code", nullable = true, length = 30)
	public String getsDictCode() {
		return sDictCode;
	}

	public void setsDictCode(String sDictCode) {
		this.sDictCode = sDictCode;
	}

	@Column(name = "s_data", nullable = true, length = 100)
	public String getsData() {
		return sData;
	}

	public void setsData(String sData) {
		this.sData = sData;
	}

	@Column(name = "extend", nullable = true, length = 100)
	public String getExtend() {
		return extend;
	}

	public void setExtend(String extend) {
		this.extend = extend;
	}

	@Column(name = "error_msg", nullable = true, length = 200)
	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	@Column(name = "null_msg", nullable = true, length = 200)
	public String getNullMsg() {
		return nullMsg;
	}

	public void setNullMsg(String nullMsg) {
		this.nullMsg = nullMsg;
	}

	@Column(name = "and_or_rule", nullable = true, length = 10)
	public String getAndOrRule() {
		return andOrRule;
	}

	public void setAndOrRule(String andOrRule) {
		this.andOrRule = andOrRule;
	}

}
