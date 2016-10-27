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
import com.xplatform.base.workflow.instance.entity.ProcessInstHistoryEntity;
import com.xplatform.base.workflow.instance.service.ProcessInstHistoryService;

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
@RequestMapping("/processInstHistoryController")
public class ProcessInstHistoryController extends BaseController {
	
	@Resource
	private ProcessInstHistoryService processInsHistoryService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 流程实例管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "processInstHistory")
	public ModelAndView ProcessInstHistory(HttpServletRequest request) {
		return new ModelAndView("workflow/processInstance/processInstHistoryList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param ProcessInsHistory
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ProcessInstHistoryEntity ProcessInsHistory,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ProcessInstHistoryEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ProcessInsHistory, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.processInsHistoryService.getDataGridReturn(cq, true);
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
	 * @param ProcessInsHistory
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ProcessInstHistoryEntity ProcessInsHistory, HttpServletRequest request) {
		message = "流程实例删除成功";
		try{
			processInsHistoryService.delete(ProcessInsHistory.getId());
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
			processInsHistoryService.batchDelete(ids);
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
	@RequestMapping(params = "ProcessInsHistoryEdit")
	public ModelAndView ProcessInsHistoryEdit(ProcessInstHistoryEntity ProcessInsHistory,Model model) {
		if (StringUtil.isNotEmpty(ProcessInsHistory.getId())) {
			ProcessInsHistory = processInsHistoryService.get(ProcessInsHistory.getId());
			model.addAttribute("ProcessInsHistory", ProcessInsHistory);
		}
		return new ModelAndView("platform/organization/ProcessInsHistory/ProcessInsHistoryEdit");
	}
	
	/**
	 * 新增或修改流程实例
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param ProcessInsHistory
	 * @return
	 */
	@RequestMapping(params = "saveProcessInsHistory")
	@ResponseBody
	public AjaxJson saveProcessInsHistory(ProcessInstHistoryEntity ProcessInsHistory) {
		try {
			if (StringUtil.isNotEmpty(ProcessInsHistory.getId())) {
				message = "流程实例更新成功";
				processInsHistoryService.update(ProcessInsHistory);
			} else {
				message = "流程实例新增成功";
				processInsHistoryService.save(ProcessInsHistory);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "ProcessInsHistorySel")
	public ModelAndView ProcessInsHistorySel(HttpServletRequest request) {
		return new ModelAndView("common/ProcessInsHistoryListSel");
	}
	
	public void setProcessInsHistoryService(ProcessInstHistoryService processInsHistoryService) {
		this.processInsHistoryService = processInsHistoryService;
	}
	
	
}
