package com.xplatform.base.workflow.node.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.xplatform.base.framework.core.common.entity.OperationEntity;

/**
 * 
 * description :流程节点设置
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年7月1日 下午2:02:12
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年7月1日 下午2:02:12
 *
 */
@Entity
@Table(name = "t_flow_node_set")
public class NodeSetEntity extends OperationEntity {

	public static final String FORM_TYPE_NULL = "-1";
	public static final String FORM_TYPE_ONLINE = "0";
	public static final String FORM_TYPE_URL = "1";

	public static final String NODE_TYPE_NORMAL = "0";
	public static final String NODE_TYPE_FORK = "1";
	public static final String NODE_TYPE_JOIN = "2";

	public static final String BACK_ALLOW = "1";
	public static final String HIDE_OPTION = "1";
	public static final String HIDE_PATH = "1";
	public static final String NOT_HIDE_PATH = "0";
	public static final String NOT_HIDE_OPTION = "0";

	public static final String JUMP_TYPE_NORMAL = "1";
	public static final String JUMP_TYPE_SELECT = "2";
	public static final String JUMP_TYPE_FREE = "3";
	public static final String JUMP_TYPE_SELF = "4";

	public static final String BACK_ALLOW_START = "1";
	public static final String BACK_DENY = "0";

	public static final String SetType_TaskNode = "0";//节点表单
	public static final String SetType_StartForm = "1";//流程实例开始表单
	public static final String SetType_GloabalForm = "2";//全局表单
	public static final String SetType_BPMForm = "3";//业务综合表单

	public static final String RULE_INVALID_NORMAL = "1";
	public static final String RULE_INVALID_NO_NORMAL = "0";

	private String defId; //流程定义id
	private String nodeName; //节点name
	private Integer nodeOrder; //节点序号
	private String actDefId; //activiti Id
	private String nodeId; //节点id
	private String formType = "-1"; //表单类型（没有设置，url，表单实体）
	private String formKey; //表单key
	private String formName; //表单名
	private String formId; //表单id
	private String formUrl; //表单来源
	private String oldFormKey;//上一次修改前表单
	private String isJumpForDef = "1";
	private String nodeType; //节点类型
	private String joinTaskKey; //汇总任务key
	private String joinTaskName;//汇总任务节点名
	private String beforeHandler;//前置处理器
	private String afterHandler;//后置处理器
	private String jumpType;//跳转类型
	private String setType = "0";//节点设置类型（全局表单或者任务节点表单）
	private String isHideOption = "0";//是否隐藏意见
	private String isHidePath = "0";//是否隐藏路径
	private String isAllowMobile = "0";//任务是否发送到mobile
	private String informType; //节点通知类型
	private String isInform="0"; //是否发送消息到执行人

	private String preInnerContent;//前置节点通知站内信模板内容
	private String preCommonTitle;//前置节点通知邮件与站内信标题
	private String preMailContent;//前置节点通知邮件模板内容
	private String preSmsContent;//前置节点通知短信模版

	private String lastInnerContent;//后置节点通知站内信模板内容
	private String lastCommonTitle;//后置节点通知邮件与站内信标题
	private String lastMailContent;//后置节点通知邮件模板内容
	private String lastSmsContent;//后置节点通知短信模版
	private String backType;//返回类型

	@Column(name = "def_id", nullable = true, length = 32)
	public String getDefId() {
		return defId;
	}

	public void setDefId(String defId) {
		this.defId = defId;
	}

	@Column(name = "node_name", nullable = true, length = 100)
	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	@Column(name = "node_order", nullable = true, length = 10)
	public Integer getNodeOrder() {
		return nodeOrder;
	}

	public void setNodeOrder(Integer nodeOrder) {
		this.nodeOrder = nodeOrder;
	}

	@Column(name = "act_id", nullable = true, length = 32)
	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	@Column(name = "node_id", nullable = true, length = 32)
	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	@Column(name = "form_type", nullable = true, length = 3)
	public String getFormType() {
		return formType;
	}

	public void setFormType(String formType) {
		this.formType = formType;
	}

	@Column(name = "form_url", nullable = true, length = 100)
	public String getFormUrl() {
		return formUrl;
	}

	public void setFormUrl(String formUrl) {
		this.formUrl = formUrl;
	}

	@Column(name = "form_key", nullable = true, length = 50)
	public String getFormKey() {
		return formKey;
	}

	public void setFormKey(String formKey) {
		this.formKey = formKey;
	}

	@Column(name = "old_form_key", nullable = true, length = 32)
	public String getOldFormKey() {
		return oldFormKey;
	}

	public void setOldFormKey(String oldFormKey) {
		this.oldFormKey = oldFormKey;
	}

	@Column(name = "form_name", nullable = true, length = 100)
	public String getFormName() {
		return formName;
	}

	public void setFormName(String formName) {
		this.formName = formName;
	}

	@Column(name = "form_id", nullable = true, length = 32)
	public String getFormId() {
		return formId;
	}

	public void setFormId(String formId) {
		this.formId = formId;
	}

	@Column(name = "is_jump_def", columnDefinition = "char", nullable = true)
	public String getIsJumpForDef() {
		return isJumpForDef;
	}

	public void setIsJumpForDef(String isJumpForDef) {
		this.isJumpForDef = isJumpForDef;
	}

	@Column(name = "node_type", nullable = true, length = 100)
	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	@Column(name = "join_task_key", nullable = true, length = 50)
	public String getJoinTaskKey() {
		return joinTaskKey;
	}

	public void setJoinTaskKey(String joinTaskKey) {
		this.joinTaskKey = joinTaskKey;
	}

	@Column(name = "join_task_name", nullable = true, length = 100)
	public String getJoinTaskName() {
		return joinTaskName;
	}

	public void setJoinTaskName(String joinTaskName) {
		this.joinTaskName = joinTaskName;
	}

	@Column(name = "before_handler", nullable = true, length = 1000)
	public String getBeforeHandler() {
		return beforeHandler;
	}

	public void setBeforeHandler(String beforeHandler) {
		this.beforeHandler = beforeHandler;
	}

	@Column(name = "after_handler", nullable = true, length = 1000)
	public String getAfterHandler() {
		return afterHandler;
	}

	public void setAfterHandler(String afterHandler) {
		this.afterHandler = afterHandler;
	}

	@Column(name = "jump_type", nullable = true, length = 10)
	public String getJumpType() {
		return jumpType;
	}

	public void setJumpType(String jumpType) {
		this.jumpType = jumpType;
	}

	@Column(name = "set_type", nullable = true, length = 100)
	public String getSetType() {
		return setType;
	}

	public void setSetType(String setType) {
		this.setType = setType;
	}

	@Column(name = "is_hide_option", columnDefinition = "char", nullable = true, length = 1)
	public String getIsHideOption() {
		return isHideOption;
	}

	public void setIsHideOption(String isHideOption) {
		this.isHideOption = isHideOption;
	}

	@Column(name = "is_hide_path", columnDefinition = "char", nullable = true, length = 1)
	public String getIsHidePath() {
		return isHidePath;
	}

	public void setIsHidePath(String isHidePath) {
		this.isHidePath = isHidePath;
	}

	@Column(name = "is_allow_mobile", columnDefinition = "char", nullable = true, length = 1)
	public String getIsAllowMobile() {
		return isAllowMobile;
	}

	public void setIsAllowMobile(String isAllowMobile) {
		this.isAllowMobile = isAllowMobile;
	}

	@Column(name = "info_type", nullable = true, length = 50)
	public String getInformType() {
		return informType;
	}

	public void setInformType(String informType) {
		this.informType = informType;
	}

	@Column(name = "back_type", nullable = true, length = 30)
	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	@Column(name = "pre_inner_content", nullable = true, length = 1000)
	public String getPreInnerContent() {
		return preInnerContent;
	}

	public void setPreInnerContent(String preInnerContent) {
		this.preInnerContent = preInnerContent;
	}

	@Column(name = "pre_common_title", nullable = true, length = 1000)
	public String getPreCommonTitle() {
		return preCommonTitle;
	}

	public void setPreCommonTitle(String preCommonTitle) {
		this.preCommonTitle = preCommonTitle;
	}

	@Column(name = "pre_mail_content", nullable = true, length = 1000)
	public String getPreMailContent() {
		return preMailContent;
	}

	public void setPreMailContent(String preMailContent) {
		this.preMailContent = preMailContent;
	}

	@Column(name = "pre_sms_content", nullable = true, length = 1000)
	public String getPreSmsContent() {
		return preSmsContent;
	}

	public void setPreSmsContent(String preSmsContent) {
		this.preSmsContent = preSmsContent;
	}

	@Column(name = "last_inner_content", nullable = true, length = 1000)
	public String getLastInnerContent() {
		return lastInnerContent;
	}

	public void setLastInnerContent(String lastInnerContent) {
		this.lastInnerContent = lastInnerContent;
	}

	@Column(name = "last_common_title", nullable = true, length = 1000)
	public String getLastCommonTitle() {
		return lastCommonTitle;
	}

	public void setLastCommonTitle(String lastCommonTitle) {
		this.lastCommonTitle = lastCommonTitle;
	}

	@Column(name = "last_mail_content", nullable = true, length = 1000)
	public String getLastMailContent() {
		return lastMailContent;
	}

	public void setLastMailContent(String lastMailContent) {
		this.lastMailContent = lastMailContent;
	}

	@Column(name = "last_sms_content", nullable = true, length = 1000)
	public String getLastSmsContent() {
		return lastSmsContent;
	}

	public void setLastSmsContent(String lastSmsContent) {
		this.lastSmsContent = lastSmsContent;
	}

	@Column(name = "is_inform",columnDefinition="char", nullable = true, length = 1)
	public String getIsInform() {
		return isInform;
	}

	public void setIsInform(String isInform) {
		this.isInform = isInform;
	}

}
