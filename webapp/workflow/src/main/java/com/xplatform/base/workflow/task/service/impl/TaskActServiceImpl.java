package com.xplatform.base.workflow.task.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormTableService;
import com.xplatform.base.form.service.FlowFormService;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.mybatis.entity.Page;
import com.xplatform.base.orgnaization.orggroup.service.OrgGroupService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.node.entity.NodeSetEntity;
import com.xplatform.base.workflow.node.service.NodeSetService;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.mybatis.dao.TaskDao;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

@Service("taskActService")
public class TaskActServiceImpl implements TaskActService {

	private TaskDao taskDao;
	@Resource
	private FlowService flowService;
	@Resource
	private TaskOpinionService taskOpinionService;
	@Resource
	private OrgGroupService orgGroupService;
	@Resource
	private NodeSetService nodeSetService;
	@Resource
	private FlowFormService flowFormService;
	@Resource
	private AppFormTableService appFormTableService;

	public void updTaskExecution(String taskId) {
		this.taskDao.updTaskExecution(taskId);
	}

	public Page<ProcessTask> getMyTasks(Page<ProcessTask> page) {
		//获取每个节点的进度
		List<ProcessTask> taskList=this.taskDao.getAllMyTaskByPage(page);
		if(taskList!=null && taskList.size()>0){//设置任务节点的进度
			for(ProcessTask task:taskList){
				/*Map<String,String> nodeList=flowService.getExecuteNodesMap(task.getProcessDefinitionId(),true);
				int totol=nodeList.keySet().size();
				int approved=1;
				List<TaskOpinionEntity> taskOpinionList=taskOpinionService.getApprovedList(task.getProcessInstanceId());
				if(taskOpinionList!=null && taskOpinionList.size()!=0){
					approved=taskOpinionList.size();
				}
				BigDecimal b=BigDecimal.valueOf(((float)approved/(float)totol)*100);*/
				task.setProgress("20%");
				NodeSetEntity bpmNodeSet = this.nodeSetService.getNodeSetByActDefIdNodeId(task.getProcessDefinitionId(), task.getTaskDefinitionKey());
				if(bpmNodeSet==null){
					bpmNodeSet= this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
				}else{
					//节点表单设置为空，取全局的表单设置
					if (StringUtil.isEmpty(bpmNodeSet.getFormId())&&StringUtil.isEmpty(bpmNodeSet.getFormUrl())) {
						NodeSetEntity globalNodeSet = this.nodeSetService.getGlobalByActDefId(task.getProcessDefinitionId());
						bpmNodeSet.setFormId(globalNodeSet.getFormId());
						bpmNodeSet.setFormType(globalNodeSet.getFormType());
					}
				}
				String fromId=bpmNodeSet.getFormId();
				FlowFormEntity flowForm=flowFormService.get(fromId);
				if(flowForm==null){
					continue;
				}
				try {
					Map<String,Object> extra=appFormTableService.getOneFieldData(flowForm.getCode(),task.getBusinessKey());
					if(extra==null){
						extra=new HashMap<String,Object>();
					}
					String groupId=task.getGroupId();
					if(StringUtil.isNotEmpty(groupId)){
						extra.put("groupId", groupId);
						Map<String,Object> groupUserList=orgGroupService.getUserOrg(groupId);
						extra.put("groupUserList", groupUserList.get("OrgGroupMember"));
					}
					extra.put("currentUserId", ((Map)page.getParameter()).get("userId"));
					extra.put("createTime", DateUtils.formatTime(task.getCreateTime()));
					task.setExtra(JSONHelper.toJSONString(extra));
					//task.setExtra(JSONHelper.toJSONString(extra));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		page.setResult(taskList);
		return page;
	}
	
	public Page<ProcessTask> getMyEndTaskList(Page<ProcessTask> page) {
		page.setResult(this.taskDao.getMyEndTaskByPage(page));
		return page;
	}

	public Page<ProcessTask> getMyCompleteTaskList(Page<ProcessTask> page) {
		page.setResult(this.taskDao.getMyCompleteTaskByPage(page));
		return page;
	}

	public Page<ProcessTask> getAll(Page<ProcessTask> page) {
		page.setResult(this.taskDao.getAllByPage(page));
		return page;
	}

	/*
	public List<?> getMyTasksCount(Long userId) {
		
		return getBySqlKey("getMyTaskCount", userId);
	}

	public List<TaskEntity> getMyMobileTasks(QueryFilter filter) {
		String statmentName = "getMyMobileTask";
		return getBySqlKey(statmentName, filter);
	}*/

	public List<ProcessTask> getMyEvents(Map param) {
		Map map = param;
		String mode = (String) map.get("mode");
		String sDate = (String) map.get("startDate");
		String eDate = (String) map.get("endDate");

		Date startDate = null;
		Date endDate = null;

		if ("month".equals(mode)) {
			try {
				Date reqDate = org.apache.commons.lang.time.DateUtils.parseDate(sDate, new String[] { "MM/dd/yyyy" });
				Calendar cal = Calendar.getInstance();
				cal.setTime(reqDate);
				startDate = DateUtils.setStartDay(cal).getTime();
				reqDate = org.apache.commons.lang.time.DateUtils.parseDate(eDate, new String[] { "MM/dd/yyyy" });
				cal.setTime(reqDate);
				endDate = DateUtils.setEndDay(cal).getTime();
			} catch (Exception ex) {

			}
		} else if ("day".equals(mode)) {
			try {
				Date reqDay = org.apache.commons.lang.time.DateUtils.parseDate(sDate, new String[] { "MM/dd/yyyy" });

				Calendar cal = Calendar.getInstance();
				cal.setTime(reqDay);

				startDate = DateUtils.setStartDay(cal).getTime();

				cal.add(2, 1);
				cal.add(5, -1);

				endDate = DateUtils.setEndDay(cal).getTime();
			} catch (Exception ex) {
			}
		} else if ("week".equals(mode)) {
			try {
				Date reqStartWeek = org.apache.commons.lang.time.DateUtils.parseDate(sDate,
						new String[] { "MM/dd/yyyy" });
				Date reqEndWeek = org.apache.commons.lang.time.DateUtils
						.parseDate(eDate, new String[] { "MM/dd/yyyy" });
				Calendar cal = Calendar.getInstance();

				cal.setTime(reqStartWeek);

				startDate = DateUtils.setStartDay(cal).getTime();
				cal.setTime(reqEndWeek);

				endDate = DateUtils.setEndDay(cal).getTime();
			} catch (Exception ex) {
			}
		} else if ("workweek".equals(mode)) {
			try {
				Date reqStartWeek = org.apache.commons.lang.time.DateUtils.parseDate(sDate,
						new String[] { "MM/dd/yyyy" });
				Date reqEndWeek = org.apache.commons.lang.time.DateUtils
						.parseDate(eDate, new String[] { "MM/dd/yyyy" });
				Calendar cal = Calendar.getInstance();

				cal.setTime(reqStartWeek);

				startDate = DateUtils.setStartDay(cal).getTime();
				cal.setTime(reqEndWeek);

				endDate = DateUtils.setEndDay(cal).getTime();
			} catch (Exception ex) {
			}
		}

		Map params = new HashMap();
		params.put("userId", ClientUtil.getUserEntity().getId());
		params.put("startDate", startDate);
		params.put("endDate", endDate);

		return this.taskDao.getAllMyEvent(params);
	}

	public int setDueDate(String taskId, Date dueDate) {
		Map params = new HashMap();
		params.put("taskId", taskId);
		params.put("dueDate", dueDate);
		return this.taskDao.setDueDate(params);
	}

	public void insertTask(ProcessTask task) {
		this.taskDao.add(task);
	}

	public List<ProcessTask> getReminderTask() {
		Date curDate = new Date(System.currentTimeMillis());
		return this.taskDao.getReminderTask(curDate);
	}

	public List<ProcessTask> getTimeReminderTask() {
		Date curDate = new Date(System.currentTimeMillis());
		return this.taskDao.getTimeReminderTask(curDate);
	}

	public List<ProcessTask> getTasksByRunId(String runId) {
		return this.taskDao.getTasksByRunId(runId);
	}

	public void updateTaskAssignee(String taskId, String userId) {
		Map params = new HashMap();
		params.put("taskId", Long.valueOf(Long.parseLong(taskId)));
		params.put("userId", userId);
		this.taskDao.updateTaskAssignee(params);
	}

	public void updateTaskDescription(String description, String taskId) {
		Map params = new HashMap();
		params.put("taskId", Long.valueOf(Long.parseLong(taskId)));
		params.put("description", description);
		this.taskDao.updateTaskDescription(params);
	}

	public void updateTaskAssigneeNull(String taskId) {
		this.taskDao.updateTaskAssigneeNull(taskId);
	}

	public void updateTaskOwner(String taskId, String userId) {
		Map params = new HashMap();
		params.put("taskId", taskId);
		params.put("userId", userId);
		this.taskDao.updateTaskOwner(params);
	}

	public ProcessTask getByTaskId(String taskId) {
		return this.taskDao.getByTaskId(taskId);
	}

	public List<ProcessTask> getByParentTaskIdAndDesc(String parentTaskId, String description) {
		Map params = new HashMap();
		params.put("parentTaskId", parentTaskId);
		params.put("description", description);
		return this.taskDao.getByParentTaskIdAndDesc(params);
	}

	public List<ProcessTask> getByInstanceIdTaskDefKey(String instanceId, String taskDefKey) {
		Map params = new HashMap();
		params.put("instanceId", instanceId);
		params.put("taskDefKey", taskDefKey);
		return this.taskDao.getByInstanceIdTaskDefKey(params);
	}

	public List<ProcessTask> getListByInstanceIdTaskDefKey(String instanceId, String taskDefKey) {
		Map params = new HashMap();
		params.put("instanceId", instanceId);
		params.put("taskDefKey", taskDefKey);
		return this.taskDao.getListByInstanceIdTaskDefKey(params);
	}

	public List<ProcessTask> getByInstanceId(String instanceId) {
		return this.taskDao.getByInstanceId(instanceId);
	}

	public void delByInstanceId(String instanceId) {
		this.taskDao.delByInstanceId(instanceId);
	}

	public void delCandidateByInstanceId(String instanceId) {
		this.taskDao.delCandidateByInstanceId(instanceId);
	}

	public void updateNewTaskDefKeyByInstIdNodeId(String newTaskDefKey, String oldTaskDefKey, String actInstId) {
		Map params = new HashMap();
		params.put("newTaskDefKey", newTaskDefKey);
		params.put("oldTaskDefKey", oldTaskDefKey);
		params.put("actInstId", new Long(actInstId));
		this.taskDao.updateNewTaskDefKeyByInstIdNodeId(params);
	}

	public void updateOldTaskDefKeyByInstIdNodeId(String newTaskDefKey, String oldTaskDefKey, String actInstId) {
		Map params = new HashMap();
		params.put("newTaskDefKey", StringUtil.isNotEmpty(newTaskDefKey) ? newTaskDefKey + "%" : newTaskDefKey);
		params.put("oldTaskDefKey", oldTaskDefKey);
		params.put("actInstId", new Long(actInstId));
		this.taskDao.updateOldTaskDefKeyByInstIdNodeId(params);
	}

	public List<Map<String, Object>> getHasCandidateExecutor(String taskIds) {
		List taskList = new ArrayList();
		String[] aryTask = taskIds.split(",");
		for (int i = 0; i < aryTask.length; i++) {
			taskList.add(aryTask[i]);
		}

		return this.taskDao.getHasCandidateExecutor(taskList);
	}

	public void delByParentId(String parentId) {
		this.taskDao.delByParentId(parentId);
	}

	public void updateTask(String taskId, String userId, String description) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("taskId", new Long(taskId));
		params.put("userId", userId);
		params.put("description", description);
		params.put("updateTime", new Date());
		this.taskDao.updateTask(params);
	}

	public void delCommuTaskByInstNodeUser(Long instInstId, String nodeId, Long userId) {
		Map params = new HashMap();
		params.put("instInstId", instInstId);
		params.put("nodeId", nodeId);
		params.put("userId", userId);
		this.taskDao.delCommuTaskByInstNodeUser(params);
	}

	public void delCommuTaskByParentTaskId(String parentTaskId) {
		this.taskDao.delCommuTaskByParentTaskId(parentTaskId);
	}

	public void delTransToTaskByParentTaskId(String parentTaskId) {
		this.taskDao.delTransToTaskByParentTaskId(parentTaskId);
	}

	public List<ProcessTask> getTaskByInstId(String actInstId) {
		return this.taskDao.getTaskByInstId(actInstId);
	}

	public List<ProcessTask> getHisTaskByInstId(String actInstId) {
		return this.taskDao.getHisTaskByInstId(actInstId);
	}

	public boolean getHasRightsByTask(String taskId, String userId) {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("taskId", taskId);
		params.put("userId", userId);
		return this.taskDao.getHasRightsByTask(params) > 0;
	}

	public boolean hasRead(Long taskId, Long userId) {
		Map params = new HashMap();
		params.put("taskId", taskId);
		params.put("userId", userId);

		Integer cnt = this.taskDao.hasRead(params);
		return cnt.intValue() > 0;
	}

	public List<ProcessTask> getTaskByActDefId(String actDefId, int num) {
		Map params = new HashMap();
		params.put("actDefId", actDefId);
		params.put("num", Integer.valueOf(num));
		return this.taskDao.getTaskByActDefId(params);
	}

	public boolean getHisByInstanceidAndUserId(Long actInstId, Long userId) {
		Map params = new HashMap();
		params.put("actInstId", actInstId);
		params.put("userId", userId);
		Integer rtn = this.taskDao.getHisByInstanceidAndUserId(params);
		return rtn.intValue() > 0;
	}

	public List<ProcessTask> getByTaskNameOrTaskIds(String userId, String taskName, String taskIds) {
		Map params = new HashMap();
		params.put("userId", userId);
		params.put("taskName", taskName);
		params.put("taskIds", taskIds);
		return this.taskDao.getByTaskNameOrTaskIds(params);
	}
	
	public void delDelegateUser(String taskId){
		this.taskDao.delDelegateUser(taskId);
	}
	
	public void setTaskUser(Map<String,Object> params){
		this.taskDao.setTaskUser(params);
	}
	
	@Autowired
	public void setTaskDao(TaskDao taskDao) {
		this.taskDao = taskDao;
	}

}
