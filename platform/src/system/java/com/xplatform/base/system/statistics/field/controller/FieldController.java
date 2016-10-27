package com.xplatform.base.system.statistics.field.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.ComboBox;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ApplicationContextUtil;
import com.xplatform.base.framework.core.util.JSONHelper;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.statistics.field.entity.FieldEntity;
import com.xplatform.base.system.statistics.field.service.FieldService;

/**   
 * @Title: Controller
 * @Description: 数据源字段
 * @author onlineGenerator
 * @date 2014-07-02 16:00:08
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/fieldController")
public class FieldController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(FieldController.class);

	@Resource
	private FieldService fieldService;
	
	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 数据源字段列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "field1")
	public ModelAndView field(HttpServletRequest request) {
		request.setAttribute("datasourceId", request.getParameter("datasourceId"));
		return new ModelAndView("platform/system/statistics/field/fieldList");
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
	public void datagrid(FieldEntity field,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(FieldEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, field, request.getParameterMap());
		//自定义追加查询条件
		if (StringUtils.isNotEmpty(request.getParameter("datasourceId"))) {
			cq.eq("datasourceId", request.getParameter("datasourceId"));
		}
		cq.add();
		this.fieldService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除数据源字段
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(FieldEntity field, HttpServletRequest request) {
		message = "数据源字段删除成功";
		try{
			fieldService.delete(field.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "数据源字段删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除数据源字段
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
				fieldService.batchDelete(ids);
				message = "数据源字段删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "数据源字段删除失败";
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
	@RequestMapping(params = "fieldEdit")
	public ModelAndView fieldEdit(FieldEntity field,Model model) {
		if (StringUtil.isNotEmpty(field.getId())) {
			field = fieldService.get(field.getId());
			model.addAttribute("field", field);
		}
		return new ModelAndView("platform/system/statistics/field/fieldEdit");
	}
	


	/**
	 * 新增或修改数据源字段
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveField")
	@ResponseBody
	public AjaxJson saveField(HttpServletRequest request) {
		String datagridList = request.getParameter("datagridList");
		String updated = request.getParameter("updatedRows");
		List<Map<String, Object>> list = JSONHelper.toList(datagridList);
		List<Map<String, Object>> updateList = JSONHelper.toList(updated);
		message="数据没有更改";
		int x = 0;
		for(int i=0;i<list.size();i++){
			Map<String, Object> map = list.get(i);
			if(StringUtils.equals(map.get("isx")== null ? "" : map.get("isx").toString(), "Y")){
				x+=1;
			}
			if(StringUtils.equals(map.get("isy")== null ? "" : map.get("isy").toString(), "Y")){
				if(!StringUtils.equals("int", map.get("type")== null ? "" : map.get("type").toString()) 
						|| !StringUtils.equals("double", map.get("type")== null ? "" : map.get("type").toString())
						|| !StringUtils.equals("BigDecimal", map.get("type")== null ? "" : map.get("BigDecimal").toString())){
					message = "Y轴的类型必须是数字";
					result.setMsg(message);
					return result;
				}
			}
		}
		if(x==0){
			message = "必须要有一个字段作为X轴";
			result.setMsg(message);
			return result;
		}else if(x>1){
			message = "X轴字段有且仅有一个";
			result.setMsg(message);
			return result;
		}
		
		for(int i=0;i<updateList.size();i++){
			Map<String, Object> map = updateList.get(i);
			//保存更改
			FieldEntity field = fieldService.get(map.get("id").toString());
			field.setName(map.get("name")== null ? "" : map.get("name").toString());
			field.setShowName(map.get("showName")== null ? "" : map.get("showName").toString());
			field.setType(map.get("type")== null ? "" : map.get("type").toString());
			field.setIsshow(map.get("isshow")== null ? "" : map.get("isshow").toString());
			field.setIssum(map.get("issum")== null ? "" : map.get("issum").toString());
			field.setIssearch(map.get("issearch")== null ? "" : map.get("issearch").toString());
			field.setSearchActivex(map.get("searchActivex")== null ? "" : map.get("searchActivex").toString());
			field.setSearchCondition(map.get("searchCondition")== null ? "" : map.get("searchCondition").toString());
			field.setDictCode(map.get("dictCode")== null ? "" : map.get("dictCode").toString());
			field.setIsx(map.get("isx")== null ? "" : map.get("isx").toString());
			field.setIsy(map.get("isy")== null ? "" : map.get("isy").toString());
			try {
				fieldService.update(field);
				message="字段数据保存成功";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				message="字段数据保存失败";
				e.printStackTrace();
			}
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 获取combobox数据
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getComboxData")
	@ResponseBody
	public List<ComboBox> getComboxData(HttpServletRequest request) {
		List<ComboBox> valueList =new ArrayList<ComboBox>();
		String dictCode= request.getParameter("dictCode");
		String type = request.getParameter("type");
		DictTypeService dictTypeService = ApplicationContextUtil
				.getBean("dictTypeService");
		if(StringUtils.equals("all", dictCode)){
			List<DictTypeEntity> list = dictTypeService.getList();
			for(DictTypeEntity dict : list){
				if(StringUtils.equals(type, dict.getValueType())){
					ComboBox cbox = new ComboBox();
					cbox.setId(dict.getCode());
					cbox.setText(dict.getName());
					valueList.add(cbox);
				}
			}
		}else{
			valueList = dictTypeService
					.findCacheByCode(dictCode);
		}
		return valueList;
	}
	
	
}
