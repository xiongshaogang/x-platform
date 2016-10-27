package com.xplatform.base.develop.codeconfig.controller;

import java.io.IOException;
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

import com.xplatform.base.develop.codeconfig.entity.GenerateConfigEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateConfigService;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.constant.Globals;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.MyBeanUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.UUIDGenerator;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * @Title: Controller
 * @Description: 模板表单实体配置
 * @author onlineGenerator
 * @date 2014-05-15 11:05:15
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/generateConfigController")
public class GenerateConfigController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateConfigController.class);

	@Resource
	private GenerateConfigService fenetateConfigService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
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
	public void datagrid(GenerateConfigEntity tBaseFormEntity,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(GenerateConfigEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, tBaseFormEntity,
				request.getParameterMap());
		cq.add();
		this.fenetateConfigService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除模板表单实体配置
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(GenerateConfigEntity tBaseFormEntity,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		tBaseFormEntity = fenetateConfigService.getEntity(GenerateConfigEntity.class,
				tBaseFormEntity.getId());
		message = "模板表单实体配置删除成功";
		fenetateConfigService.delete(tBaseFormEntity);
		
		j.setMsg(message);
		return j;
	}

	/**
	 * 批量删除模板表单实体配置
	 * 
	 * @return
	 */
	@RequestMapping(params = "doBatchDel")
	@ResponseBody
	public AjaxJson doBatchDel(String ids, HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "模板表单实体配置删除成功";
		try {
			for (String id : ids.split(",")) {
				GenerateConfigEntity tBaseFormEntity = fenetateConfigService.getEntity(
						GenerateConfigEntity.class, id);
				fenetateConfigService.delete(tBaseFormEntity);
			}
		} catch (Exception e) {
			message = "模板表单实体配置删除失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新或添加添加模板表单实体配置
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "addorupdate")
	@ResponseBody
	public AjaxJson doAdd(GenerateConfigEntity tBaseFormEntity,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "数据保存成功";
		if(StringUtils.isNotEmpty(tBaseFormEntity.getPack())){
			tBaseFormEntity.setPack(tBaseFormEntity.getPack().toLowerCase());
		}
		try {
			if (StringUtil.isNotEmpty(tBaseFormEntity.getId())) {
				GenerateConfigEntity t = fenetateConfigService.get(
						GenerateConfigEntity.class, tBaseFormEntity.getId());

				MyBeanUtils.copyBeanNotNull2Bean(tBaseFormEntity, t);
				fenetateConfigService.saveOrUpdate(t);

			} else {
				fenetateConfigService.save(tBaseFormEntity);
			}
			// 如果表单实体表下还没有表单字段表记录,则说明首次生成,要去读取生成实体表下的字段表
			if (StringUtil.isNotEmpty(tBaseFormEntity.getId())) {
				// 如果字段表里已有记录了,就进行删除
				if (fenetateConfigService
						.isExsitsFormFieldEntity(tBaseFormEntity.getId())) {
					fenetateConfigService
							.deleteFieldsByEntityId(tBaseFormEntity.getId());
				}
				String cgEntityId = fenetateConfigService
						.findCgEntityIdByFormEntityId(tBaseFormEntity.getId());
				List<MetaDataFieldEntity> cgFormFieldEntityList = fenetateConfigService
						.findCgFormFieldEntityListByEntityId(cgEntityId);
				fenetateConfigService.saveInitFormFieldEntitys(
						cgFormFieldEntityList, tBaseFormEntity.getId());

			}
		} catch (Exception e) {
			message = "数据保存失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setObj(tBaseFormEntity.getId());
		j.setMsg(message);
		return j;
	}

	/**
	 * @author by
	 * @createtime 2014年5月14日 上午14:25:41
	 * @Decription 取得模板实体类型详细信息
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params = "getBaseFormEntity")
	@ResponseBody
	public String getBaseFormEntity(HttpServletRequest request,
			HttpServletResponse response) {
		String con_Id = request.getParameter("con_Id");
		String type_id = request.getParameter("type_id");
		GenerateConfigEntity baseFormEntity = fenetateConfigService
				.getBaseFormEntityByConId(con_Id, type_id);
		if (baseFormEntity != null) {
			baseFormEntity.setFormTypeEntity(null);
		}
		return JSONHelper.toJSONString(baseFormEntity);
	}

	

	

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月15日 上午11:07:41
	 * @Decription 异步方式得到tree的单个子节点
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(params = "getFormEntityTree")
	public void getFormEntityTree(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// 当前加载的父节点的ID
		String formTypeID = request.getParameter("formTypeID");
		List<GenerateConfigEntity> baseFormEntityList = fenetateConfigService
				.findBaseFormEntityListByFormTypeID(formTypeID);
		String json = "[";
		for (int i = 0; i < baseFormEntityList.size(); i++) {
			String entityStr = "{\"id\":\"" + baseFormEntityList.get(i).getId()
					+ "\",\"text\":\""
					+ baseFormEntityList.get(i).getEntityClass() 
					+ "\",\"iconCls\":\"awsm-icon-leaf green\""+ "}";
			if (i < baseFormEntityList.size() - 1) {
				entityStr += ",";
			}
			json += entityStr;
		}
		json += "]";
		response.setContentType("text/html;charset=utf-8");

		try {
			response.getWriter().write(json);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
