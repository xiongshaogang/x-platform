package com.xplatform.base.develop.codeconfig.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.develop.codeconfig.entity.ConBaseFormEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateEntity;
import com.xplatform.base.develop.codeconfig.entity.GenerateTypeEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateService;
import com.xplatform.base.develop.codeconfig.service.GenerateTypeService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
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
@RequestMapping("/generateController")
public class GenerateController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(GenerateController.class);

	@Resource
	private GenerateService generateService;
	@Resource
	private GenerateTypeService generateTypeService;
	
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 页面模型种类表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "generate")
	public ModelAndView generate(HttpServletRequest request) {
		return new ModelAndView("develop/generate/generateList");
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
	public void datagrid(GenerateEntity formTypeEntity,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(GenerateEntity.class, dataGrid);
		// 查询条件组装器
		String typeID = request.getParameter("typeID");
		if (typeID != null) {
			formTypeEntity.setTypeID(typeID);
		}
		HqlGenerateUtil.installHql(cq, formTypeEntity,request.getParameterMap());
		cq.add();
		this.generateService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除页面模型种类表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(GenerateEntity formTypeEntity,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		formTypeEntity = generateService.getEntity(GenerateEntity.class,
				formTypeEntity.getId());
		message = "页面模型种类表删除成功";
		try {
			generateService.delete(formTypeEntity);
		} catch (Exception e) {
			message = "页面模型种类表删除失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除页面模型种类表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "删除成功";
		for (String id : ids.split(",")) {
			GenerateEntity formTypeEntity = generateService.getEntity(
					GenerateEntity.class, id);
			generateService.delete(formTypeEntity);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加页面模型种类表
	 * 
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "doAddOrUpdate")
	@ResponseBody
	public AjaxJson doAddOrUpdate(GenerateEntity formTypeEntity,
			HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		message = "保存成功";
		// 为了兼容ID为空字符串("")时也要进行
		if (StringUtil.isEmpty(formTypeEntity.getId())){
		generateService.save(formTypeEntity);
		}else{
			GenerateEntity f = generateService.getEntity(
					GenerateEntity.class, formTypeEntity.getId());
			MyBeanUtils.copyBeanNotNull2Bean(formTypeEntity,f);
			generateService.update(f);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新页面模型种类表
	 * 
	 * @param ids
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(GenerateEntity formTypeEntity,
			HttpServletRequest request) throws Exception {
		AjaxJson j = new AjaxJson();
		message = "页面模型种类表更新成功";
		GenerateEntity t = generateService.get(GenerateEntity.class,
				formTypeEntity.getId());
		MyBeanUtils.copyBeanNotNull2Bean(formTypeEntity, t);
		generateService.saveOrUpdate(t);
		j.setMsg(message);
		return j;
	}

	/**
	 * 页面模型种类表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAddOrUpdate")
	public ModelAndView goAddOrUpdate(HttpServletRequest request,
			GenerateEntity formTypeEntity, Model model) {
		if (StringUtil.isNotEmpty(formTypeEntity.getId())) {
			formTypeEntity = generateService.getEntity(GenerateEntity.class,
					formTypeEntity.getId());
		} else {
			String typeID = request.getParameter("typeID");
			GenerateTypeEntity modelTypeEntity = generateTypeService.get(
					GenerateTypeEntity.class, typeID);
			formTypeEntity.setTypeID(typeID);
			formTypeEntity.setTypeName(modelTypeEntity.getName());
		}
		model.addAttribute("formTypeEntity", formTypeEntity);
		return new ModelAndView("develop/generate/generateEdit");
	}

	/**
	 * 页面模型种类表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(GenerateEntity formTypeEntity,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(formTypeEntity.getId())) {
			formTypeEntity = generateService.getEntity(GenerateEntity.class,
					formTypeEntity.getId());
			req.setAttribute("TBaseModelTypePage", formTypeEntity);
		}
		return new ModelAndView("develop/generate/generateEdit");
	}

	/**
	 * 模版表单实体配置页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "deployEntity")
	public ModelAndView deployEntity(HttpServletRequest request) {
		String model_id = request.getParameter("model_id");
		String type_id = request.getParameter("type_id");
		request.setAttribute("model_id", model_id);
		request.setAttribute("type_id", type_id);
		//取得模板实体联系表第一条记录（即进入页面树形第一个按钮）
		ConBaseFormEntity con = generateService.queryModelData(model_id).get(0);
		request.setAttribute("con_id", con.getId());
		request.setAttribute("type", con.getType());
		request.setAttribute("tableType", "tableType_"+con.getTable_type());
		if(StringUtils.equals(con.getBase_model_id(), "4")){
			return new ModelAndView("develop/generate/config/singleTable");			
		}else if(StringUtils.equals(con.getBase_model_id(), "5")){
			return new ModelAndView("develop/generate/config/onetomany");
		}else if(StringUtils.equals(con.getBase_model_id(), "6")){
			return new ModelAndView("develop/generate/config/singleOwerTree");
		}else if(StringUtils.equals(con.getBase_model_id(), "7")){
			return new ModelAndView("develop/generate/config/treeAndTable");
		}else if(StringUtils.equals(con.getBase_model_id(), "8")){
			return new ModelAndView("develop/generate/config/quoteTreeTable");
		}
		return new ModelAndView("develop/generate/baseformentity/base_form_entity");
	}
	
	
	/**
	 * @author by
	 * @createtime 2014年5月14日 上午14:25:41
	 * @Decription 异步方式得到该模板下需要填写的实体类型
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(params = "getTreeData")
	public ModelAndView getTreeData(HttpServletRequest request,
			HttpServletResponse response) {
		// 当前加载的父节点的ID 
		String model_id = request.getParameter("model_id");
		String type_id = request.getParameter("type_id");
		List<ConBaseFormEntity> conBaseFormEntity = generateService.queryModelData(model_id);
		StringBuilder sb = new StringBuilder();
		sb.append("<ul class=\"tree\" id=\"formType\" fit=\"false\" border=\"false\">");
		for(ConBaseFormEntity con : conBaseFormEntity){
			sb.append("<li class=\"formType-li\">");
			sb.append("<div class=\"tree-node\" onclick=\"selectTab('"+ con.getId() +"','"+ type_id +"','"+ con.getType() +"','tableType_"+con.getTable_type()+"')\" style=\"cursor: pointer;\">");
			sb.append("<span class=\"tree-indent\"></span>");
			sb.append("<span class=\"icon-color fa fa-file-text \"></span>");
			sb.append("<span class=\"tree-title\">"+con.getName()+"</span></div>");
			sb.append("</li>");
		}
		sb.append("</ul>");
		request.setAttribute("sb", sb);
		return new ModelAndView("develop/generate/generateTree");
	}

}
