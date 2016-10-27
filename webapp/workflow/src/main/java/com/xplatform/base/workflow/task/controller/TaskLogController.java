package com.xplatform.base.workflow.task.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.task.entity.TaskLogEntity;
import com.xplatform.base.workflow.task.service.TaskLogService;

/**
 * 
 * description : 任务日志管理controller
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
@RequestMapping("/taskLogController")
public class TaskLogController extends BaseController {
	
	@Resource
	private TaskLogService taskLogService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务日志管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskLog")
	public ModelAndView TaskLog(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskLog/TaskLogList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskLog
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskLogEntity TaskLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskLogEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskLog, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskLogService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务日志删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskLog
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskLogEntity TaskLog, HttpServletRequest request) {
		message = "任务日志删除成功";
		try{
			taskLogService.delete(TaskLog.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务日志
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
			taskLogService.batchDelete(ids);
			message="删除成功";
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
	@RequestMapping(params = "TaskLogEdit")
	public ModelAndView TaskLogEdit(TaskLogEntity TaskLog,Model model) {
		if (StringUtil.isNotEmpty(TaskLog.getId())) {
			TaskLog = taskLogService.get(TaskLog.getId());
			model.addAttribute("TaskLog", TaskLog);
		}
		return new ModelAndView("platform/organization/TaskLog/TaskLogEdit");
	}
	
	/**
	 * 新增或修改任务日志
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskLog
	 * @return
	 */
	@RequestMapping(params = "saveTaskLog")
	@ResponseBody
	public AjaxJson saveTaskLog(TaskLogEntity TaskLog) {
		try {
			if (StringUtil.isNotEmpty(TaskLog.getId())) {
				message = "任务日志更新成功";
				taskLogService.update(TaskLog);
			} else {
				message = "任务日志新增成功";
				taskLogService.save(TaskLog);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskLogService(TaskLogService taskLogService) {
		this.taskLogService = taskLogService;
	}
	
}
