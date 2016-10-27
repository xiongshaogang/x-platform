package com.xplatform.base.develop.codeconfig.controller;

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

import com.xplatform.base.develop.codeconfig.entity.GenerateTypeEntity;
import com.xplatform.base.develop.codeconfig.service.GenerateTypeService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
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
@RequestMapping("/generateTypeController")
public class GenerateTypeController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(GenerateTypeController.class);

	@Resource
	private GenerateTypeService generateTypeService;

	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@RequestMapping(params = "build")
	public ModelAndView build() {
		return new ModelAndView("develop/modeltype/modelBuild");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年5月13日 上午11:07:41
	 * @Decription 异步方式得到tree的单个子节点
	 *
	 * @param request
	 * @param response
	 * @return
	 */

	@RequestMapping(params = "getAsynTree")
	public void getAsynTree(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		// 当前加载的父节点的ID
		String id = request.getParameter("id");
		List<GenerateTypeEntity> modelTypeEntity = generateTypeService
				.findChildrenByParentID(id);
		List<TreeNode> treeNodeList = generateTypeService
				.transformTreeNode(modelTypeEntity);
		TagUtil.tree(response, treeNodeList);

	}

	/**
	 * 页面模型种类表列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "TBaseModelType")
	public ModelAndView TBaseModelType(HttpServletRequest request) {
		return new ModelAndView("develop/modeltype/TBaseModelTypeList");
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
	public void datagrid(GenerateTypeEntity TBaseModelType,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(GenerateTypeEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, TBaseModelType,
				request.getParameterMap());
		cq.add();
		this.generateTypeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除页面模型种类表
	 * 
	 * @return
	 */
	@RequestMapping(params = "doDel")
	@ResponseBody
	public AjaxJson doDel(GenerateTypeEntity TBaseModelType,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		TBaseModelType = generateTypeService.getEntity(GenerateTypeEntity.class,
				TBaseModelType.getId());
		message = "页面模型种类表删除成功";
		generateTypeService.delete(TBaseModelType);
		/*generateTypeService.addLog(message, Globals.Log_Type_DEL,
				Globals.Log_Leavel_INFO);*/
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
		message = "页面模型种类表删除成功";
		try {
			for (String id : ids.split(",")) {
				GenerateTypeEntity TBaseModelType = generateTypeService.getEntity(
						GenerateTypeEntity.class, id);
				generateTypeService.delete(TBaseModelType);
				/*systemService.addLog(message, Globals.Log_Type_DEL,
						Globals.Log_Leavel_INFO);*/
			}
		} catch (Exception e) {
			message = "页面模型种类表删除失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 添加页面模型种类表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doAdd")
	@ResponseBody
	public AjaxJson doAdd(GenerateTypeEntity TBaseModelType,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "页面模型种类表添加成功";
		try {
			generateTypeService.save(TBaseModelType);
			/*systemService.addLog(message, Globals.Log_Type_INSERT,
					Globals.Log_Leavel_INFO);*/
		} catch (Exception e) {
			message = "页面模型种类表添加失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 更新页面模型种类表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "doUpdate")
	@ResponseBody
	public AjaxJson doUpdate(GenerateTypeEntity TBaseModelType,
			HttpServletRequest request) {
		AjaxJson j = new AjaxJson();
		message = "页面模型种类表更新成功";
		GenerateTypeEntity t = generateTypeService.get(GenerateTypeEntity.class,
				TBaseModelType.getId());
		try {
			MyBeanUtils.copyBeanNotNull2Bean(TBaseModelType, t);
			generateTypeService.saveOrUpdate(t);
			/*systemService.addLog(message, Globals.Log_Type_UPDATE,
					Globals.Log_Leavel_INFO);*/
		} catch (Exception e) {
			message = "页面模型种类表更新失败";
			ExceptionUtil.printStackTraceAndLogger(e);
		}
		j.setMsg(message);
		return j;
	}

	/**
	 * 页面模型种类表新增页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goAdd")
	public ModelAndView goAdd(GenerateTypeEntity TBaseModelType,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(TBaseModelType.getId())) {
			TBaseModelType = generateTypeService.getEntity(GenerateTypeEntity.class,
					TBaseModelType.getId());
			req.setAttribute("TBaseModelTypePage", TBaseModelType);
		}
		return new ModelAndView("develop/modeltype/TBaseModelType-add");
	}

	/**
	 * 页面模型种类表编辑页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "goUpdate")
	public ModelAndView goUpdate(GenerateTypeEntity TBaseModelType,
			HttpServletRequest req) {
		if (StringUtil.isNotEmpty(TBaseModelType.getId())) {
			TBaseModelType = generateTypeService.getEntity(GenerateTypeEntity.class,
					TBaseModelType.getId());
			req.setAttribute("TBaseModelTypePage", TBaseModelType);
		}
		return new ModelAndView("develop/modeltype/TBaseModelType-update");
	}
}
