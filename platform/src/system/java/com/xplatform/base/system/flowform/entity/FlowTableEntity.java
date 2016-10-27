package com.xplatform.base.system.flowform.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.OrderBy;

import com.xplatform.base.framework.core.common.entity.OperationEntity;
/**
 * @Title: Entity
 * @Description:   流程表单相关表
 * @author 宾勇
 * @date 2015-01-04 
 * @version V1.0
 *
 */
@Entity
@Table(name = "t_flow_table")
@SuppressWarnings("serial")
public class FlowTableEntity extends OperationEntity{

	/** 表名*/
	private String tableName;
	/** 注释 */
	private String content;
	/** 是否主表 */
	private String isMainTable;
	/** 主表id*/
	private String mainTable;
	/** 主键*/
	private String pkfield;
	/** 外键字段*/
	private String relation;
	/** 是否发布*/
	private String ispublished;
	/** 发布人*/
	private String publishedby;
	/** 发布时间*/
	private Date publishtime;
	/** 生成方式*/
	private String generateType;
	/** 是否外部表*/
	private String isexternal;
	/** 版本号*/
	private Integer versionno;
	
	private String listtemplate;
	private String detailtemplate;
	/** 分组*/
	private String team;
	
	private List<FlowFieldEntity> columns;
	
	@Column(name = "table_name", nullable = false, length = 64)
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	@Column(name = "content", nullable = true, length = 200)
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	@Column(name = "is_main_table", nullable = true, length = 5)
	public String getIsMainTable() {
		return isMainTable;
	}
	public void setIsMainTable(String isMainTable) {
		this.isMainTable = isMainTable;
	}
	
	@Column(name = "main_table", nullable = true, length = 32)
	public String getMainTable() {
		return mainTable;
	}
	public void setMainTable(String mainTable) {
		this.mainTable = mainTable;
	}
	
	@Column(name = "generate_type", nullable = true, length = 20)
	public String getGenerateType() {
		return generateType;
	}
	public void setGenerateType(String generateType) {
		this.generateType = generateType;
	}
	
	@Column(name = "is_published", nullable = true, length = 5)
	public String getIspublished() {
		return ispublished;
	}
	public void setIspublished(String ispublished) {
		this.ispublished = ispublished;
	}
	
	@Column(name = "publish_by", nullable = true, length = 32)
	public String getPublishedby() {
		return publishedby;
	}
	public void setPublishedby(String publishedby) {
		this.publishedby = publishedby;
	}
	
	@Column(name = "publish_time", nullable = true, length = 32)
	public Date getPublishtime() {
		return publishtime;
	}
	public void setPublishtime(Date publishtime) {
		this.publishtime = publishtime;
	}
	
	@Column(name = "is_external", nullable = true, length = 5)
	public String getIsexternal() {
		return isexternal;
	}
	public void setIsexternal(String isexternal) {
		this.isexternal = isexternal;
	}
	
	@Column(name = "version_no", nullable = true, length = 15)
	public Integer getVersionno() {
		return versionno;
	}
	public void setVersionno(Integer versionno) {
		this.versionno = versionno;
	}
	
	@Column(name = "list_template", nullable = true, length = 2000)
	public String getListtemplate() {
		return listtemplate;
	}
	public void setListtemplate(String listtemplate) {
		this.listtemplate = listtemplate;
	}
	
	@Column(name = "detail_template", nullable = true, length = 2000)
	public String getDetailtemplate() {
		return detailtemplate;
	}
	public void setDetailtemplate(String detailtemplate) {
		this.detailtemplate = detailtemplate;
	}
	
	@Column(name = "team", nullable = true, length = 2000)
	public String getTeam() {
		return team;
	}
	public void setTeam(String team) {
		this.team = team;
	}
	
	@Column(name = "pk_field", nullable = true, length = 50)
	public String getPkfield() {
		return pkfield;
	}
	public void setPkfield(String pkfield) {
		this.pkfield = pkfield;
	}
	
	@Column(name = "relation", nullable = true, length = 50)
	public String getRelation() {
		return relation;
	}
	public void setRelation(String relation) {
		this.relation = relation;
	}
	
	@OneToMany(cascade=CascadeType.REMOVE,mappedBy="table")
	public List<FlowFieldEntity> getColumns() {
		return columns;
	}
	public void setColumns(List<FlowFieldEntity> columns) {
		this.columns = columns;
	}
}
