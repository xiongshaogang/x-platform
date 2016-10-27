package ${bussiPackage}.${entityPackage}.controller;
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
import com.xplatform.base.system.service.SystemService;
import ${cgformConfig.baseFormEntity.refPackage}.entity.${cEntityName}Entity;
import ${bussiPackage}.${entityPackage}.entity.${entityName}Entity;
import ${bussiPackage}.${entityPackage}.service.${entityName}Service;

/**   
 * @Title: Controller
 * @Description: ${ftl_description}
 * @author onlineGenerator
 * @date ${ftl_create_time}
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/${entityName?uncap_first}Controller")
public class ${entityName}Controller extends BaseController {

	@Resource
	private ${entityName}Service ${entityName?uncap_first}Service;
	
	@Resource
	private SystemService systemService;
	
	public void set${entityName}Service(${entityName}Service ${entityName?uncap_first}Service) {
		this.${entityName?uncap_first}Service = ${entityName?uncap_first}Service;
	}

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
	 * @date ${ftl_create_time}
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
	 * @param user
	 */

	@RequestMapping(params = "datagrid")
	public void datagrid(${entityName}Entity ${entityName?uncap_first},HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(${entityName}Entity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, ${entityName?uncap_first}, request.getParameterMap());
		try{
		//自定义追加查询条件
		if (StringUtil.isNotEmpty(request.getParameter("${cField}"))) {
				cq.eq("${cEntityName?uncap_first}.id", request.getParameter("${cField}"));
			} else {
				cq.eq("${cEntityName?uncap_first}.id", "-1");
			}
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
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "${entityName?uncap_first}Edit")
	public ModelAndView ${entityName?uncap_first}Edit(${entityName}Entity ${entityName?uncap_first},Model model) {
		if (StringUtil.isNotEmpty(${entityName?uncap_first}.getId())) {
			${entityName?uncap_first} = ${entityName?uncap_first}Service.get(${entityName?uncap_first}.getId());
			model.addAttribute("${entityName?uncap_first}", ${entityName?uncap_first});
		}
		return new ModelAndView("${jsp_path}/${entityPackage}/${entityName?uncap_first}Edit");
	}
	


	/**
	 * 新增或修改${ftl_description}
	 * 
	 * @param ids
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
				if(StringUtil.isNotEmpty(${entityName?uncap_first}.get${cEntityName}().getId())){
					${entityName?uncap_first}Service.save(${entityName?uncap_first});
				}else{
					message = "新增${ftl_description}没有指定树记录";
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "${ftl_description}操作失败";
		}
		result.setMsg(message);
		return result;
	}
}
