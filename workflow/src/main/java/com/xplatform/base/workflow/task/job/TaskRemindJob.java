package com.xplatform.base.workflow.task.job;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.impl.persistence.entity.HistoricProcessInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricTaskInstanceEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;

import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.DateUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.system.message.config.entity.MessageSendEntity;
import com.xplatform.base.system.message.config.service.MessageService;
import com.xplatform.base.system.timer.job.BaseJob;
import com.xplatform.base.workflow.core.bpm.model.ProcessCmd;
import com.xplatform.base.workflow.core.facade.service.ActService;
import com.xplatform.base.workflow.history.service.HistoryProcessInstanceService;
import com.xplatform.base.workflow.history.service.HistoryTaskInstanceService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;
import com.xplatform.base.workflow.task.entity.TaskDueStateEntity;
import com.xplatform.base.workflow.task.mybatis.vo.ProcessTask;
import com.xplatform.base.workflow.task.service.TaskActService;
import com.xplatform.base.workflow.task.service.TaskDueService;
import com.xplatform.base.workflow.task.service.TaskDueStateService;
import com.xplatform.base.workflow.task.service.TaskUserService;
import com.xplatform.base.workflow.util.FlowUtil;

public class TaskRemindJob extends BaseJob {
	private Log logger = LogFactory.getLog(TaskRemindJob.class);

	@Override
	public void executeJob(JobExecutionContext context) throws Exception {
		// TODO Auto-generated method stub

		TaskDueService taskDueService = ApplicationContextUtil.getBean("taskDueService");
		GroovyScriptEngine groovyScriptEngine = ApplicationContextUtil.getBean("scriptEngine");
		RuntimeService runtimeService = ApplicationContextUtil.getBean("runtimeService");
		TaskActService taskActService = ApplicationContextUtil.getBean("taskActService");

		// 获取所有设置了催办任务的节点
		List<ProcessTask> list = taskActService.getReminderTask();
		if (list.size() == 0) {
			this.logger.debug("没有获取到任务!");
			return;
		}

		for (ProcessTask task : list) {

			String actDefId = task.getProcessDefinitionId();

			String nodeId = task.getTaskDefinitionKey();

			List<TaskDueEntity> taskReminders = taskDueService.getByActDefAndNodeId(actDefId, nodeId);
			String executionId = task.getExecutionId();
			Map<String, Object> vars = runtimeService.getVariables(executionId);
			for (TaskDueEntity taskReminder : taskReminders) {
				String conditionExp = StringUtil.replaceBlank(taskReminder.getConditon());
				// 第一步：计算是否满足条件
				if (StringUtil.isNotEmpty(conditionExp)) {
					boolean result = groovyScriptEngine.executeBoolean(conditionExp, vars);
					if (!result) {
						this.logger.debug("Skip TaskReminder :" + taskReminder.getId() + "," + taskReminder.getName());
						continue;
					}
				}
				this.logger.debug("Execute TaskReminder :" + taskReminder.getId() + "," + taskReminder.getName());

				//获得本次需要催办的人集合
				Set<String> userSet = getUserByTask(task);
				// 第二步：发送消息催办
				if (taskReminder.getTimes().intValue() > 0) {
					handReminder(task, taskReminder, userSet, vars);
				}
				// 第三步：到期处理任务
				handlerDueTask(task, taskReminder, userSet);
			}
		}

	}

	private void handReminder(ProcessTask task, TaskDueEntity taskReminder, Set<String> userSet,
			Map<String, Object> vars) throws Exception {

		TaskDueStateService taskDueStateService = ApplicationContextUtil.getBean("taskDueStateService");
		String actInstanceId = task.getProcessInstanceId();
		String taskId = task.getId();
		String actDefId = task.getProcessDefinitionId();
		int needRemindTimes = taskReminder.getTimes().intValue();//需要提醒次数

		//		int reminderStart = taskReminder.getReminderStart().intValue();//开始时间

		int interval = taskReminder.getReminderEnd().intValue();//间隔频率

		String completeTime = taskReminder.getCompleteTime() + "分钟";

		//获得相对任务的日期时间
		Date relativeStartTime = getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(),
				Integer.parseInt(taskReminder.getRelativeNodeType()));
		if (relativeStartTime == null) {
			return;
		}
		//获得相对任务到期后时间
		Date startDate = new Date(DateUtils.getNextTime(1, taskReminder.getCompleteTime().intValue(),
				relativeStartTime.getTime()));
		for (String userId : userSet) {
			//查询催办消息 已提醒次数
			int reminderTimes = taskDueStateService.getAmountByUserTaskId(taskId, userId, "1", taskReminder.getId());
			if (reminderTimes >= needRemindTimes) {
				continue;
			}
			int startAdd = interval * reminderTimes;//间隔*已提醒次数=离发生催办已过去时间(分钟)
			Date curDate = new Date();//当前日期时间
			Date dueDate = null;
			if ("1".equals(taskReminder.getRelativeTimeType())) { //日历日方式
				dueDate = new Date(DateUtils.getNextTime(1, startAdd, startDate.getTime()));//获得应当触发的时间
			} else {//工作日方式

			}
			//如果应通知时间比当前晚,则不触发
			if (dueDate.compareTo(curDate) > 0) {
				continue;
			}
			//发送消息
			sendMsg(userId, taskReminder, task, completeTime, vars);
			//保存催办状态
			saveReminderState(taskId, actDefId, actInstanceId, userId, "1", taskReminder.getId());
		}
	}

	public void sendMsg(String userId, TaskDueEntity taskReminder, ProcessTask task, String time,
			Map<String, Object> vars) {
		SysUserService sysUserService = ApplicationContextUtil.getBean("sysUserService");
		ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
		MessageService messageService = ApplicationContextUtil.getBean("messageService");
		String actInstanceId = task.getProcessInstanceId();
		String taskId = task.getId();

		ProcessInstanceEntity process = processInstanceService.getByActInstanceId(actInstanceId);
		//获取接收人(多个的话,用","隔开)
		//List<EmpUserVo> empUserVoList = sysUserService.getEmpUserByUserIds(userId);
		StringBuffer sb1 = new StringBuffer();
		StringBuffer sb2 = new StringBuffer();
		/*for (EmpUserVo empUserVo : empUserVoList) {
			sb1.append(empUserVo.getEmpName()).append(",");
			sb2.append(empUserVo.getEmpId()).append(",");
		}*/
		String empNames = StringUtil.removeDot(sb1.toString());
		String empIds = StringUtil.removeDot(sb2.toString());

		//构造消息发送数据
		MessageSendEntity msg = new MessageSendEntity();

		String commonTitle = taskReminder.getCommonTitle().replace("${收件人}", empNames).replace("${发件人}", "系统短信")
				.replace("${剩余时间}", taskReminder.getCompleteTime() + "分钟").replace("${跳转地址}", "")
				.replace("${事项名称}", process.getTitle());
		String smsContent = taskReminder.getSmsContent().replace("${收件人}", empNames).replace("${发件人}", "系统短信")
				.replace("${剩余时间}", taskReminder.getCompleteTime() + "分钟").replace("${跳转地址}", "")
				.replace("${事项名称}", process.getTitle());
		String jumpUrl = "<a href='" + FlowUtil.getUrl(taskId, true) + "'>" + process.getTitle() + "</a>";
		String mailContent = taskReminder.getMailContent().replace("${收件人}", empNames).replace("${发件人}", "系统短信")
				.replace("${剩余时间}", taskReminder.getCompleteTime() + "分钟").replace("${跳转地址}", jumpUrl)
				.replace("${事项名称}", process.getTitle());
		mailContent = FlowUtil.replaceVars(mailContent, vars);
		String innerContent = taskReminder.getInnerContent().replace("${收件人}", empNames).replace("${发件人}", "系统短信")
				.replace("${剩余时间}", taskReminder.getCompleteTime() + "分钟").replace("${跳转地址}", jumpUrl)
				.replace("${事项名称}", process.getTitle());
		innerContent = FlowUtil.replaceVars(innerContent, vars);

		msg.setTitle(commonTitle);
		msg.setSmsContent(smsContent);
		msg.setMailContent(mailContent);
		/*msg.setInnerContent(innerContent);
		msg.setMessageType("taskRemind");
		msg.setMailConfigId(null);
		msg.setReceiveIds(empIds);
		msg.setSendType(taskReminder.getSendType());
		msg.setTypeId(taskReminder.getId());*/

		messageService.saveMsgSend(msg);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 上午11:06:27
	 * @Decription 按预设方式完成催办动作
	 *
	 * @param task
	 * @param taskReminder
	 * @param userSet
	 * @throws Exception
	 */
	private void handlerDueTask(ProcessTask task, TaskDueEntity taskReminder, Set<String> userSet) throws Exception {

		GroovyScriptEngine groovyScriptEngine = ApplicationContextUtil.getBean("scriptEngine");
		ProcessInstanceService processInstanceService = ApplicationContextUtil.getBean("processInstanceService");
		TaskDueStateService taskDueStateService = ApplicationContextUtil.getBean("taskDueStateService");
		ActService actService = ApplicationContextUtil.getBean("actService");
		String actInstanceId = task.getProcessInstanceId();
		String taskId = task.getId();
		String actDefId = task.getProcessDefinitionId();

		//获得相对任务的日期时间
		Date relativeStartTime = getRelativeStartTime(actInstanceId, taskReminder.getRelativeNodeId(),
				Integer.parseInt(taskReminder.getRelativeNodeType()));
		if (relativeStartTime == null) {
			return;
		}

		//获得相对任务到期后时间
		Date startDate = new Date(DateUtils.getNextTime(1, taskReminder.getCompleteTime().intValue(),
				relativeStartTime.getTime()));

		if ("1".equals(taskReminder.getRelativeTimeType())) { //日历日方式
			int reminderTimes = taskDueStateService.getAmountByTaskId(taskId, "2", taskReminder.getId());
			if (reminderTimes == 0) {
				Date curDate = new Date();
				if (startDate.compareTo(curDate) <= 0) {
					handlerAction(taskReminder, processInstanceService, task, actService, groovyScriptEngine);
					saveReminderState(taskId, actDefId, actInstanceId, null, "2", taskReminder.getId());
				}
			}

		} else {//工作日方式

		}
	}

	private void handlerAction(TaskDueEntity taskReminder, ProcessInstanceService processRunService, ProcessTask task,
			ActService processService, GroovyScriptEngine scriptEngine) throws Exception {
		Integer action = Integer.valueOf(taskReminder.getAction());
		String taskId = task.getId();
		ProcessCmd processCmd = new ProcessCmd();
		processCmd.setTaskId(taskId);
		switch (action.intValue()) {
		case 1:
			processCmd.setVoteAgree(new Short("1"));
			processRunService.nextProcess(processCmd);
			this.logger.debug("对该任务执行同意操作");
			break;
		case 2:
			processCmd.setVoteAgree(new Short("2"));
			processRunService.nextProcess(processCmd);
			this.logger.debug("对该任务执行反对操作");
			break;
		case 3:
			processCmd.setVoteAgree(new Short("3"));
			processCmd.setBack(Integer.valueOf(1));
			processRunService.nextProcess(processCmd);
			this.logger.debug("对该任务执行驳回操作");
			break;
		case 4:
			processCmd.setVoteAgree(new Short("3"));
			processCmd.setBack(Integer.valueOf(2));
			processRunService.nextProcess(processCmd);
			this.logger.debug("对该任务执行驳回到发起人操作");
			break;
		case 6:
			processService.endProcessByTaskId(task.getId());
			this.logger.debug("结束流程");
			break;
		case 7:
			String script = taskReminder.getScript();
			Map<String, Object> vars = new HashMap<String, Object>();
			vars.put("task", task);
			scriptEngine.execute(script, vars);
			this.logger.debug("执行指定的脚本");
		case 5:
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 上午11:05:51
	 * @Decription 查询需要通知的人
	 *
	 * @param task
	 * @return
	 */
	private Set<String> getUserByTask(ProcessTask task) {
		TaskUserService taskUserService = ApplicationContextUtil.getBean("taskUserService");
		Set<String> set = new HashSet<String>();
		String assignee = task.getAssignee();
		//先查询任务指定人
		if (StringUtil.isNotEmpty(task.getAssignee())) {
			set.add(assignee);
		} else { //没有则指定任务候选人
			Set<UserEntity> users = null;
			try {
				users = taskUserService.getCandidateUsers(task.getId());
			} catch (BusinessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (UserEntity user : users) {
				set.add(user.getId());
			}
		}
		return set;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 上午11:05:58
	 * @Decription 保存提示状态
	 *
	 * @param taskId
	 * @param actDefId
	 * @param actInstanceId
	 * @param userId
	 * @param remindType
	 * @throws Exception
	 */
	private void saveReminderState(String taskId, String actDefId, String actInstanceId, String userId,
			String remindType, String taskDueId) throws Exception {
		TaskDueStateService taskDueStateService = ApplicationContextUtil.getBean("taskDueStateService");
		TaskDueStateEntity reminderState = new TaskDueStateEntity();
		reminderState.setActInstId(actInstanceId);
		reminderState.setCreateUserId(userId);
		reminderState.setActDefId(actDefId);
		reminderState.setTaskId(taskId);
		reminderState.setType(remindType);
		reminderState.setTaskDueId(taskDueId);
		taskDueStateService.save(reminderState);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年10月29日 上午11:09:43
	 * @Decription 获得某个任务到期的相对时间
	 *
	 * @param actInstanceId
	 * @param nodeId
	 * @param eventType
	 * @return
	 */
	public Date getRelativeStartTime(String actInstanceId, String nodeId, Integer eventType) {
		HistoryProcessInstanceService historyProcessInstanceService = ApplicationContextUtil
				.getBean("historyProcessInstanceService");
		HistoryTaskInstanceService historyTaskInstanceService = ApplicationContextUtil
				.getBean("historyTaskInstanceService");
		//创建的任务类型
		Map<String, String> param = new HashMap<String, String>();
		param.put("actInstanceId", actInstanceId);
		param.put("nodeId", nodeId);
		List<HistoricTaskInstanceEntity> list = historyTaskInstanceService.getByInstanceIdAndNodeId(param);
		if (list != null) {
			HistoricTaskInstanceEntity historicTaskInstanceEntity = list.get(0);
			if (new Integer(0).equals(eventType)) {
				return historicTaskInstanceEntity.getStartTime();
			}
			return historicTaskInstanceEntity.getEndTime();
		}
		//完成的任务类型
		HistoricProcessInstanceEntity historicProcessInstanceEntity = historyProcessInstanceService
				.getByInstanceIdAndNodeId(actInstanceId, nodeId);
		if (historicProcessInstanceEntity != null)
			return historicProcessInstanceEntity.getStartTime();
		return null;
	}
}
