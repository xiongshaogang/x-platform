package com.xplatform.base.system.flowform.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.datatable.SortDirection;
import com.xplatform.base.system.flowform.entity.FlowFieldEntity;
import com.xplatform.base.system.flowform.entity.FlowTableEntity;
import com.xplatform.base.system.flowform.service.FlowFieldService;
import com.xplatform.base.system.flowform.service.FlowTableService;


/**   
 * @Title: Controller
 * @Description: 
 * @author onlineGenerator
 * @date 2015-01-04 18:00:42
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/flowFieldController")
public class FlowFieldController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FlowFieldController.class);

	@Resource
	private FlowFieldService flowFieldService;
	
	@Resource
	private FlowTableService flowTableService ;
	
	public void setFlowFieldService(FlowFieldService flowFieldService) {
		this.flowFieldService = flowFieldService;
	}
	
	public void setFlowTableService(FlowTableService flowTableService) {
		this.flowTableService = flowTableService;
	}

	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "flowField")
	public ModelAndView flowField(HttpServletRequest request) {
		return new ModelAndView("platform/system/flowform/flowfield/flowFieldList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param entity
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(FlowFieldEntity flowField,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FlowFieldEntity.class, dataGrid);
		dataGrid.setOrder(SortDirection.asc);
		String flowTable_id = request.getParameter("flowTable_id");
		if(StringUtil.isEmpty(flowTable_id)){
		flowField.setId("-1");
		}else{
		FlowTableEntity flowTable = new FlowTableEntity();
		flowTable.setId(flowTable_id);
		flowField.setTable(flowTable);
		}
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, flowField, request.getParameterMap());
		cq.add();
		this.flowFieldService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(FlowFieldEntity flowField, HttpServletRequest request) {
		message = "删除成功";
		try{
			flowFieldService.delete(flowField.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
			flowFieldService.batchDelete(ids);
			message = "删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param flowField
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "flowFieldEdit")
	public ModelAndView flowFieldEdit(FlowFieldEntity flowField,HttpServletRequest request) {
		if (StringUtil.isNotEmpty(flowField.getId())) {
			flowField = flowFieldService.get(flowField.getId());
		}
		return new ModelAndView("platform/system/flowform/flowfield/flowFieldEdit").addObject("flowField", flowField);
	}
	


	/**
	 * 新增或修改
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "saveFlowField")
	@ResponseBody
	public AjaxJson saveFlowField(FlowFieldEntity flowField) {
		try {
			flowField.getTable();
			if(StringUtil.isEmpty(flowField.getIsrequired())){
				flowField.setIsrequired("0");
			}
			if(StringUtil.isEmpty(flowField.getIslist())){
				flowField.setIslist("0");
			}
			if(StringUtil.isEmpty(flowField.getIsquery())){
				flowField.setIsquery("0");
			}
			if(StringUtil.isEmpty(flowField.getIsflowvar())){
				flowField.setIsflowvar("0");
			}
			if(flowField.getPointLength() == null){
				flowField.setPointLength(0);
			}
			if (StringUtil.isNotEmpty(flowField.getId())) {
				message = "更新成功";
				flowFieldService.update(flowField);
			} else {
				message = "新增成功";
				flowFieldService.save(flowField);
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "操作失败";
		}
		result.setMsg(message);
		return result;
	}
}
