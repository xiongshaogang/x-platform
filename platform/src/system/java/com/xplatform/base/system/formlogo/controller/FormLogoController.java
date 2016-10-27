package com.xplatform.base.system.formlogo.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.formlogo.entity.FormLogoEntity;
import com.xplatform.base.system.formlogo.service.FormLogoService;

@Controller
@RequestMapping("/formLogoController")
public class FormLogoController extends BaseController {
	
	@Resource
	private FormLogoService formLogoService;

	@RequestMapping(params = "formLogoList")
	public ModelAndView formLogo(HttpServletRequest request) {
		return new ModelAndView("platform/system/formlogo/formLogoList");
	}
	
	@RequestMapping(params = "formLogoEdit")
	public ModelAndView commodityEdit(FormLogoEntity formLogo, HttpServletRequest request) throws BusinessException{
		FormLogoEntity formLogoEntity = null;
		if (StringUtil.isNotEmpty(formLogo.getId())) {
			formLogoEntity = this.formLogoService.getFormLogo(formLogo.getId());
		}else{
			formLogoEntity = new FormLogoEntity();
			formLogoEntity.setId(UUIDGenerator.generate());
		}
		request.setAttribute("formLogo", formLogoEntity);
		return new ModelAndView("platform/system/formlogo/formLogoEdit");
	}
	
	@RequestMapping(params = "datagrid")
	public void datagrid(FormLogoEntity formLogoEntity, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {

		CriteriaQuery cq = new CriteriaQuery(FormLogoEntity.class, dataGrid);
		HqlGenerateUtil.installHql(cq, formLogoEntity, request.getParameterMap());
		cq.add();
		this.formLogoService.getDataGridReturn(cq, true);

		TagUtil.datagrid(response, dataGrid);

	}
	
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(FormLogoEntity formLogoEntity, HttpServletRequest request) throws BusinessException{
		AjaxJson j = new AjaxJson();
		String message = "";
		if ("update".equals(request.getParameter("optFlag"))) {
			this.formLogoService.updateFormLogo(formLogoEntity);
			message = "修改成功";
		} else {
			this.formLogoService.saveFormLogo(formLogoEntity);
			message = "新增商品成功";
		}
		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(FormLogoEntity formLogoEntity) throws BusinessException{
		AjaxJson j = new AjaxJson();
		String message = "删除失败";
		this.formLogoService.deleteFormLogo(formLogoEntity.getId());
		message = "删除成功";
		j.setMsg(message);
		return j;
	}
	
	/**
	 * 产品批量删除
	 */
	@RequestMapping(params = "deleteBatch")
	@ResponseBody
	public AjaxJson deleteBatch(HttpServletRequest request) throws BusinessException{
		String ids = request.getParameter("ids");
		AjaxJson j = new AjaxJson();
		String message = "批量删除时失败";

		if (StringUtil.isNotBlank(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				FormLogoEntity formLogo = this.formLogoService.getFormLogo(id);
				if (formLogo != null) {
					delete(formLogo);
				}

			}
		}
		message = "批量删除成功";

		j.setMsg(message);
		return j;
	}
	
	@RequestMapping(params = "queryLogoList")
	@ResponseBody
	public AjaxJson queryLogoList(FormLogoEntity formLogoEntity) throws BusinessException{
		AjaxJson j = new AjaxJson();
		List<FormLogoEntity> list = this.formLogoService.queryList();
		j.setObj(list);
		return j;
	}
	
	
}
