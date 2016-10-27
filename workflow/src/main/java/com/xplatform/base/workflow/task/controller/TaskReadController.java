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
import com.xplatform.base.workflow.task.entity.TaskReadEntity;
import com.xplatform.base.workflow.task.service.TaskReadService;

/**
 * 
 * description : 任务已读管理controller
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
@RequestMapping("/TaskReadController")
public class TaskReadController extends BaseController {
	
	@Resource
	private TaskReadService taskReadService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务已读管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskRead")
	public ModelAndView TaskRead(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskRead/TaskReadList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskRead
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskReadEntity TaskRead,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskReadEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskRead, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskReadService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务已读删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskRead
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskReadEntity TaskRead, HttpServletRequest request) {
		message = "任务已读删除成功";
		try{
			taskReadService.delete(TaskRead.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务已读
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
			taskReadService.batchDelete(ids);
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
	@RequestMapping(params = "TaskReadEdit")
	public ModelAndView TaskReadEdit(TaskReadEntity TaskRead,Model model) {
		if (StringUtil.isNotEmpty(TaskRead.getId())) {
			TaskRead = taskReadService.get(TaskRead.getId());
			model.addAttribute("TaskRead", TaskRead);
		}
		return new ModelAndView("platform/organization/TaskRead/TaskReadEdit");
	}
	
	/**
	 * 新增或修改任务已读
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskRead
	 * @return
	 */
	@RequestMapping(params = "saveTaskRead")
	@ResponseBody
	public AjaxJson saveTaskRead(TaskReadEntity TaskRead) {
		try {
			if (StringUtil.isNotEmpty(TaskRead.getId())) {
				message = "任务已读更新成功";
				taskReadService.update(TaskRead);
			} else {
				message = "任务已读新增成功";
				taskReadService.save(TaskRead);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskReadService(TaskReadService taskReadService) {
		this.taskReadService = taskReadService;
	}
	
}
