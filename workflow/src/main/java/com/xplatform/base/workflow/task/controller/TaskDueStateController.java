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
import com.xplatform.base.workflow.task.entity.TaskDueStateEntity;
import com.xplatform.base.workflow.task.service.TaskDueStateService;

/**
 * 
 * description : 任务催办状态管理controller
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
@RequestMapping("/taskDueStateController")
public class TaskDueStateController extends BaseController {
	
	@Resource
	private TaskDueStateService taskDueStateService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务催办状态管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskDueState")
	public ModelAndView TaskDueState(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskDueState/TaskDueStateList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskDueState
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskDueStateEntity TaskDueState,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskDueStateEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskDueState, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskDueStateService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务催办状态删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskDueState
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskDueStateEntity TaskDueState, HttpServletRequest request) {
		message = "任务催办状态删除成功";
		try{
			taskDueStateService.delete(TaskDueState.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务催办状态
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
			taskDueStateService.batchDelete(ids);
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
	@RequestMapping(params = "TaskDueStateEdit")
	public ModelAndView TaskDueStateEdit(TaskDueStateEntity TaskDueState,Model model) {
		if (StringUtil.isNotEmpty(TaskDueState.getId())) {
			TaskDueState = taskDueStateService.get(TaskDueState.getId());
			model.addAttribute("TaskDueState", TaskDueState);
		}
		return new ModelAndView("platform/organization/TaskDueState/TaskDueStateEdit");
	}
	
	/**
	 * 新增或修改任务催办状态
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskDueState
	 * @return
	 */
	@RequestMapping(params = "saveTaskDueState")
	@ResponseBody
	public AjaxJson saveTaskDueState(TaskDueStateEntity TaskDueState) {
		try {
			if (StringUtil.isNotEmpty(TaskDueState.getId())) {
				message = "任务催办状态更新成功";
				taskDueStateService.update(TaskDueState);
			} else {
				message = "任务催办状态新增成功";
				taskDueStateService.save(TaskDueState);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskDueStateService(TaskDueStateService taskDueStateService) {
		this.taskDueStateService = taskDueStateService;
	}
	
}
