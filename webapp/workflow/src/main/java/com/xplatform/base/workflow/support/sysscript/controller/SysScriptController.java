/**
 * Copyright (c) 2014
 *
 * Licensed under the UCG License, Version 1.0 (the "License");
 */
package com.xplatform.base.workflow.support.sysscript.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javassist.NotFoundException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.ReflectHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.platform.common.groovy.GroovyScriptEngine;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictValueService;
import com.xplatform.base.workflow.support.sysscript.entity.SysScriptEntity;
import com.xplatform.base.workflow.support.sysscript.service.SysScriptService;

/**
 * description :
 *
 * @version 1.0
 * @author xiaqiang
 * @createtime : 2014年7月1日 上午11:09:43
 * 
 * 修改历史:
 * 修改人                                          修改时间                                                     修改内容
 * --------------- ------------------- -----------------------------------
 * xiaqiang        2014年7月1日 上午11:09:43 
 *
*/
@Controller
@RequestMapping("/sysScriptController")
public class SysScriptController extends BaseController {

	private SysScriptService sysScriptService;

	private GroovyScriptEngine scriptEngine;

	private DictValueService dictValueService;

	@RequestMapping(params = "sysScriptList")
	public ModelAndView sysScriptList(HttpServletRequest request) {
		return new ModelAndView("workflow/support/sysScript/sysScriptList");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月5日 上午9:24:35
	 * @Decription 单表hibernate组装表格数据
	 *
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(SysScriptEntity sysScriptEntity, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(SysScriptEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, sysScriptEntity, request.getParameterMap());
		try {
			//自定义追加查询条件
		} catch (Exception e) {
			//throw new BusinessException(e.getMessage());
		}
		cq.add();
		try {
			this.sysScriptService.getDataGridReturn(cq, true);
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		TagUtil.datagrid(response, dataGrid);
	}

	@RequestMapping(params = "edit")
	public ModelAndView edit(HttpServletRequest request) {
		SysScriptEntity sysScript = new SysScriptEntity();
		String id = request.getParameter("id");

		if (StringUtil.isNotEmpty(id)) {
			sysScript = sysScriptService.get(id);
		}
		request.setAttribute("sysScript", sysScript);

		return new ModelAndView("workflow/support/sysScript/sysScriptEdit");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月7日 上午9:51:49
	 * @Decription 获得
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getClass")
	@ResponseBody
	public List<ComboBox> getClass(HttpServletRequest request) {
		List<String> classList = new ArrayList<String>();
		List<ComboBox> classComboboxList = new ArrayList<ComboBox>();
		try {
			Map<String, Object> instanceMap = scriptEngine.binding.propertyMap;
			for (Object proxyObj : instanceMap.values()) {
				Object realObj = ReflectHelper.getTargetObject(proxyObj, null);
				String className = realObj.getClass().getName();
				classList.add(className);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (String className : classList) {
			ComboBox combobx = new ComboBox();
			combobx.setText(className);
			combobx.setId(className);
			classComboboxList.add(combobx);
		}
		return classComboboxList;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月7日 上午11:31:14
	 * @Decription 获取类名对应的spring类实例名称
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getClassInsName")
	@ResponseBody
	public String getClassInsName(HttpServletRequest request) {
		String className = request.getParameter("className");
		Map<String, Object> instanceMap = scriptEngine.binding.propertyMap;
		for (String key : instanceMap.keySet()) {
			Object proxyObj = instanceMap.get(key);
			Object realObj = new Object();
			try {
				realObj = ReflectHelper.getTargetObject(proxyObj, null);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String tempClassName = realObj.getClass().getName();
			if (tempClassName.equals(className)) {
				return key;
			}
		}
		return null;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月7日 上午11:31:14
	 * @Decription 获取类名对应的spring类实例名称
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "getMethods")
	public void getMethods(HttpServletRequest request, HttpServletResponse response) {
		String q = request.getParameter("q");
		String className = request.getParameter("className");
		List<String> methodsList = new ArrayList<String>();
		JSONArray array = new JSONArray();
		try {
			array = sysScriptService.getMethods(className, q);
			response.setContentType("application/json;charset=UTF-8");
			response.setHeader("Pragma", "No-cache");
			response.setHeader("Cache-Control", "no-cache");
			response.setDateHeader("Expires", 0);
			response.getWriter().write(array.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} catch (BusinessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月5日 上午9:26:15
	 * @Decription 单记录删除
	 *
	 * @param MsgTemplate
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(SysScriptEntity sysScriptEntity, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			sysScriptService.delete(sysScriptEntity.getId());
		} catch (Exception e) {
			result.setMsg(e.getMessage());
		}
		result.setMsg("删除成功");
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月5日 上午9:12:54
	 * @Decription 批量删除脚本记录
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			String ids = request.getParameter("ids");
			sysScriptService.batchDelete(ids);
			result.setMsg("删除成功");
		} catch (Exception e) {
			result.setMsg(e.getMessage());
		}
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月5日 上午9:38:09
	 * @Decription
	 *
	 * @param MsgTemplate
	 * @return
	 */
	@RequestMapping(params = "save")
	@ResponseBody
	public AjaxJson save(SysScriptEntity sysScriptEntity) {
		AjaxJson result = new AjaxJson();
		String message;
		if ("pieceScript".equals(sysScriptEntity.getSourceDict())) {
			String classInsName = sysScriptEntity.getClassInsName();//类实例名
			String methodName = sysScriptEntity.getMethodName();//类来源方法
			String argumentsJson = sysScriptEntity.getArgument();//参数json
			String arguments = sysScriptService.getArgumentsString(argumentsJson);

			StringBuffer sb = new StringBuffer();
			sb.append("return ").append(classInsName).append(".").append(methodName).append("(").append(arguments)
					.append(");");
			sysScriptEntity.setScriptContent(sb.toString());
		} else if ("mergeScript".equals(sysScriptEntity.getSourceDict())) {
			sysScriptEntity.setClassName("");
			sysScriptEntity.setClassInsName("");
			sysScriptEntity.setMethodName("");
			sysScriptEntity.setReturnType("");
			sysScriptEntity.setArgument("");
		}

		try {
			if (StringUtil.isNotEmpty(sysScriptEntity.getId())) {
				message = "更新成功";
				sysScriptService.update(sysScriptEntity);
			} else {
				message = "新增成功";
				sysScriptService.save(sysScriptEntity);
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月25日 下午3:36:43
	 * @Decription 进入脚本选择页
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "scriptSelect")
	public ModelAndView scriptSelect(HttpServletRequest request) {
		return new ModelAndView("workflow/support/sysScript/scriptSelect");
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月25日 下午3:36:41
	 * @Decription 获得分类的脚本树
	 *
	 * @param request
	 */
	@RequestMapping(params = "getScriptTree")
	public void getScriptTree(HttpServletRequest request, HttpServletResponse response) {
		List<DictValueEntity> dictValuelist = dictValueService.findValuesByType("scriptType");
		List<TreeNode> rootNodeList = new ArrayList<TreeNode>();
		Map<String, String> propertyMapping1 = new HashMap<String, String>();
		propertyMapping1.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping1.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		rootNodeList = TreeMapper.buildJsonTree(dictValuelist, propertyMapping1);
		for (int i = 0; i < dictValuelist.size(); i++) {
			DictValueEntity entity = dictValuelist.get(i);
			TreeNode rootNode = rootNodeList.get(i);

			//获得脚本类型下对应的各脚本
			List<SysScriptEntity> valuesList = sysScriptService.queryList(entity.getValue(), "Y");
			List<TreeNode> childNodeList = new ArrayList<TreeNode>();
			Map<String, String> propertyMapping2 = new HashMap<String, String>();
			propertyMapping2.put(TreeMapper.PropertyType.ID.getValue(), "id");
			propertyMapping2.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
			propertyMapping2.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "scriptContent");
			childNodeList = TreeMapper.buildJsonTree(valuesList, propertyMapping2);
			//为对应类型设置对应子脚本
			rootNode.setChildren(childNodeList);
		}

		TagUtil.tree(response, rootNodeList);
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年8月25日 下午9:24:35
	 * @Decription 验证脚本的内容
	 *
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "validExpression")
	@ResponseBody
	public AjaxJson validExpression(HttpServletRequest request, HttpServletResponse response) {
		AjaxJson result = new AjaxJson();
		Map<String, Object> map = new HashMap<String, Object>();
		String scriptContent = request.getParameter("scriptContent");
		try {
			Object obj = scriptEngine.executeObject(scriptContent, null);
			map.put("result", obj.toString());
			map.put("resultType", obj.getClass().getName());
			result.setObj(map);
			result.setMsg("执行成功");
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg(e.getMessage());
		}
		return result;
	}

	public SysScriptService getSysScriptService() {
		return sysScriptService;
	}

	@Resource
	public void setSysScriptService(SysScriptService sysScriptService) {
		this.sysScriptService = sysScriptService;
	}

	public GroovyScriptEngine getScriptEngine() {
		return scriptEngine;
	}

	@Resource
	public void setScriptEngine(GroovyScriptEngine scriptEngine) {
		this.scriptEngine = scriptEngine;
	}

	public DictValueService getDictValueService() {
		return dictValueService;
	}

	@Resource
	public void setDictValueService(DictValueService dictValueService) {
		this.dictValueService = dictValueService;
	}

}
