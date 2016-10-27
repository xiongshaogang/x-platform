package ${bussiPackage}.${entityPackage}.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
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
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.service.SystemService;

import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;
import ${bussiPackage}.${entityPackage}.service.${entityName}Service;
<#list subList as sub>
import ${bussiPackage}.${sub.entityPackage}.entity.${sub.entityName}Entity;
import ${bussiPackage}.${sub.entityPackage}.service.${sub.entityName}Service;
</#list>

/**   
 * @Title: Controller
 * @Description: ${ftl_description}
 * @date ${ftl_create_time}
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/${entityName?uncap_first}Controller")
public class ${entityName}Controller extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(${entityName}Controller.class);

	@Resource
	private ${entityName}Service ${entityName?uncap_first}Service;
	
	<#list subList as sub>
	@Resource
	private ${sub.entityName}Service ${sub.entityName?uncap_first}Service;
	
	</#list>
	
	@Resource
	private SystemService systemService;
	
	public void set${entityName}Service(${entityName}Service ${entityName?uncap_first}Service) {
		this.${entityName?uncap_first}Service = ${entityName?uncap_first}Service;
	}
	
	<#list subList as sub>
	public void set${sub.entityName}Service(${sub.entityName}Service ${sub.entityName?uncap_first}Service) {
		this.${sub.entityName?uncap_first}Service = ${sub.entityName?uncap_first}Service;
	}
	
	</#list>

	public void setSystemService(SystemService systemService) {
		this.systemService = systemService;
	}
	
	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * ${ftl_description}列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "${entityName?uncap_first}")
	public ModelAndView ${entityName?uncap_first}(HttpServletRequest request) {
		return new ModelAndView("${jsp_path}/${entityPackage}/${entityName?uncap_first}List");
	}

	/**
	 * easyui AJAX请求数据
	 * 
	 * @param request
	 * @param response
	 * @param dataGrid
	 * @param entity
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(${entityName}Entity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ${entityName?uncap_first}, request.getParameterMap());
		try{
		//自定义追加查询条件
		
		}catch (Exception e) {
			throw new BusinessException(e.getMessage());
		}
		cq.add();
		this.${entityName?uncap_first}Service.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除${ftl_description}
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request) {
		message = "${ftl_description}删除成功";
		try{
			${entityName?uncap_first}Service.delete(${entityName?uncap_first}.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "${ftl_description}删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除${ftl_description}
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
			${entityName?uncap_first}Service.batchDelete(ids);
			message = "${ftl_description}删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "${ftl_description}删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param ${entityName?uncap_first}
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "${entityName?uncap_first}Edit")
	public ModelAndView ${entityName?uncap_first}Edit(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request) {
		if (StringUtil.isNotEmpty(${entityName?uncap_first}.getId())) {
			${entityName?uncap_first} = ${entityName?uncap_first}Service.get(${entityName?uncap_first}.getId());
		}
		return new ModelAndView("${jsp_path}/${entityPackage}/${entityName?uncap_first}Edit").addObject("${entityName?uncap_first}", ${entityName?uncap_first});
	}
	


	/**
	 * 新增或修改${ftl_description}
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping(params = "save${entityName}")
	@ResponseBody
	public AjaxJson save${entityName}(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(${entityName?uncap_first}.getId())) {
				message = "${ftl_description}更新成功";
				${entityName?uncap_first}Service.update(${entityName?uncap_first});
			} else {
				message = "${ftl_description}新增成功";
				${entityName?uncap_first}Service.save(${entityName?uncap_first});
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "${ftl_description}操作失败";
		}
		<#assign flag = true>
		<#list subList as sub>
		<#if sub.baseFormEntity.enableEdit?if_exists?html=='Y'>
		<#if flag == true>
		String insertedRows = "";
		String updatedRows = "";
		String deletedRows = "";
		
		List<Map<String, Object>> insertList = null;
		List<Map<String, Object>> updateList = null;
		List<Map<String, Object>> deleteList = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		<#assign flag = false>
		</#if>
		insertedRows = request.getParameter("insertedRows${sub.entityName}");
		updatedRows = request.getParameter("updatedRows${sub.entityName}");
		deletedRows = request.getParameter("deletedRows${sub.entityName}");
		
		insertList = JSONHelper.toList(insertedRows);
		updateList = JSONHelper.toList(updatedRows);
		deleteList = JSONHelper.toList(deletedRows);
		
		for(int i=0;i<insertList.size();i++){
			Map<String, Object> map = insertList.get(i);
			${sub.entityName}Entity ${sub.entityName?uncap_first} = new ${sub.entityName}Entity();
			${sub.entityName?uncap_first}.set${entityName}(${entityName?uncap_first});
			<#list sub.columns as po>
			<#if po.listShow?if_exists?html =='Y' && po.fieldType?if_exists?html !='Date'>
			${sub.entityName?uncap_first}.set${po.fieldName?cap_first}(map.get("${po.fieldName}") == null ? "" : map.get("${po.fieldName}").toString());
			</#if>
			<#if po.listShow?if_exists?html =='Y' && po.fieldType?if_exists?html =='Date'>
			try {
				${sub.entityName?uncap_first}.set${po.fieldName?cap_first}(df.parse(map.get("${po.fieldName}") == null ? "" : map.get("${po.fieldName}").toString()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			</#if>
		   </#list>
		    try {
					${sub.entityName?uncap_first}Service.save(${sub.entityName?uncap_first});
				} catch (Exception e) {
					e.printStackTrace();
					message = "${sub.tableName}表保存失败";
					throw new BusinessException(e.getMessage());
				}
		}
		
		for(int i=0;i<updateList.size();i++){
			Map<String, Object> map = updateList.get(i);
			${sub.entityName}Entity ${sub.entityName?uncap_first} = ${sub.entityName?uncap_first}Service.get(map.get("id").toString());
			${sub.entityName?uncap_first}.set${entityName}(${entityName?uncap_first});
			<#list sub.columns as po>
			<#if po.listShow?if_exists?html =='Y' && po.fieldType?if_exists?html !='Date'>
			${sub.entityName?uncap_first}.set${po.fieldName?cap_first}(map.get("${po.fieldName}") == null ? "" : map.get("${po.fieldName}").toString());
			</#if>
			<#if po.listShow?if_exists?html =='Y' && po.fieldType?if_exists?html =='Date'>
			try {
				${sub.entityName?uncap_first}.set${po.fieldName?cap_first}(df.parse(map.get("${po.fieldName}") == null ? "" : map.get("${po.fieldName}").toString()));
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			</#if>
		   </#list>
		    try {
					${sub.entityName?uncap_first}Service.update(${sub.entityName?uncap_first});
				} catch (Exception e) {
					e.printStackTrace();
					message = "${sub.tableName}表更新失败";
					throw new BusinessException(e.getMessage());
				}
		}
		
		for(int i=0;i<deleteList.size();i++){
			Map<String, Object> map = deleteList.get(i);
		    try {
					${sub.entityName?uncap_first}Service.delete(map.get("id").toString());
				} catch (Exception e) {
					e.printStackTrace();
					message = "${sub.tableName}表删除失败";
					throw new BusinessException(e.getMessage());
				}
		}
		
		</#if>
		</#list>
		result.setMsg(message);
		result.setObj(${entityName?uncap_first}.getId());
		return result;
	}
}
