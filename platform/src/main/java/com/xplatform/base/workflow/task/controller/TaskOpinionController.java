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
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.workflow.task.entity.TaskOpinionEntity;
import com.xplatform.base.workflow.task.service.TaskOpinionService;

/**
 * 
 * description : 审批意见管理controller
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
@RequestMapping("/taskOpinionController")
public class TaskOpinionController extends BaseController {
	
	@Resource
	private TaskOpinionService taskOpinionService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 审批意见管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskOpinion")
	public ModelAndView TaskOpinion(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskOpinion/TaskOpinionList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskOpinion
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskOpinionEntity TaskOpinion,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) throws BusinessException {
		CriteriaQuery cq = new CriteriaQuery(TaskOpinionEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskOpinion, request.getParameterMap());
		cq.add();
		this.taskOpinionService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 审批意见删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskOpinion
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskOpinionEntity TaskOpinion, HttpServletRequest request) {
		message = "审批意见删除成功";
		try{
			taskOpinionService.delete(TaskOpinion.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除审批意见
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
			taskOpinionService.batchDelete(ids);
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
	@RequestMapping(params = "TaskOpinionEdit")
	public ModelAndView TaskOpinionEdit(TaskOpinionEntity TaskOpinion,Model model) {
		if (StringUtil.isNotEmpty(TaskOpinion.getId())) {
			try {
				TaskOpinion = taskOpinionService.get(TaskOpinion.getId());
				model.addAttribute("TaskOpinion", TaskOpinion);
			} catch (BusinessException e) {
				
			}
			
		}
		return new ModelAndView("platform/organization/TaskOpinion/TaskOpinionEdit");
	}
	
	/**
	 * 新增或修改审批意见
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskOpinion
	 * @return
	 */
	@RequestMapping(params = "saveTaskOpinion")
	@ResponseBody
	public AjaxJson saveTaskOpinion(TaskOpinionEntity TaskOpinion) {
		try {
			if (StringUtil.isNotEmpty(TaskOpinion.getId())) {
				message = "审批意见更新成功";
				taskOpinionService.update(TaskOpinion);
			} else {
				message = "审批意见新增成功";
				taskOpinionService.save(TaskOpinion);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "TaskOpinionSel")
	public ModelAndView TaskOpinionSel(HttpServletRequest request) {
		return new ModelAndView("common/TaskOpinionListSel");
	}
	
	
	public void setTaskOpinionService(TaskOpinionService taskOpinionService) {
		this.taskOpinionService = taskOpinionService;
	}
	
}
