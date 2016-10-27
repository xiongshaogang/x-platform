package com.xplatform.base.workflow.task.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.BeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.workflow.core.facade.service.FlowService;
import com.xplatform.base.workflow.instance.entity.ProcessInstanceEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstanceService;
import com.xplatform.base.workflow.task.entity.TaskExeEntity;
import com.xplatform.base.workflow.task.service.TaskExeService;

/**
 * 
 * description : 任务转办代理管理controller
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
@RequestMapping("/taskExeController")
public class TaskExeController extends BaseController {
	
	@Resource
	private TaskExeService taskExeService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private FlowService flowService;
	@Resource
	private ProcessInstanceService processInstanceService;
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务转办代理管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "taskExe")
	public ModelAndView taskExe(HttpServletRequest request) {
		return new ModelAndView("workflow/task/taskExe/taskExeList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskExe
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskExeEntity TaskExe,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskExeEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskExe, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskExeService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}
	
	/**
	 * 任务转办代理管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "myTaskList")
	public ModelAndView myTaskList(HttpServletRequest request) {
		return new ModelAndView("workflow/task/taskExe/myTaskExeList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskExe
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "myTaskListDatagrid")
	public void myTaskListDatagrid(TaskExeEntity TaskExe,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskExeEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskExe, request.getParameterMap());
		try{
			//自定义追加查询条件
			cq.eq("ownerId", ClientUtil.getUserEntity().getId());
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskExeService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务转办代理删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskExe
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskExeEntity taskExe, HttpServletRequest request) {
		message = "任务转办代理删除成功";
		
		
		String opinion = request.getParameter("opinion");
		String informType = request.getParameter("informType");

		String bpmTaskExeType = null;
		try {
			TaskExeEntity bpmTaskExe = this.taskExeService.get(taskExe.getId());

			if (TaskExeEntity.TYPE_ASSIGNEE == bpmTaskExe.getType())
				bpmTaskExeType = "代理";
			else if (TaskExeEntity.TYPE_TRANSMIT == bpmTaskExe.getType()) {
				bpmTaskExeType = "转办";
			}

			String taskId = bpmTaskExe.getTaskId();
			TaskEntity taskEntity = this.flowService.getTask(taskId);
			if (taskEntity == null) {
				result.setMsg("任务已经结束!");
				return result;
			}
			String ownerId = bpmTaskExe.getOwnerId();
			if (ownerId == null) {
				result.setMsg("找不到原来的任务执行人!");
				return result;
			}
			UserEntity sysUser = this.sysUserService.getUserById(ownerId);
			if (sysUser == null) {
				result.setMsg("找不到原来的任务执行人!");
				return result;
			}
			this.taskExeService.cancel(bpmTaskExe, sysUser, opinion,informType);

		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			taskExeService.batchDelete(ids);
			UserEntity sysUser = ClientUtil.getUserEntity();
			String opinion = request.getParameter("opinion");
			String informType = request.getParameter("informType");
			if (StringUtil.isEmpty(ids)) {
				result.setMsg("请选择需要取消转办的项目!");
				return result;
			}
			this.taskExeService.cancelBat(ids, opinion,informType, sysUser);
			message="批量取消成功";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "taskExeEdit")
	public ModelAndView taskExeEdit(TaskExeEntity TaskExe,Model model) {
		if (StringUtil.isNotEmpty(TaskExe.getId())) {
			TaskExe = taskExeService.get(TaskExe.getId());
			model.addAttribute("TaskExe", TaskExe);
		}
		return new ModelAndView("platform/organization/TaskExe/TaskExeEdit");
	}
	
	/**
	 * 新增或修改任务转办代理
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskExe
	 * @return
	 */
	@RequestMapping(params = "saveTaskExe")
	@ResponseBody
	public AjaxJson saveTaskExe(TaskExeEntity TaskExe) {
		try {
			if (StringUtil.isNotEmpty(TaskExe.getId())) {
				message = "任务转办代理更新成功";
				taskExeService.update(TaskExe);
			} else {
				message = "任务转办代理新增成功";
				taskExeService.save(TaskExe);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	@RequestMapping(params = "assignee")
	public ModelAndView assignee(HttpServletRequest request,Model model) {
		model.addAttribute("taskId", request.getParameter("taskId"));
		return new ModelAndView("workflow/task/taskExeAssignee");
	}
	
	@RequestMapping(params ="assignSave")
	@ResponseBody
	public AjaxJson  assignSave(HttpServletRequest request,HttpServletResponse response){
		try {
			String taskId = request.getParameter("taskId");
			String userId= request.getParameter("exeUserId");
			UserEntity user=sysUserService.getUserById(userId);
			String memo = request.getParameter("description");//转交原因
			String informType = request.getParameter("informType");

			UserEntity sysUser = ClientUtil.getUserEntity();
			TaskEntity taskEntity = this.flowService.getTask(taskId);
			if (BeanUtils.isEmpty(taskEntity)) {
				message="任务已经被处理！";
			}else{
				ProcessInstanceEntity processRun = this.processInstanceService.getByActInstanceId(taskEntity.getProcessInstanceId());
				TaskExeEntity bpmTaskExe = new TaskExeEntity();
				bpmTaskExe.setTaskId(taskId);
				bpmTaskExe.setTaskDefKey(taskEntity.getTaskDefinitionKey());
				bpmTaskExe.setTaskName(taskEntity.getName());
				bpmTaskExe.setAssigneeId(userId);
				bpmTaskExe.setAssigneeName(user.getName());
				bpmTaskExe.setOwnerId(sysUser.getId());
				bpmTaskExe.setOwnerName(sysUser.getName());
				bpmTaskExe.setSubject(processRun.getTitle());
				bpmTaskExe.setStatus(TaskExeEntity.STATUS_INIT);
				bpmTaskExe.setDescription(memo);
				bpmTaskExe.setInstId(processRun.getId());
				bpmTaskExe.setActInstId(taskEntity.getProcessInstanceId());
				bpmTaskExe.setTaskDefKey(taskEntity.getTaskDefinitionKey());
				bpmTaskExe.setTaskName(taskEntity.getName());
				bpmTaskExe.setType(TaskExeEntity.TYPE_TRANSMIT);
				bpmTaskExe.setInformType(informType);
				this.taskExeService.saveAssignee(bpmTaskExe,sysUser);
			}
			message="任务已转办设置成功！";
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	public void setTaskExeService(TaskExeService taskExeService) {
		this.taskExeService = taskExeService;
	}
	
}
