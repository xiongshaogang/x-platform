package com.xplatform.base.develop.metadata.controller;

import java.util.ArrayList;
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

import com.xplatform.base.develop.metadata.entity.MetaDataEntity;
import com.xplatform.base.develop.metadata.entity.MetaDataFieldEntity;
import com.xplatform.base.develop.metadata.service.MetaDataFieldService;
import com.xplatform.base.develop.metadata.service.MetaDataService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

/**
 * @Title: Controller
 * @Description: 表属性
 * @author onlineGenerator
 * @date 2014-05-29 16:49:08
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/metaDataFieldController")
public class MetaDataFieldController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(MetaDataFieldController.class);

	@Resource
	private MetaDataFieldService metaDataFieldService;
	@Resource
	private MetaDataService metaDataService;

	private AjaxJson result = new AjaxJson();
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 表属性列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "formField")
	public ModelAndView formField(HttpServletRequest request) {
		return new ModelAndView("com/xplatform/base/develop/main/formFieldList");
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
	public void datagrid(MetaDataFieldEntity formField, HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		String tableId = request.getParameter("tableId");
		MetaDataEntity formHeadEntity = new MetaDataEntity();
		formHeadEntity.setId(tableId);
		String jformVersion = StringUtil.isEmpty(request.getParameter("jformVersion"), "0");
		List<MetaDataFieldEntity> list = new ArrayList<MetaDataFieldEntity>();
		if (StringUtil.isEmpty(tableId) && !(Integer.valueOf(jformVersion) > 1)) {
			MetaDataFieldEntity formField1 = new MetaDataFieldEntity();
			formField1.setTable(formHeadEntity);
			formField1.setFieldName("id");
			formField1.setContent("主键");
			formField1.setLength(32);
			formField1.setPointLength(0);
			formField1.setType("string");
			formField1.setIsKey("Y");
			formField1.setIsNull("N");
			MetaDataFieldEntity formField2 = new MetaDataFieldEntity();
			formField2.setTable(formHeadEntity);
			formField2.setFieldName("createUserId");
			formField2.setContent("创建人ID");
			formField2.setLength(32);
			formField2.setPointLength(0);
			formField2.setType("string");
			formField2.setIsKey("N");
			formField2.setIsNull("Y");
			MetaDataFieldEntity formField3 = new MetaDataFieldEntity();
			formField3.setTable(formHeadEntity);
			formField3.setFieldName("createUserName");
			formField3.setContent("创建人name");
			formField3.setLength(100);
			formField3.setPointLength(0);
			formField3.setType("string");
			formField3.setIsKey("N");
			formField3.setIsNull("Y");
			MetaDataFieldEntity formField4 = new MetaDataFieldEntity();
			formField4.setTable(formHeadEntity);
			formField4.setFieldName("createTime");
			formField4.setContent("创建时间");
			formField4.setLength(32);
			formField4.setPointLength(0);
			formField4.setType("Date");
			formField4.setIsKey("N");
			formField4.setIsNull("Y");
			MetaDataFieldEntity formField5 = new MetaDataFieldEntity();
			formField5.setTable(formHeadEntity);
			formField5.setFieldName("updateUserId");
			formField5.setContent("更新人ID");
			formField5.setLength(32);
			formField5.setPointLength(0);
			formField5.setType("string");
			formField5.setIsKey("N");
			formField5.setIsNull("Y");
			MetaDataFieldEntity formField6 = new MetaDataFieldEntity();
			formField6.setTable(formHeadEntity);
			formField6.setFieldName("updateUserName");
			formField6.setContent("更新人name");
			formField6.setLength(100);
			formField6.setPointLength(0);
			formField6.setType("string");
			formField6.setIsKey("N");
			formField6.setIsNull("Y");
			MetaDataFieldEntity formField7 = new MetaDataFieldEntity();
			formField7.setTable(formHeadEntity);
			formField7.setFieldName("updateTime");
			formField7.setContent("更新时间");
			formField7.setLength(32);
			formField7.setPointLength(0);
			formField7.setType("Date");
			formField7.setIsKey("N");
			formField7.setIsNull("Y");
			list.add(formField1);
			list.add(formField2);
			list.add(formField3);
			list.add(formField4);
			list.add(formField5);
			list.add(formField6);
			list.add(formField7);
			dataGrid.setResults(list);
			dataGrid.setTotal(list.size());
		} else {
			CriteriaQuery cq = new CriteriaQuery(MetaDataFieldEntity.class, dataGrid);
			formField.setTable(metaDataService.getEntity(MetaDataEntity.class, tableId));
			HqlGenerateUtil.installHql(cq, formField, request.getParameterMap());
			cq.add();
			this.metaDataFieldService.getDataGridReturn(cq, true);
		}
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除表属性
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(MetaDataFieldEntity formField, HttpServletRequest request) {
		message = "表属性删除成功";
		try {
			metaDataFieldService.delete(formField.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = "表属性删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除表属性
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		try {
			metaDataFieldService.batchDelete(ids);
			message = "表属性删除成功";
		} catch (Exception e) {
			e.printStackTrace();
			message = "表属性删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 进入新增或者修改查看页面
	 * 
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "metaDataFieldEdit")
	public ModelAndView metaDataFieldEdit(MetaDataFieldEntity formField, Model model) {
		if (StringUtil.isNotEmpty(formField.getId())) {
			formField = metaDataFieldService.get(formField.getId());
			model.addAttribute("formField", formField);
		}
		return new ModelAndView("develop/formhead/formfield_edit");
	}

	/**
	 * 新增或修改表属性
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveFormField")
	@ResponseBody
	public AjaxJson saveFormField(MetaDataFieldEntity formField) {
		try {
			if (StringUtil.isNotEmpty(formField.getId())) {
				message = "表属性更新成功";
				metaDataFieldService.update(formField);
			} else {
				message = "表属性新增成功";
				metaDataFieldService.save(formField);
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "机构操作失败";
		}
		result.setMsg(message);
		return result;
	}

}
