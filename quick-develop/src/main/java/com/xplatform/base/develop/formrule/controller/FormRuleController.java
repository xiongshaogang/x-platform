package com.xplatform.base.develop.formrule.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.develop.formrule.entity.FormRuleEntity;
import com.xplatform.base.develop.formrule.service.FormRuleService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**   
 * @Title: Controller
 * @Description: 表单校验规则
 * @author onlineGenerator
 * @date 2014-05-15 17:19:29
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/formRuleController")
public class FormRuleController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FormRuleController.class);

	@Resource
	private FormRuleService formRuleService;
	
	public void setFormRuleService(FormRuleService formRuleService) {
		this.formRuleService = formRuleService;
	}

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 表单校验规则列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "formRule")
	public ModelAndView formRule(HttpServletRequest request) {
		return new ModelAndView("develop/formrule/formRuleList");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(FormRuleEntity formRule,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FormRuleEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, formRule, request.getParameterMap());
		cq.add();
		this.formRuleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}
	
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(FormRuleEntity formRule, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		if(StringUtil.isNotEmpty(formRule.getId())){
			formRuleService.update(formRule);
			message = "表单校验规则修改成功";
		}else{
			formRuleService.save(formRule);
			message = "表单校验规则添加成功";
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 批量删除表单校验规则
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(String ids,HttpServletRequest request){
		AjaxJson j = new AjaxJson();
		message = "表单校验规则删除成功";
		try{
			for(String id:ids.split(",")){
				formRuleService.delete(id);
			}
		}catch(Exception e){
			message = "表单校验规则删除失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 表单校验规则编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "editPage")
	public ModelAndView editPage(FormRuleEntity formRule, HttpServletRequest req) {
		if (StringUtil.isNotEmpty(formRule.getId())) {
			formRule = formRuleService.getEntity(FormRuleEntity.class, formRule.getId());
			req.setAttribute("formRulePage", formRule);
		}
		return new ModelAndView("develop/formrule/formRule");
	}
	
	
	/**
	 * 获取规则列表数据
	 * 
	 * @return
	 */
	@RequestMapping(params = "getAllRule")
	@ResponseBody
	public List<FormRuleEntity> getAllRule(){
		List<FormRuleEntity> list = this.formRuleService.queryAll();
		return list;
	}
}
