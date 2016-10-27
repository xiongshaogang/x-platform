package com.xplatform.base.workflow.instance.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程实例扩展
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年8月14日 上午10:42:05
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年8月14日 上午10:42:05
 *
 */
@Entity
@Table(name="t_flow_instance")
public class ProcessInstanceEntity extends OperationEntity {
	public static final Short STATUS_SUSPEND = Short.valueOf((short)0);//暂停
	public static final Short STATUS_RUNNING = Short.valueOf((short)1);//运行中
	public static final Short STATUS_FINISH = Short.valueOf((short)2);//完成
	public static final Short STATUS_MANUAL_FINISH = Short.valueOf((short)3);//人工终止
	public static final Short STATUS_FORM = Short.valueOf((short)4);//草稿
	public static final Short STATUS_RECOVER = Short.valueOf((short)5);//撤销
	public static final Short STATUS_REJECT = Short.valueOf((short)6);//驳回
	public static final Short STATUS_DELETE = Short.valueOf((short)10);//逻辑删除
	public static final Short RECOVER_NO = Short.valueOf((short)0);
	public static final Short RECOVER = Short.valueOf((short)1);
	
	
	private String defId;//流程定id
	private String defName;//流程定义名称
	private String defKey;//流程定义key
	private String actDefId;//acitiviti定义id
	
	private String title;//实例名称
	private String status;//状态
	private String actInstId;//acitiviti实例id
	private String parentId ;//父流程实例id
	
	private String businessKey;//业务主键
	private String businessName;//业务名称
	private Date endTime;//结束时间
	private String duration;//审批时长
	
	private String formCode;//开始节点表单id
	private Integer lastSubmitDuration;//基于最后一次提交，执行持续时间总长
	private String groupId;//群id
	
	@Column(name="def_id",nullable=true,length=32)
	public String getDefId() {
		return defId;
	}
	public void setDefId(String defId) {
		this.defId = defId;
	}
	
	@Column(name="def_name",nullable=true,length=100)
	public String getDefName() {
		return defName;
	}
	public void setDefName(String defName) {
		this.defName = defName;
	}
	
	@Column(name="title",nullable=true,length=300)
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	@Column(name="def_key",nullable=true,length=50)
	public String getDefKey() {
		return defKey;
	}
	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}
	
	@Column(name="act_id",nullable=true,length=32)
	public String getActDefId() {
		return actDefId;
	}
	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}
	
	@Column(name="status",nullable=true,length=10)
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="act_inst_id",nullable=true,length=32)
	public String getActInstId() {
		return actInstId;
	}
	public void setActInstId(String actInstId) {
		this.actInstId = actInstId;
	}
	
	@Column(name="parent_id",nullable=true,length=32)
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	
	@Column(name="business_key",nullable=true,length=50)
	public String getBusinessKey() {
		return businessKey;
	}
	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}
	
	@Column(name="end_time",nullable=true)
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	@Column(name="duration",nullable=true,length=50)
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	@Column(name="formCode",nullable=true,length=50)
	public String getFormCode() {
		return formCode;
	}
	public void setFormCode(String formCode) {
		this.formCode = formCode;
	}
	
	@Column(name="last_submit_duration",nullable=true,length=50)
	public Integer getLastSubmitDuration() {
		return lastSubmitDuration;
	}
	public void setLastSubmitDuration(Integer lastSubmitDuration) {
		this.lastSubmitDuration = lastSubmitDuration;
	}
	
	@Column(name="business_name",nullable=true,length=300)
	public String getBusinessName() {
		return businessName;
	}
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}
	
	@Column(name="groupId",nullable=true,length=32)
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
