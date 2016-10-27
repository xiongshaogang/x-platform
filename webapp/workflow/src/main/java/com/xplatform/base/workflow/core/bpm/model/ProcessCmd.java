package com.xplatform.base.workflow.core.bpm.model;

import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.workflow.core.bpm.util.BpmUtil;
import com.xplatform.base.workflow.core.facade.model.TaskExecutor;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.PUBLIC_MEMBER)
@XmlRootElement
public class ProcessCmd {
	private String actDefId;
	private String flowKey;
	private String taskId;
	private String nodeId;
	private String parentNodeId;
	private String parentBusinessKey;
	private String runId;

	private String subject = "";
	private String destTask;
	private String[] lastDestTaskIds;
	private String[] lastDestTaskUids;
	private List<TaskExecutor> taskExecutors = new ArrayList<TaskExecutor>();

	private String businessKey = "";
	private String businessName = "";
	private String stackId;
	private boolean skipPreHandler = false;

	private boolean skipAfterHandler = false;

	private Integer isBack = Integer.valueOf(0);
	private String backType;

	private boolean isRecover = false;

	private boolean isOnlyCompleteTask = false;

	private Short voteAgree = Short.valueOf((short) 1);

	private String voteContent = "";

	private String voteFieldName = "";

	private Map<String, Object> variables = new HashMap<String, Object>();

	private String formData = "";

	private Map formDataMap = new HashMap();

	private String currentUserId = "";

	private ProcessInstanceEntity processRun = null;

	private String userAccount = null;

	private boolean invokeExternal = false;

	private String informType = "";

	private String informStart = "";

	private boolean isPhone = false;

	private String isManage = "N";

	private String appCode = "";
	private String dynamicTask;
	private Short jumpType;

	public ProcessCmd() {
	}

	public ProcessCmd(String flowKey) {
		this.flowKey = flowKey;
	}

	public ProcessCmd(String flowKey, boolean skipPreHandler,
			boolean skipAfterHandler) {
		this.flowKey = flowKey;
		this.skipPreHandler = skipPreHandler;
		this.skipAfterHandler = skipAfterHandler;
	}

	public String getActDefId() {
		return this.actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getTaskId() {
		return this.taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Map<String, Object> getVariables() {
		return this.variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public void putVariables(Map<String, Object> variables) {
		this.variables.putAll(variables);
	}

	public void addVariable(String key, Object obj) {
		this.variables.put(key, obj);
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getSubject() {
		return this.subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDestTask() {
		return this.destTask;
	}

	public void setDestTask(String destTask) {
		this.destTask = destTask;
	}

	public String getBusinessKey() {
		return this.businessKey;
	}

	public void setBusinessKey(String businessKey) {
		this.businessKey = businessKey;
	}

	public String getFlowKey() {
		return this.flowKey;
	}

	public void setFlowKey(String flowKey) {
		this.flowKey = flowKey;
	}

	/**
	 * 驳回
	 * @return
	 */
	public Integer isBack() {
		return this.isBack;
	}

	public void setBack(Integer isBack) {
		this.isBack = isBack;
	}

	/**
	 * 追回
	 * 
	 * @return
	 */
	public boolean isRecover() {
		return this.isRecover;
	}

	public void setRecover(boolean isRecover) {
		this.isRecover = isRecover;
	}

	public Short getVoteAgree() {
		return this.voteAgree;
	}

	public void setVoteAgree(Short voteAgree) {
		if (TaskOpinionEntity.STATUS_RECOVER.equals(voteAgree)) {
			setRecover(true);
		}
		this.voteAgree = voteAgree;
	}

	public String getVoteContent() {
		return this.voteContent;
	}

	public void setVoteContent(String voteContent) {
		this.voteContent = voteContent;
	}

	public String getStackId() {
		return this.stackId;
	}

	public void setStackId(String stackId) {
		this.stackId = stackId;
	}

	public String getFormData() {
		return this.formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	public Map getFormDataMap() {
		return this.formDataMap;
	}

	public void setFormDataMap(Map formDataMap) {
		this.formDataMap = formDataMap;
	}

	public String getCurrentUserId() {
		return this.currentUserId;
	}

	public void setCurrentUserId(String currentUserId) {
		this.currentUserId = currentUserId;
	}

	public String getUserAccount() {
		return this.userAccount;
	}

	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}

	public String[] getLastDestTaskIds() {
		return this.lastDestTaskIds;
	}

	public void setLastDestTaskIds(String[] lastDestTaskIds) {
		this.lastDestTaskIds = lastDestTaskIds;
	}

	public String[] getLastDestTaskUids() {
		return this.lastDestTaskUids;
	}

	public void setLastDestTaskUids(String[] lastDestTaskUids) {
		this.lastDestTaskUids = lastDestTaskUids;
	}

	public boolean isOnlyCompleteTask() {
		return this.isOnlyCompleteTask;
	}

	public void setOnlyCompleteTask(boolean isOnlyCompleteTask) {
		this.isOnlyCompleteTask = isOnlyCompleteTask;
	}

	public boolean isInvokeExternal() {
		return this.invokeExternal;
	}

	public void setInvokeExternal(boolean invokeExternal) {
		this.invokeExternal = invokeExternal;
	}

	public String getInformType() {
		return this.informType;
	}


	public String getParentBusinessKey() {
		return parentBusinessKey;
	}

	public void setParentBusinessKey(String parentBusinessKey) {
		this.parentBusinessKey = parentBusinessKey;
	}

	public void setInformType(String informType) {
		this.informType = informType;
	}

	public boolean isSkipPreHandler() {
		return this.skipPreHandler;
	}

	public void setSkipPreHandler(boolean skipPreHandler) {
		this.skipPreHandler = skipPreHandler;
	}

	public boolean isSkipAfterHandler() {
		return this.skipAfterHandler;
	}

	public void setSkipAfterHandler(boolean skipAfterHandler) {
		this.skipAfterHandler = skipAfterHandler;
	}

	public Map<String, List<TaskExecutor>> getTaskExecutor() {
		Map map = new HashMap();
		if (BeanUtils.isEmpty(this.lastDestTaskIds))
			return map;
		for (int i = 0; i < this.lastDestTaskIds.length; i++) {
			String nodeId = this.lastDestTaskIds[i];
			String executor = this.lastDestTaskUids[i];
			if (StringUtil.isEmpty(executor))
				continue;
			List list = BpmUtil.getTaskExecutors(executor);
			map.put(nodeId, list);
		}
		return map;
	}

	public List<TaskExecutor> getTaskExecutors() {
		return this.taskExecutors;
	}

	public void setTaskExecutors(List<TaskExecutor> taskExecutors) {
		this.taskExecutors = taskExecutors;
	}

	public boolean isPhone() {
		return this.isPhone;
	}

	public void setPhone(boolean isPhone) {
		this.isPhone = isPhone;
	}

	public String getRunId() {
		return this.runId;
	}

	public void setRunId(String runId) {
		this.runId = runId;
	}

	public String getIsManage() {
		return this.isManage;
	}

	public void setIsManage(String isManage) {
		this.isManage = isManage;
	}

	public String getAppCode() {
		return this.appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getDynamicTask() {
		return this.dynamicTask;
	}

	public void setDynamicTask(String dynamicTask) {
		this.dynamicTask = dynamicTask;
	}

	public Short getJumpType() {
		return this.jumpType;
	}

	public void setJumpType(Short jumpType) {
		this.jumpType = jumpType;
	}

	public String getInformStart() {
		return this.informStart;
	}

	public void setInformStart(String informStart) {
		this.informStart = informStart;
	}

	public String getVoteFieldName() {
		return this.voteFieldName;
	}

	public void setVoteFieldName(String voteFieldName) {
		this.voteFieldName = voteFieldName;
	}

	public ProcessInstanceEntity getProcessRun() {
		return processRun;
	}

	public void setProcessRun(ProcessInstanceEntity processRun) {
		this.processRun = processRun;
	}

	public String getBusinessName() {
		return businessName;
	}

	public String getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(String parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public Integer getIsBack() {
		return isBack;
	}

	public void setIsBack(Integer isBack) {
		this.isBack = isBack;
	}

	public String getBackType() {
		return backType;
	}

	public void setBackType(String backType) {
		this.backType = backType;
	}

	public String toString() {
		return "ProcessCmd [actDefId=" + this.actDefId + ", flowKey="
				+ this.flowKey + ", taskId=" + this.taskId + ", runId="
				+ this.runId + ", destTask=" + this.destTask + ", isBack="
				+ this.isBack + ", isRecover=" + this.isRecover
				+ ", isOnlyCompleteTask=" + this.isOnlyCompleteTask
				+ ", voteAgree=" + this.voteAgree + ", informType="
				+ this.informType + "]";
	}
}