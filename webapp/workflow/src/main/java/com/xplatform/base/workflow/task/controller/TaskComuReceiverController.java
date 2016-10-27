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
import com.xplatform.base.workflow.task.entity.TaskComuReceiverEntity;
import com.xplatform.base.workflow.task.service.TaskComuReceiverService;

/**
 * 
 * description : 任务沟通接收管理controller
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
@RequestMapping("/taskComuReceiverController")
public class TaskComuReceiverController extends BaseController {
	
	@Resource
	private TaskComuReceiverService taskComuReceiverService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务沟通接收管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskComuReceiver")
	public ModelAndView TaskComuReceiver(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskComuReceiver/TaskComuReceiverList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskComuReceiver
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskComuReceiverEntity TaskComuReceiver,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskComuReceiverEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskComuReceiver, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskComuReceiverService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务沟通接收删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskComuReceiver
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskComuReceiverEntity TaskComuReceiver, HttpServletRequest request) {
		message = "任务沟通接收删除成功";
		try{
			taskComuReceiverService.delete(TaskComuReceiver.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务沟通接收
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
			taskComuReceiverService.batchDelete(ids);
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
	@RequestMapping(params = "TaskComuReceiverEdit")
	public ModelAndView TaskComuReceiverEdit(TaskComuReceiverEntity TaskComuReceiver,Model model) {
		if (StringUtil.isNotEmpty(TaskComuReceiver.getId())) {
			TaskComuReceiver = taskComuReceiverService.get(TaskComuReceiver.getId());
			model.addAttribute("TaskComuReceiver", TaskComuReceiver);
		}
		return new ModelAndView("platform/organization/TaskComuReceiver/TaskComuReceiverEdit");
	}
	
	/**
	 * 新增或修改任务沟通接收
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskComuReceiver
	 * @return
	 */
	@RequestMapping(params = "saveTaskComuReceiver")
	@ResponseBody
	public AjaxJson saveTaskComuReceiver(TaskComuReceiverEntity TaskComuReceiver) {
		try {
			if (StringUtil.isNotEmpty(TaskComuReceiver.getId())) {
				message = "任务沟通接收更新成功";
				taskComuReceiverService.update(TaskComuReceiver);
			} else {
				message = "任务沟通接收新增成功";
				taskComuReceiverService.save(TaskComuReceiver);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskComuReceiverService(TaskComuReceiverService taskComuReceiverService) {
		this.taskComuReceiverService = taskComuReceiverService;
	}
	
}
