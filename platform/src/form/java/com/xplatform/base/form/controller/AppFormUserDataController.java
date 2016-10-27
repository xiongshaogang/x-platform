package com.xplatform.base.form.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormUser;
import com.xplatform.base.form.entity.AppFormUserData;
import com.xplatform.base.form.entity.FlowFormEntity;
import com.xplatform.base.form.service.AppFormUserDataService;
import com.xplatform.base.form.service.AppFormUserService;
import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * 
 * description : 字典类型管理controller
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
@RequestMapping("/appFormUserDataController")
public class AppFormUserDataController extends BaseController {
	
	@Resource
	private AppFormUserDataService appFormUserDataService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appForm")
	public ModelAndView appForm(HttpServletRequest request) {
		return new ModelAndView("main/home/form/formHome");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param AppForm
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(AppFormUserData appFormUserData,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FlowFormEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, appFormUserData, request.getParameterMap());
		cq.add();
		this.appFormUserDataService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 字典类型删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param AppForm
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(AppFormUserData appFormUserData, HttpServletRequest request) {
		String ids = request.getParameter("ids");
		appFormUserDataService.deleteEntityByIds(ids);
		message = "字典类型删除成功";
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 保存或删除from中用户id
	 * @author lixt
	 * @createtime 2015年11月03日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "saveOrDeleteAppForm")
	@ResponseBody
	public AjaxJson saveOrDeleteAppForm(AppFormUserData appFormUserData, HttpServletRequest request) throws BusinessException {
	/*	String formId = request.getParameter("formId");
		String userIds = request.getParameter("userIds");
		
		if(StringUtil.isEmpty(formId)){
			result.setMsg("参数传递有误");
			result.setSuccess(false);
		}else{
			result = this.appFormUserDataService.saveOrDeleteAppFormData(formId, userIds);
			if(result.isSuccess()){
				List<AppFormUserData> list = this.appFormUserDataService.getList();
				result.setObj(list);
			}
		}*/
		return result;
	}

	/**
	 * 保存或删除from中用户id
	 * @author lixt
	 * @createtime 2015年11月03日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "queryAppFormData")
	@ResponseBody
	public AjaxJson queryAppForm(AppFormUserData appFormUserData, HttpServletRequest request) throws BusinessException {
	
		List<AppFormUserData> list = this.appFormUserDataService.getList();
		result.setObj(list);
		result.setMsg("查询成功");
		result.setSuccess(true);
		
		return result;
	}
}
