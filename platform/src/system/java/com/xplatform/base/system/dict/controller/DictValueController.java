package com.xplatform.base.system.dict.controller;

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
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictTypeService;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description : 字典值管理controller ，维护树的过程中注意维护level,is_leaf,tree_index字段
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
@RequestMapping("/dictValueController")
public class DictValueController extends BaseController {

	@Resource
	private DictValueService dictValueService;
	
	@Resource
	private DictTypeService dictTypeService;

	private String message;

	public void setDictValueService(DictValueService dictValueService) {
		this.dictValueService = dictValueService;
	}

	public void setDictTypeService(DictTypeService dictTypeService) {
		this.dictTypeService = dictTypeService;
	}

	/**
	 * 字典值管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dictValue")
	public ModelAndView DictValue(HttpServletRequest request) {
		request.setAttribute("typeId", request.getParameter("typeId"));
		return new ModelAndView("platform/system/dict/dictValueList");
	}

	/**
	 * 字典值树
	 * @author xiehs
	 * @createtime 2014年5月24日 下午6:13:48
	 * @param DictValue
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "tree")
	public void tree(DictValueEntity DictValue, HttpServletRequest request,
			HttpServletResponse response) {
		//类型id
		Map<String,String> param=new HashMap<String,String>();
		if (StringUtil.isNotEmpty(request.getParameter("typeId"))) {
			param.put("dictType.id", request.getParameter("typeId"));
		}else{
			param.put("dictType.id", request.getParameter("-1"));
		}
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			param.put("parent.id",request.getParameter("id"));
		} else {
			param.put("parent.id","-1");
		}
		List<DictValueEntity> DictValueList = dictValueService.queryListByTypeidAndParentid(param);
		//树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		List<TreeNode> treeList = TreeMapper.buildJsonTree(DictValueList,propertyMapping);
		TagUtil.tree(response, treeList);
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param DictValue
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(DictValueEntity dictValue, HttpServletRequest request,
			HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DictValueEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, dictValue, request.getParameterMap());
			if (StringUtil.isNotEmpty(request.getParameter("typeId"))) {
				cq.eq("dictType.id", request.getParameter("typeId"));
			} else {
				cq.eq("dictType.id", "-1");
			}
			//父节点
			if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
				cq.eq("parent.id", request.getParameter("parentId"));
			} else {
				cq.eq("parent.id", "-1");
			}
		cq.add();
		this.dictValueService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 字典值删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param DictValue
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(DictValueEntity dictValue, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		
		String ids = request.getParameter("ids");
		try {
			if (jodd.util.StringUtil.isNotBlank(ids)) {
				String[] idArr = jodd.util.StringUtil.split(ids, ",");
				for (String id : idArr) {
					dictValueService.delete(id);
				}
			}
			message = "字典值删除成功";
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
	@RequestMapping(params = "dictValueEdit")
	public ModelAndView dictValueEdit(HttpServletRequest request, DictValueEntity dictValue, Model model) {
		if (StringUtil.isNotEmpty(dictValue.getId())) {
			dictValue = dictValueService.get(dictValue.getId());
			model.addAttribute("dictValue", dictValue);
		}
		model.addAttribute("typeId", request.getParameter("typeId"));
		return new ModelAndView("platform/system/dict/dictValueEdit");
	}

	/**
	 * 新增或修改字典值
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param DictValue
	 * @return
	 */
	@RequestMapping(params = "saveDictValue")
	@ResponseBody
	public AjaxJson saveDictValue(DictValueEntity dictValue, HttpServletRequest request) {
		if (StringUtil.isNotEmpty(request.getParameter("typeId"))) {
			DictTypeEntity dictType=this.dictTypeService.get(request.getParameter("typeId"));
			dictValue.setDictType(dictType);
			dictValue.setTypeCode(dictType.getCode());
			dictValue.setTypeValue(dictType.getCode()+"_"+dictValue.getValue());
		}
		AjaxJson result = new AjaxJson();
		try {
			if (StringUtil.isNotEmpty(dictValue.getId())) {
				message = "字典值更新成功";
				dictValueService.update(dictValue);
			} else {
				message = "字典值新增成功";
				DictValueEntity parent = new DictValueEntity();
				if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
					parent.setId(request.getParameter("parentId"));
				} else {
					parent.setId("-1");
				}
				dictValue.setParent(parent);
				dictValueService.save(dictValue);
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
		String dictTypeId = request.getParameter("dictTypeId");
		String newValue = request.getParameter("param");
		String oldValue = request.getParameter("oldValue");
		String uniquemsg = request.getParameter("uniquemsg");

		boolean flag = false;
		if (newValue == null || StringUtil.equals(newValue, oldValue)) {//修改同一条记录
			flag = true;
		}
		flag = this.dictValueService.isValueUnique(dictTypeId, newValue);
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

}
