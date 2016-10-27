package com.xplatform.base.develop.codeconfig.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.develop.codeconfig.entity.GenerateFieldEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateConfigService;
import com.xplatform.base.develop.codeconfig.service.GenerateFieldService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * @Title: Controller
 * @Description: 页面模型种类表
 * @author onlineGenerator
 * @date 2014-05-12 20:09:32
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/generateFieldController")
public class GenerateFieldController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateFieldController.class);
	@Resource
	private GenerateFieldService generateFieldService;
	private String message;


	/**
	 * 页面模型种类表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goFormTypeEntityList")
	public ModelAndView goFormTypeEntityList(HttpServletRequest request) {
		return new ModelAndView("develop/formtype/formtype_list");
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
	public void datagrid(HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(GenerateFieldEntity.class, dataGrid);
		List<GenerateFieldEntity> formFieldEntityList = new ArrayList<GenerateFieldEntity>();
		// 查询条件组装器
		String formEntityID = request.getParameter("formEntityID");

		if (StringUtil.isNotEmpty(formEntityID)) {
			cq.eq("formEntityId.id", formEntityID);
		}
		HqlGenerateUtil.installHql(cq, new GenerateFieldEntity(),
				request.getParameterMap());
		cq.add();
		this.generateFieldService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月21日 下午9:10:06
	 * @Decription 表单保存方法
	 *
	 * @param formTypeEntity
	 * @param request
	 * @return
	 * @throws Exception 
	 */

	@RequestMapping(params = "doSaveOrUpdate")
	@ResponseBody
	public AjaxJson doSaveOrUpdate(GenerateFieldEntity formTypeEntity,
			HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		message = "保存成功";
		generateFieldService.saveOrUpdateFromPage(formTypeEntity);
		j.setMsg(message);
		return j;
	}

	/**
	 * 页面模型种类表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goFieldList")
	public ModelAndView goFieldList(HttpServletRequest request,
			GenerateFieldEntity formFieldEntity, Model model, HttpServletRequest req) {
		String formTypeID = request.getParameter("formTypeID");
		model.addAttribute("formTypeID", formTypeID);
		return new ModelAndView("develop/generate/field/generateFieldList");
	}

	@RequestMapping(params = "goFormFieldUpdate")
	public ModelAndView goFormFieldUpdate(HttpServletRequest request,
			Model model) {
		String formFieldId = request.getParameter("formFieldId");
		GenerateFieldEntity formFieldEntity = new GenerateFieldEntity();
		if (StringUtil.isNotEmpty(formFieldId)) {
			formFieldEntity = generateFieldService.getEntity(GenerateFieldEntity.class,formFieldId);
		}
		model.addAttribute(formFieldEntity);
		return new ModelAndView("develop/generate/field/generateFieldEdit");
	}
}
