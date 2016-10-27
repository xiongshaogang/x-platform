package com.xplatform.base.workflow.task.controller;
import java.util.ArrayList;
import java.util.List;

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
import com.xplatform.base.workflow.task.entity.TaskNodeStatusEntity;
import com.xplatform.base.workflow.task.service.TaskNodeStatusService;

/**
 * 
 * description : 流程实例管理controller
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
@RequestMapping("/processInsStatusController")
public class TaskNodeStatustController extends BaseController {
	
	@Resource
	private TaskNodeStatusService processInsStatusService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程实例管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "ProcessInsStatus")
	public ModelAndView ProcessInsStatus(HttpServletRequest request) {
		return new ModelAndView("platform/organization/ProcessInsStatus/ProcessInsStatusList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param ProcessInsStatus
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskNodeStatusEntity ProcessInsStatus,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskNodeStatusEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ProcessInsStatus, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.processInsStatusService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 流程实例删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param ProcessInsStatus
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskNodeStatusEntity ProcessInsStatus, HttpServletRequest request) {
		message = "流程实例删除成功";
		try{
			processInsStatusService.delete(ProcessInsStatus.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除流程实例
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
			processInsStatusService.batchDelete(ids);
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
	@RequestMapping(params = "ProcessInsStatusEdit")
	public ModelAndView ProcessInsStatusEdit(TaskNodeStatusEntity ProcessInsStatus,Model model) {
		if (StringUtil.isNotEmpty(ProcessInsStatus.getId())) {
			ProcessInsStatus = processInsStatusService.get(ProcessInsStatus.getId());
			model.addAttribute("ProcessInsStatus", ProcessInsStatus);
		}
		return new ModelAndView("platform/organization/ProcessInsStatus/ProcessInsStatusEdit");
	}
	
	/**
	 * 新增或修改流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ProcessInsStatus
	 * @return
	 */
	@RequestMapping(params = "saveProcessInsStatus")
	@ResponseBody
	public AjaxJson saveProcessInsStatus(TaskNodeStatusEntity ProcessInsStatus) {
		try {
			if (StringUtil.isNotEmpty(ProcessInsStatus.getId())) {
				message = "流程实例更新成功";
				processInsStatusService.update(ProcessInsStatus);
			} else {
				message = "流程实例新增成功";
				processInsStatusService.save(ProcessInsStatus);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "ProcessInsStatusSel")
	public ModelAndView ProcessInsStatusSel(HttpServletRequest request) {
		return new ModelAndView("common/ProcessInsStatusListSel");
	}
	
	public void setProcessInsStatusService(TaskNodeStatusService processInsStatusService) {
		this.processInsStatusService = processInsStatusService;
	}
	
	
}
