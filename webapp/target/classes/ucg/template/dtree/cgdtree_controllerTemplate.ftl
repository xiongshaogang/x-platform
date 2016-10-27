package ${bussiPackage}.${entityPackage}.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.xplatform.base.system.service.SystemService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

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
	 * 模块树
	 * @createtime ${ftl_create_time}
	 * @param ${entityName?uncap_first}
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "tree")
	public void tree(${entityName}Entity ${entityName?uncap_first}, HttpServletRequest request,
			HttpServletResponse response) throws Exception{
		String parentId = "";
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		List<${entityName}Entity> ${entityName?uncap_first}List = ${entityName?uncap_first}Service.queryListByPorperty(
				"parent.id", parentId);
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		List<TreeNode> treeList = TreeMapper.buildJsonTree(${entityName?uncap_first}List,
				propertyMapping);
		TagUtil.tree(response, treeList);
	}
	

	/**
	 * 批量删除${ftl_description}
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(HttpServletRequest request) {
		String ids=request.getParameter("ids");
		try {
			List<${entityName}Entity> ${entityName?uncap_first}List= ${entityName?uncap_first}Service.queryListByPorperty("parent.id",ids);
			if(${entityName?uncap_first}List!=null && ${entityName?uncap_first}List.size() > 0){
				message="请先删除子节点";
			}else{
				${entityName?uncap_first}Service.delete(ids);
				message="删除成功";
			}
		} catch (Exception e) {
			message = e.getMessage();
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
	 * 新增或修改
	 * @return
	 */
	@RequestMapping(params = "save${entityName}")
	@ResponseBody
	public AjaxJson save${entityName}(${entityName}Entity ${entityName}, HttpServletRequest request) {
		try {
			if (StringUtil.isNotEmpty(${entityName}.getId())) {
				message = "${ftl_description}更新成功";
				${entityName?uncap_first}Service.update(${entityName});
			} else {
				message = "${ftl_description}新增成功";
				${entityName}Entity ${entityName?uncap_first}Entity = new ${entityName}Entity();
				${entityName}Entity parent =null;
				if(jodd.util.StringUtil.isNotEmpty(${entityName}.getParent().getId())){
				    parent = ${entityName?uncap_first}Service.get(${entityName}.getParent().getId());
				 }	
				else{ 
				${entityName?uncap_first}Entity.setId("-1");
				${entityName}.setParent(${entityName?uncap_first}Entity);
				}
				${entityName?uncap_first}Service.save(${entityName});
				
				//设置父节点不为子节点
				if(StringUtil.equals(parent.getIsLeaf(),"1") && parent !=null ){
				parent.setIsLeaf("0");
				${entityName?uncap_first}Service.update(parent);
				}
			}
		} catch (Exception e) {
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}
}
