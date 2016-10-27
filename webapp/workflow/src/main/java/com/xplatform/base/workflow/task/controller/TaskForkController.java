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
import com.xplatform.base.workflow.task.entity.TaskForkEntity;
import com.xplatform.base.workflow.task.service.TaskForkService;

/**
 * 
 * description : 任务分发汇总管理controller
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
@RequestMapping("/TaskForkController")
public class TaskForkController extends BaseController {
	
	@Resource
	private TaskForkService taskForkService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务分发汇总管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskFork")
	public ModelAndView TaskFork(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskFork/TaskForkList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskFork
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskForkEntity TaskFork,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskForkEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskFork, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskForkService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务分发汇总删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskFork
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskForkEntity TaskFork, HttpServletRequest request) {
		message = "任务分发汇总删除成功";
		try{
			taskForkService.delete(TaskFork.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务分发汇总
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
			taskForkService.batchDelete(ids);
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
	@RequestMapping(params = "TaskForkEdit")
	public ModelAndView TaskForkEdit(TaskForkEntity TaskFork,Model model) {
		if (StringUtil.isNotEmpty(TaskFork.getId())) {
			TaskFork = taskForkService.get(TaskFork.getId());
			model.addAttribute("TaskFork", TaskFork);
		}
		return new ModelAndView("platform/organization/TaskFork/TaskForkEdit");
	}
	
	/**
	 * 新增或修改任务分发汇总
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskFork
	 * @return
	 */
	@RequestMapping(params = "saveTaskFork")
	@ResponseBody
	public AjaxJson saveTaskFork(TaskForkEntity TaskFork) {
		try {
			if (StringUtil.isNotEmpty(TaskFork.getId())) {
				message = "任务分发汇总更新成功";
				taskForkService.update(TaskFork);
			} else {
				message = "任务分发汇总新增成功";
				taskForkService.save(TaskFork);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskForkService(TaskForkService taskForkService) {
		this.taskForkService = taskForkService;
	}
	
}
