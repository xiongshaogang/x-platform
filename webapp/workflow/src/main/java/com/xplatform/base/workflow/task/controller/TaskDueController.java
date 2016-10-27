package com.xplatform.base.workflow.task.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.core.bpm.model.FlowNode;
import com.xplatform.base.workflow.core.bpm.model.NodeCache;
import com.xplatform.base.workflow.support.msgtemplate.entity.MsgTemplateEntity;
import com.xplatform.base.workflow.support.msgtemplate.service.MsgTemplateService;
import com.xplatform.base.workflow.task.entity.TaskDueEntity;
import com.xplatform.base.workflow.task.service.TaskDueService;

/**
 * 
 * description : 任务催办管理controller
 *
 * @version 1.0
 * @author xiehs
 * @createtime : 2014年5月24日 下午12:32:17
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                  修改内容
 * --------------- ------------------- -----------------------------------
 * xiehs        2014年5月24日 下午12:32:17
 *
 */
@Controller
@RequestMapping("/taskDueController")
public class TaskDueController extends BaseController {

	@Resource
	private TaskDueService taskDueService;

	@Resource
	private MsgTemplateService msgTemplateService;

	private AjaxJson result = new AjaxJson();

	private String message;

	/**
	 * 任务催办管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "taskDue")
	public ModelAndView taskDue(HttpServletRequest request) {
		//	String taskDueId = request.getParameter("id");
		String actDefId = request.getParameter("actDefId");
		String nodeId = request.getParameter("nodeId");
		String defId = request.getParameter("defId");
		//获取任务节点错版列表
		List<TaskDueEntity> taskReminders = this.taskDueService.getByActDefAndNodeId(actDefId, nodeId);

		//构造执行动作combobox数据
		Map<String, FlowNode> nodeMaps = NodeCache.getByActDefId(actDefId);
		List<ComboBox> comboNodes = new ArrayList<ComboBox>();
		for (Map.Entry<String, FlowNode> entry : nodeMaps.entrySet()) {
			FlowNode node = (FlowNode) entry.getValue();
			ComboBox comboBox = new ComboBox();
			comboBox.setId(node.getNodeId());
			comboBox.setText(node.getNodeName());
			if ((!"userTask".equals(node.getNodeType())) && (!"startEvent".equals(node.getNodeType())))
				continue;
			if (nodeId.equals(node.getNodeId())) {
				comboNodes.add(0, comboBox);
			} else {
				comboNodes.add(comboBox);
			}
		}
		String jsonNodes = JSONHelper.toJSONString(comboNodes);

		//构造发送信息次数combobox数据
		List<ComboBox> comboNums = new ArrayList<ComboBox>();
		for (int i = 0; i < 11; i++) {
			ComboBox comboBox = new ComboBox();
			comboBox.setId("" + i);
			comboBox.setText("" + i);
			comboNums.add(comboBox);
		}
		String jsonNums = JSONHelper.toJSONString(comboNums);

		//构造小时数combobox数据
		List<ComboBox> comboHours = new ArrayList<ComboBox>();
		for (int i = 0; i < 24; i++) {
			ComboBox comboBox = new ComboBox();
			comboBox.setId("" + i);
			comboBox.setText(i + "小时");
			comboHours.add(comboBox);
		}
		String jsonHours = JSONHelper.toJSONString(comboHours);

		//构造分钟数combobox数据
		List<ComboBox> comboMinutes = new ArrayList<ComboBox>();
		for (int i = 0; i < 5; i++) {
			ComboBox comboBox = new ComboBox();
			comboBox.setId("" + i);
			comboBox.setText(i + "分钟");
			comboMinutes.add(comboBox);
		}
		for (int i = 5; i < 56; i = i + 5) {
			ComboBox comboBox = new ComboBox();
			comboBox.setId("" + i);
			comboBox.setText(i + "分钟");
			comboMinutes.add(comboBox);
		}
		String jsonMinutes = JSONHelper.toJSONString(comboMinutes);

		List flowVars = null;//bpmFormFieldService.getFlowVarByFlowDefId(defId);
		List bpmdefVars = null;//this.bpmDefVarService.getVarsByFlowDefId(defId.longValue());

		int reminderStartDay = 0;
		int reminderStartHour = 0;
		int reminderStartMinute = 0;
		int reminderEndDay = 0;
		int reminderEndHour = 0;
		int reminderEndMinute = 0;
		int completeTimeDay = 0;
		int completeTimeHour = 0;
		int completeTimeMinute = 0;

		TaskDueEntity taskReminder = null;

		//获得催办默认模板
		MsgTemplateEntity msgTemplate = msgTemplateService.getDefaultByUseType(MsgTemplateEntity.USE_TYPE_URGE);
		String innerContent = msgTemplate.getInnerContent();
		String mailContent = msgTemplate.getMailContent();
		String smsContent = msgTemplate.getSmsContent();
		String commonTitle = msgTemplate.getTitle();
		if (taskReminders != null && taskReminders.size() > 0) {
			taskReminder = taskReminders.get(0);
		}

		if (taskReminder == null) {//新增
			taskReminder = new TaskDueEntity();
			taskReminder.setInnerContent(innerContent);
			taskReminder.setMailContent(mailContent);
			taskReminder.setSmsContent(smsContent);
			taskReminder.setCommonTitle(commonTitle);
		} else {//编辑
			//	taskReminder= this.taskDueService.get(taskDueId);
			//开始发送催办时间
			//			int reminderStart = taskReminder.getReminderStart().intValue();
			//			reminderStartDay = reminderStart / 1440;
			//			reminderStartHour = (reminderStart - reminderStartDay * 1440) / 60;
			//			reminderStartMinute = reminderStart - reminderStartDay * 1440 - reminderStartHour * 60;
			//发送催办的间隔
			int reminderEnd = taskReminder.getReminderEnd().intValue();
			reminderEndDay = reminderEnd / 1440;
			reminderEndHour = (reminderEnd - reminderEndDay * 1440) / 60;
			reminderEndMinute = reminderEnd - reminderEndDay * 1440 - reminderEndHour * 60;
			//相对任务节点完成时间
			int complateTime = taskReminder.getCompleteTime().intValue();
			completeTimeDay = complateTime / 1440;
			completeTimeHour = (complateTime - completeTimeDay * 1440) / 60;
			completeTimeMinute = complateTime - completeTimeDay * 1440 - completeTimeHour * 60;
		}

		//查询默认的邮件、站内信、短信模板

		return new ModelAndView("workflow/task/taskDue")
				.addObject("taskReminder", taskReminder)
				//编辑的数据
				.addObject("taskReminders", taskReminders)
				//列表的数据
				.addObject("defId", defId)
				//流程定义id
				.addObject("actDefId", actDefId)
				//activiti定义id
				.addObject("nodeId", nodeId)
				//节点id
				.addObject("flowVars", flowVars).addObject("defVars", bpmdefVars)
				.addObject("reminderStartDay", Integer.valueOf(reminderStartDay))
				.addObject("reminderStartHour", Integer.valueOf(reminderStartHour))
				.addObject("reminderStartMinute", Integer.valueOf(reminderStartMinute))
				.addObject("reminderEndDay", Integer.valueOf(reminderEndDay))
				.addObject("reminderEndHour", Integer.valueOf(reminderEndHour))
				.addObject("reminderEndMinute", Integer.valueOf(reminderEndMinute))
				.addObject("completeTimeDay", Integer.valueOf(completeTimeDay))
				.addObject("completeTimeHour", Integer.valueOf(completeTimeHour))
				.addObject("completeTimeMinute", Integer.valueOf(completeTimeMinute)).addObject("jsonNodes", jsonNodes)
				.addObject("jsonNums", jsonNums).addObject("jsonHours", jsonHours)
				.addObject("jsonMinutes", jsonMinutes).addObject("commonTitle", commonTitle)
				.addObject("smsContent", smsContent).addObject("mailContent", mailContent)
				.addObject("innerContent", innerContent);
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskDue
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskDueEntity TaskDue, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskDueEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskDue, request.getParameterMap());
		try {
			//自定义追加查询条件
		} catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskDueService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务催办删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskDue
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskDueEntity TaskDue, HttpServletRequest request) {
		message = "任务催办删除成功";
		try {
			taskDueService.delete(TaskDue.getId());
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除任务催办
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			taskDueService.batchDelete(ids);
			message = "删除成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 新增或修改任务催办
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskDue
	 * @return
	 */
	@RequestMapping(params = "saveTaskDue")
	@ResponseBody
	public AjaxJson saveTaskDue(HttpServletRequest request, TaskDueEntity taskDue) {
		//催办开始时间
		//		int reminderStartDay = Integer.parseInt(request.getParameter("reminderStartDay"));
		//	    int reminderStartHour = Integer.parseInt(request.getParameter("reminderStartHour"));
		//	    int reminderStartMinute = Integer.parseInt(request.getParameter("reminderStartMinute"));
		//	    int reminderStart = (reminderStartDay * 24 + reminderStartHour) * 60 + reminderStartMinute;
		//	    taskDue.setReminderStart(reminderStart);
		//催办间隔时间
		int reminderEndDay = Integer.parseInt(request.getParameter("reminderEndDay"));
		int reminderEndHour = Integer.parseInt(request.getParameter("reminderEndHour"));
		int reminderEndMinute = Integer.parseInt(request.getParameter("reminderEndMinute"));
		int reminderEnd = (reminderEndDay * 24 + reminderEndHour) * 60 + reminderEndMinute;
		taskDue.setReminderEnd(reminderEnd);
		//催办完成时间
		int completeTimeDay = Integer.parseInt(request.getParameter("completeTimeDay"));
		int completeTimeHour = Integer.parseInt(request.getParameter("completeTimeHour"));
		int completeTimeMinute = Integer.parseInt(request.getParameter("completeTimeMinute"));
		int completeTime = (completeTimeDay * 24 + completeTimeHour) * 60 + completeTimeMinute;
		taskDue.setCompleteTime(completeTime);
		//数据存储
		try {
			if (StringUtil.isNotEmpty(taskDue.getId())) {
				message = "任务催办更新成功";
				taskDueService.update(taskDue);
			} else {
				message = "任务催办新增成功";
				taskDueService.save(taskDue);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("TaskDue", taskDue);
		result.setObj(attributes);
		return result;
	}

	/**
	 * 根据id取得催办信息数据
	 */
	@RequestMapping(params = "getTaskDue")
	@ResponseBody
	public AjaxJson getNodeRule(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		TaskDueEntity taskReminder = this.taskDueService.get(id);

		int reminderStartDay = 0;
		int reminderStartHour = 0;
		int reminderStartMinute = 0;
		int reminderEndDay = 0;
		int reminderEndHour = 0;
		int reminderEndMinute = 0;
		int completeTimeDay = 0;
		int completeTimeHour = 0;
		int completeTimeMinute = 0;

		//开始发送催办时间
		//		int reminderStart = taskReminder.getReminderStart().intValue();
		//		reminderStartDay = reminderStart / 1440;
		//		reminderStartHour = (reminderStart - reminderStartDay * 1440) / 60;
		//		reminderStartMinute = reminderStart - reminderStartDay * 1440 - reminderStartHour * 60;
		//发送催办的间隔
		int reminderEnd = taskReminder.getReminderEnd().intValue();
		reminderEndDay = reminderEnd / 1440;
		reminderEndHour = (reminderEnd - reminderEndDay * 1440) / 60;
		reminderEndMinute = reminderEnd - reminderEndDay * 1440 - reminderEndHour * 60;
		//相对任务节点完成时间
		int complateTime = taskReminder.getCompleteTime().intValue();
		completeTimeDay = complateTime / 1440;
		completeTimeHour = (complateTime - completeTimeDay * 1440) / 60;
		completeTimeMinute = complateTime - completeTimeDay * 1440 - completeTimeHour * 60;

		Map<String, Object> attributes = new HashMap<String, Object>();
		attributes.put("taskDue", taskReminder);
		attributes.put("reminderStartDay", reminderStartDay);
		attributes.put("reminderStartHour", reminderStartHour);
		attributes.put("reminderStartMinute", reminderStartMinute);
		attributes.put("reminderEndDay", reminderEndDay);
		attributes.put("reminderEndHour", reminderEndHour);
		attributes.put("reminderEndMinute", reminderEndMinute);
		attributes.put("completeTimeDay", completeTimeDay);
		attributes.put("completeTimeHour", completeTimeHour);
		attributes.put("completeTimeMinute", completeTimeMinute);

		result.setObj(attributes);
		return result;
	}

	public void setTaskDueService(TaskDueService taskDueService) {
		this.taskDueService = taskDueService;
	}

}
