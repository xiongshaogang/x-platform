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
import com.xplatform.base.workflow.task.entity.TaskSignDataEntity;
import com.xplatform.base.workflow.task.service.TaskSignDataService;

/**
 * 
 * description : 任务会签投票管理controller
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
@RequestMapping("/taskSignDataController")
public class TaskSignDataController extends BaseController {
	
	@Resource
	private TaskSignDataService taskSignDataService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 任务会签投票管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "TaskSignData")
	public ModelAndView TaskSignData(HttpServletRequest request) {
		return new ModelAndView("platform/organization/TaskSignData/TaskSignDataList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param TaskSignData
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(TaskSignDataEntity TaskSignData,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(TaskSignDataEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, TaskSignData, request.getParameterMap());
		try{
		//自定义追加查询条件
		}catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.taskSignDataService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 任务会签投票删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param TaskSignData
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(TaskSignDataEntity TaskSignData, HttpServletRequest request) {
		message = "任务会签投票删除成功";
		try{
			taskSignDataService.delete(TaskSignData.getId());
		}catch(Exception e){
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除任务会签投票
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
			taskSignDataService.batchDelete(ids);
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
	@RequestMapping(params = "TaskSignDataEdit")
	public ModelAndView TaskSignDataEdit(TaskSignDataEntity TaskSignData,Model model) {
		if (StringUtil.isNotEmpty(TaskSignData.getId())) {
			TaskSignData = taskSignDataService.get(TaskSignData.getId());
			model.addAttribute("TaskSignData", TaskSignData);
		}
		return new ModelAndView("platform/organization/TaskSignData/TaskSignDataEdit");
	}
	
	/**
	 * 新增或修改任务会签投票
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param TaskSignData
	 * @return
	 */
	@RequestMapping(params = "saveTaskSignData")
	@ResponseBody
	public AjaxJson saveTaskSignData(TaskSignDataEntity TaskSignData) {
		try {
			if (StringUtil.isNotEmpty(TaskSignData.getId())) {
				message = "任务会签投票更新成功";
				taskSignDataService.update(TaskSignData);
			} else {
				message = "任务会签投票新增成功";
				taskSignDataService.save(TaskSignData);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	
	
	public void setTaskSignDataService(TaskSignDataService taskSignDataService) {
		this.taskSignDataService = taskSignDataService;
	}
	
}
