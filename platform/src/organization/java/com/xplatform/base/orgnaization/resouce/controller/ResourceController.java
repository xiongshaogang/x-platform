package com.xplatform.base.orgnaization.resouce.controller;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.resouce.entity.ResourceEntity;
import com.xplatform.base.orgnaization.resouce.service.ResourceService;

/**
 * 
 * description : 资源管理controller
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
@RequestMapping("/resourceController")
public class ResourceController extends BaseController {
	
	@Resource
	private ResourceService resourceService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 资源管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "resource")
	public ModelAndView resource(HttpServletRequest request) {
		return new ModelAndView("platform/organization/resource/resourceList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param resource
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ResourceEntity resource,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ResourceEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, resource, request.getParameterMap());
		if(StringUtil.isNotEmpty(request.getParameter("moduleId"))){
			cq.eq("module.id", request.getParameter("moduleId"));
		}else{
			cq.eq("module.id", "-1");
		}
		cq.add();
		this.resourceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 资源删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param resource
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ResourceEntity resource, HttpServletRequest request) {
		message = "资源删除成功";
		try{
			resourceService.delete(resource.getId());
		}catch(Exception e){
			message = "资源删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除资源
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			resourceService.batchDelete(ids);
			message="删除成功";
		} catch (Exception e) {
			// TODO: handle exception
			message="删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "resourceEdit")
	public ModelAndView resourceEdit(ResourceEntity resource,Model model) {
		if (StringUtil.isNotEmpty(resource.getId())) {
			resource = resourceService.get(resource.getId());
			model.addAttribute("resource", resource);
		}
		return new ModelAndView("platform/organization/resource/resourceEdit");
	}
	
	/**
	 * 新增或修改资源
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param resource
	 * @return
	 */
	@RequestMapping(params = "saveResource")
	@ResponseBody
	public AjaxJson saveResource(ResourceEntity resource,HttpServletRequest request) {
		
		resource.setOptCode(request.getParameter("moduleCode")+"_"+resource.getCode()+"_"+resource.getOptType());
		try {
			if (StringUtil.isNotEmpty(resource.getId())) {
				message = "资源更新成功";
				resourceService.update(resource);
			} else {
				message = "资源新增成功";
				ModuleEntity module=new ModuleEntity();
				if(StringUtil.isNotEmpty(request.getParameter("moduleId"))){
					module.setId(request.getParameter("moduleId"));
					resource.setModule(module);
					resourceService.save(resource);
				}else{
					message = "新增资源没有指定模块";
				}
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
	
	@RequestMapping(params = "checkUnique")
	@ResponseBody
	public AjaxJson checkUnique(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String propertyName = request.getParameter("name");
		String newValue = request.getParameter("param");
		String oldValue = request.getParameter("oldValue");
		String uniquemsg = request.getParameter("uniquemsg");
		
		Map<String, String> param = new HashMap<String, String>();
		param.put("newValue", newValue);
		param.put("oldValue", oldValue);
		
		boolean flag = resourceService.isUnique(param, propertyName);
		if (!flag) {
			result.setStatus("n");
			if (StringUtil.isNotEmpty(uniquemsg))
				result.setInfo(uniquemsg);
		} else {
			result.setStatus("y");
			result.setInfo("通过验证"); 
		}
		return result;
	}

	public void setResourceService(ResourceService resourceService) {
		this.resourceService = resourceService;
	}
	
	
}
