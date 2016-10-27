package com.xplatform.base.develop.codeconfig.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**   
 * @Title: Entity
 * @Description: 模板表单实体配置
 * @author by
 * @date 2014-05-14 18:18:44
 * @version V1.0   
 *
 */
@Entity
@Table(name = "t_dev_generate_entity_config", schema = "")
@SuppressWarnings("serial")
public class GenerateConfigEntity extends OperationEntity  implements java.io.Serializable {
	
	/**模板表单*/
//	private String formTypeId;
	/**所属实体表*/
	private String entityId ;
	/**功能描述*/
	private String description;
	/**包名*/
	private String pack ;
	/**实体类名*/
	private String entityClass;
	/**树根id*/
	private String treeParent;
	/**页面风格*/
	private String style;
	/**生成的文件类型*/
	private String fileType;
	/**显示类型*/
	private String showType;
	/**是否生成除jsp的代码*/
	private String isGenerate;
	/**与树表关联字段*/
	private String connect;
	/**引用的模块所在包*/
	private String refPackage;
	/**查询方式*/
	private String queryType;
	/**是否分页*/
	private String isPagenation ;
	/**是否复选*/
	private String selectType;
	/**是否可编辑*/
	private String enableEdit;
	/**表单实体关联表ID*/
	private String conBaseFormId;
	/**表类型：主表，单表，附表 */
	private String tableType;
	/**系统模块code*/
	private String moduleCode;
	/** 所配置的模板记录具体的实体对象数据 */
	private GenerateEntity  formTypeEntity;
	
	

	/**
	 *方法: 取得String
	 *@return: String  模板表单
	 */
//	@Column(name ="FORM_TYPE_ID",nullable=true,length=32)
//	public String getFormTypeId(){
//		return this.formTypeId;
//	}

	/**
	 *方法: 设置String
	 *@param: String  模板表单
	 */
//	public void setFormTypeId(String formTypeId){
//		this.formTypeId = formTypeId;
//	}
	/**
	 *方法: 取得String
	 *@return: String  所属实体表
	 */
	@Column(name ="ENTITY_ID ",nullable=true,length=32)
	public String getEntityId (){
		return this.entityId ;
	}

	/**
	 *方法: 设置String
	 *@param: String  所属实体表
	 */
	public void setEntityId (String entityId ){
		this.entityId  = entityId ;
	}
	/**
	 *方法: 取得String
	 *@return: String  功能描述
	 */
	@Column(name ="DESCRIPTION",nullable=true,length=1000)
	public String getDescription(){
		return this.description;
	}

	/**
	 *方法: 设置String
	 *@param: String  功能描述
	 */
	public void setDescription(String description){
		this.description = description;
	}
	/**
	 *方法: 取得String
	 *@return: String  包名
	 */
	@Column(name ="PACKAGE ",nullable=true,length=100)
	public String getPack (){
		return pack ;
	}

	/**
	 *方法: 设置String
	 *@param: String  包名
	 */
	public void setPack (String pack ){
		this.pack  = pack ;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  实体类名
	 */
	@Column(name ="ENTITY_CLASS",nullable=true,length=50)
	public String getEntityClass(){
		return this.entityClass;
	}

	/**
	 *方法: 设置String
	 *@param: String  实体类名
	 */
	public void setEntityClass(String entityClass){
		this.entityClass = entityClass;
	}
	/**
	 *方法: 取得String
	 *@return: String  树根id
	 */
	@Column(name ="TREE_PARENT",nullable=true,length=10)
	public String getTreeParent(){
		return this.treeParent;
	}

	/**
	 *方法: 设置String
	 *@param: String  树根id
	 */
	public void setTreeParent(String treeParent){
		this.treeParent = treeParent;
	}
	/**
	 *方法: 取得String
	 *@return: String  页面风格
	 */
	@Column(name ="STYLE",nullable=true,length=20)
	public String getStyle(){
		return this.style;
	}

	/**
	 *方法: 设置String
	 *@param: String  页面风格
	 */
	public void setStyle(String style){
		this.style = style;
	}
	/**
	 *方法: 取得String
	 *@return: String  生成的文件类型
	 */
	@Column(name ="FILE_TYPE",nullable=true,length=100)
	public String getFileType(){
		return this.fileType;
	}

	/**
	 *方法: 设置String
	 *@param: String  生成的文件类型
	 */
	public void setFileType(String fileType){
		this.fileType = fileType;
	}
	/**
	 *方法: 取得String
	 *@return: String  显示类型
	 */
	@Column(name ="SHOW_TYPE",nullable=true,length=20)
	public String getShowType(){
		return this.showType;
	}

	/**
	 *方法: 设置String
	 *@param: String  显示类型
	 */
	public void setShowType(String showType){
		this.showType = showType;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否生成除jsp的代码
	 */
	@Column(name ="IS_GENERATE",nullable=true,length=1)
	public String getIsGenerate(){
		return this.isGenerate;
	}

	/**
	 *方法: 设置String
	 *@param: String  是否生成除jsp的代码
	 */
	public void setIsGenerate(String isGenerate){
		this.isGenerate = isGenerate;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  与树表关联字段
	 */
	@Column(name ="CONNECT",nullable=true,length=100)
	public String getConnect() {
		return connect;
	}

	public void setConnect(String connect) {
		this.connect = connect;
	}

	/**
	 *方法: 取得String
	 *@return: String  引用的模块所在包
	 */
	@Column(name ="REF_PACKAGE",nullable=true,length=100)
	public String getRefPackage(){
		return this.refPackage;
	}

	/**
	 *方法: 设置String
	 *@param: String  引用的模块所在包
	 */
	public void setRefPackage(String refPackage){
		this.refPackage = refPackage;
	}
	/**
	 *方法: 取得String
	 *@return: String  查询方式
	 */
	@Column(name ="QUERY_TYPE",nullable=true,length=20)
	public String getQueryType(){
		return this.queryType;
	}

	/**
	 *方法: 设置String
	 *@param: String  查询方式
	 */
	public void setQueryType(String queryType){
		this.queryType = queryType;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否分页
	 */
	@Column(name ="IS_PAGENATION ",nullable=true,length=1)
	public String getIsPagenation (){
		return this.isPagenation ;
	}

	/**
	 *方法: 设置String
	 *@param: String  是否分页
	 */
	public void setIsPagenation (String isPagenation ){
		this.isPagenation  = isPagenation ;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否复选
	 */
	@Column(name ="SELECT_TYPE",nullable=true,length=1)
	public String getSelectType(){
		return this.selectType;
	}

	/**
	 *方法: 设置String
	 *@param: String  是否复选
	 */
	public void setSelectType(String selectType){
		this.selectType = selectType;
	}
	/**
	 *方法: 取得String
	 *@return: String  是否可编辑
	 */
	@Column(name ="ENABLE_EDIT",nullable=true,length=1)
	public String getEnableEdit(){
		return this.enableEdit;
	}

	/**
	 *方法: 设置String
	 *@param: String  是否可编辑
	 */
	public void setEnableEdit(String enableEdit){
		this.enableEdit = enableEdit;
	}
	/**
	 *方法: 取得String
	 *@return: String  表单实体关联表ID
	 */
	@Column(name ="CON_BASE_FORM_ID",nullable=true,length=32)
	public String getConBaseFormId(){
		return this.conBaseFormId;
	}

	/**
	 *方法: 设置String
	 *@param: String  表单实体关联表ID
	 */
	public void setConBaseFormId(String conBaseFormId){
		this.conBaseFormId = conBaseFormId;
	}
	
	/**
	 *方法: 取得String
	 *@return: String  表实体类型  
	 */
	@Column(name ="TABLE_TYPE",nullable=true,length=1)
	public String getTableType() {
		return tableType;
	}

	public void setTableType(String tableType) {
		this.tableType = tableType;
	}
	
	@Column(name ="module_code",nullable=false,length=100)
	public String getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(String moduleCode) {
		this.moduleCode = moduleCode;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "FORM_TYPE_ID")
	@ForeignKey(name="null")
	public GenerateEntity getFormTypeEntity() {
		return formTypeEntity;
	}

	public void setFormTypeEntity(GenerateEntity formTypeEntity) {
		this.formTypeEntity = formTypeEntity;
	}
	
	
}
