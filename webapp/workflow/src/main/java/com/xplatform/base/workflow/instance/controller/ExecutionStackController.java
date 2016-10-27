package com.xplatform.base.workflow.instance.controller;
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
import com.xplatform.base.workflow.instance.entity.ExecutionStackEntity;
import com.xplatform.base.workflow.instance.service.ExecutionStackService;

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
@RequestMapping("/executionStackController")
public class ExecutionStackController extends BaseController {
	
	@Resource
	private ExecutionStackService executionStackService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程实例管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "ExecutionStack")
	public ModelAndView ExecutionStack(HttpServletRequest request) {
		return new ModelAndView("platform/organization/ExecutionStack/ExecutionStackList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param ExecutionStack
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ExecutionStackEntity ExecutionStack,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ExecutionStackEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ExecutionStack, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.executionStackService.getDataGridReturn(cq, true);
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
	 * @param ExecutionStack
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ExecutionStackEntity ExecutionStack, HttpServletRequest request) {
		message = "流程实例删除成功";
		try{
			executionStackService.delete(ExecutionStack.getId());
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
			executionStackService.batchDelete(ids);
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
	@RequestMapping(params = "ExecutionStackEdit")
	public ModelAndView ExecutionStackEdit(ExecutionStackEntity ExecutionStack,Model model) {
		if (StringUtil.isNotEmpty(ExecutionStack.getId())) {
			ExecutionStack = executionStackService.get(ExecutionStack.getId());
			model.addAttribute("ExecutionStack", ExecutionStack);
		}
		return new ModelAndView("platform/organization/ExecutionStack/ExecutionStackEdit");
	}
	
	/**
	 * 新增或修改流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ExecutionStack
	 * @return
	 */
	@RequestMapping(params = "saveExecutionStack")
	@ResponseBody
	public AjaxJson saveExecutionStack(ExecutionStackEntity ExecutionStack) {
		try {
			if (StringUtil.isNotEmpty(ExecutionStack.getId())) {
				message = "流程实例更新成功";
				executionStackService.update(ExecutionStack);
			} else {
				message = "流程实例新增成功";
				executionStackService.save(ExecutionStack);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "ExecutionStackSel")
	public ModelAndView ExecutionStackSel(HttpServletRequest request) {
		return new ModelAndView("common/ExecutionStackListSel");
	}
	
	

	public void setExecutionStackService(ExecutionStackService executionStackService) {
		this.executionStackService = executionStackService;
	}
	
	
}
