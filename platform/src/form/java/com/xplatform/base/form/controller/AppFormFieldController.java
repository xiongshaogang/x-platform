package com.xplatform.base.form.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormField;
import com.xplatform.base.form.service.AppFormFieldService;
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
@RequestMapping("/appFormFieldController")
public class AppFormFieldController extends BaseController {
	
	@Resource
	private AppFormFieldService appFormFieldService;
	
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
		String id = request.getParameter("id");
		if(StringUtil.isNotEmpty(id)){
			request.setAttribute("formId", id);
		}
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
	public void datagrid(AppFormField appForm,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AppFormField.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, appForm, request.getParameterMap());
		cq.add();
		this.appFormFieldService.getDataGridReturn(cq, true);
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
	public AjaxJson delete(AppFormField AppForm, HttpServletRequest request) {
		String ids = request.getParameter("ids");
		appFormFieldService.deleteEntityByIds(ids);
		message = "字典类型删除成功";
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "appFormEdit")
	public ModelAndView appFormEdit(AppFormField appForm,Model model) {
		if (StringUtil.isNotEmpty(appForm.getId())) {
			appForm = appFormFieldService.get(appForm.getId());
			model.addAttribute("AppForm", appForm);
		}
		return new ModelAndView("platform/system/dict/AppFormEdit");
	}
	
	/**
	 * 新增或修改字典类型
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "saveAppForm")
	@ResponseBody
	public AjaxJson saveAppForm(AppFormField appForm) throws BusinessException {
		if (StringUtil.isNotEmpty(appForm.getId())) {
			message = "字典类型更新成功";
			try {
				appFormFieldService.update(appForm, appForm.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "字典类型新增成功";
			appFormFieldService.save(appForm);
		}
		result.setMsg(message);
		return result;
	}

	
	
}
