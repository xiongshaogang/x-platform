package com.xplatform.base.form.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.ForeignKey;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.xplatform.base.framework.core.common.entity.BaseTreeEntity;
import com.xplatform.base.framework.core.common.entity.OperationEntity;
import com.xplatform.base.system.type.entity.TypeEntity;

@Entity
@Table(name="t_flow_form")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "type" })
public class FlowFormEntity extends BaseTreeEntity {
	private static final long serialVersionUID = 1L;
	private String name;//表单名称
	private String code;//表单code
	private String description;//描述
	private String url;//表单外部url
	private String content;//表单内容
	private Integer version;//版本号
	private String parentId;//父表单
	private String logo;//应用logo文件名
	//private String logoId;//应用logo
	private Integer isApp;//是否应用,有些表单并不是应用
	private Integer isFlow;//是否走流程审批（有审批人，则走流程，否则不走流程）
	private Integer isEdit;//是否直接编辑，不直接进入列表
	private Integer isStartAssign;//是否发起流程时才指定审批人
	private TypeEntity type;
	private Integer status;//应用发布状态 0保存，1发布 ，2删除
	private Integer isAutoDefinition;//是否自定义表单
	private String fieldJson; //前端控件json串
	private String fieldJsonTemp; //前端控件json串
	private String viewType; //视图类型，用于操作模板产生的数据是否可编辑
	private Integer notifyType; //0-不传阅 1-传阅
	private String mainFormCode; //主表code（若无主表，则是自己code）
	private String orgId; //组织机构id
	private Integer isSharefolder;//发布到共享栏目
	
	@Column(name = "version", length = 10)
	public Integer getVersion() {
		return version;
	}
	public void setVersion(Integer version) {
		this.version = version;
	}
	
	@Column(name = "parentId", length = 32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name = "logo", length = 32)
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	
	//@Column(name = "isFlow", columnDefinition="char", length = 1)
	@Column(name = "isFlow", nullable = true, length = 2)
	public Integer getIsFlow() {
		return isFlow;
	}
	public void setIsFlow(Integer isFlow) {
		this.isFlow = isFlow;
	}
	
	//@Column(name = "isEdit", columnDefinition="char", length = 1)
	@Column(name = "isEdit", nullable = true, length = 2)
	public Integer getIsEdit() {
		return isEdit;
	}
	public void setIsEdit(Integer isEdit) {
		this.isEdit = isEdit;
	}
	
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
	
	@Column(name = "description", nullable = true, length = 1000)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@Column(name = "url", nullable = true, length = 100)
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "type_id")
	@ForeignKey(name="null")
	public TypeEntity getType() {
		return type;
	}
	public void setType(TypeEntity type) {
		this.type = type;
	}
	
	@Column(name = "content", nullable = true, columnDefinition="longtext")
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	//@Column(name = "isApp", columnDefinition="char", length = 1)
	@Column(name = "isApp", nullable = true, length = 2)
	public Integer getIsApp() {
		return isApp;
	}
	public void setIsApp(Integer isApp) {
		this.isApp = isApp;
	}
	
	@Column(name = "status", nullable = true, length = 2)
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Column(name = "isAutoDefinition", nullable = true, length = 2)
	public Integer getIsAutoDefinition() {
		return isAutoDefinition;
	}
	public void setIsAutoDefinition(Integer isAutoDefinition) {
		this.isAutoDefinition = isAutoDefinition;
	}
	
	
	@Column(name = "fieldJson", length = 3000)
	public String getFieldJson() {
		return fieldJson;
	}
	public void setFieldJson(String fieldJson) {
		this.fieldJson = fieldJson;
	}
	
	@Column(name = "fieldJsonTemp", length = 3000)
	public String getFieldJsonTemp() {
		return fieldJsonTemp;
	}
	public void setFieldJsonTemp(String fieldJsonTemp) {
		this.fieldJsonTemp = fieldJsonTemp;
	}
	
	@Column(name = "viewType", nullable = true, length = 6)
	public String getViewType() {
		return viewType;
	}
	public void setViewType(String viewType) {
		this.viewType = viewType;
	}
	
	@Column(name = "isStartAssign")
	public Integer getIsStartAssign() {
		return isStartAssign;
	}
	
	public void setIsStartAssign(Integer isStartAssign) {
		this.isStartAssign = isStartAssign;
	}
	
	@Column(name = "notifyType", nullable = true, length = 6)
	public Integer getNotifyType() {
		return notifyType;
	}
	public void setNotifyType(Integer notifyType) {
		this.notifyType = notifyType;
	}
	
	@Column(name = "mainFormCode", length = 50)
	public String getMainFormCode() {
		return mainFormCode;
	}
	public void setMainFormCode(String mainFormCode) {
		this.mainFormCode = mainFormCode;
	}
	
/*	@Column(name = "logoId", length = 32)
	public String getLogoId() {
		return logoId;
	}
	public void setLogoId(String logoId) {
		this.logoId = logoId;
	}*/
	
	@Column(name = "orgId", length = 50)
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	
	@Column(name = "isSharefolder", length = 6)
	public Integer getIsSharefolder() {
		return isSharefolder;
	}
	public void setIsSharefolder(Integer isSharefolder) {
		this.isSharefolder = isSharefolder;
	}
	
}
