package com.xplatform.base.system.dict.controller;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.framework.core.annotation.log.Action;
import com.xplatform.base.framework.core.annotation.log.ActionExecOrder;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.system.dict.entity.DictTypeEntity;
import com.xplatform.base.system.dict.service.DictTypeService;

/**
 * 
 * description : 字典类型管理controller
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
@RequestMapping("/dictTypeController")
public class DictTypeController extends BaseController {
	
	@Resource
	private DictTypeService dictTypeService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "dictType")
	public ModelAndView dictType(HttpServletRequest request) {
		return new ModelAndView("platform/system/dict/dictTypeList");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param DictType
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(DictTypeEntity dictType,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(DictTypeEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, dictType, request.getParameterMap());
		cq.add();
		this.dictTypeService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 字典类型删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param DictType
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(DictTypeEntity dictType, HttpServletRequest request) {
		String ids = request.getParameter("ids");
		dictTypeService.deleteEntityByIds(ids);
		message = "字典类型删除成功";
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 进入新增或者修改查看页面
	 * @param organization
	 * @param req
	 * @return
	 */
	@RequestMapping(params = "dictTypeEdit")
	public ModelAndView dictTypeEdit(DictTypeEntity dictType,Model model) {
		if (StringUtil.isNotEmpty(dictType.getId())) {
			dictType = dictTypeService.get(dictType.getId());
			model.addAttribute("dictType", dictType);
		}
		return new ModelAndView("platform/system/dict/dictTypeEdit");
	}
	
	/**
	 * 新增或修改字典类型
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param DictType
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "saveDictType")
	@ResponseBody
	public AjaxJson saveDictType(DictTypeEntity dictType) throws BusinessException {
		if (StringUtil.isNotEmpty(dictType.getId())) {
			message = "字典类型更新成功";
			try {
				dictTypeService.update(dictType, dictType.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			message = "字典类型新增成功";
			dictTypeService.save(dictType);
		}
		result.setMsg(message);
		return result;
	}

	public void setDictTypeService(DictTypeService dictTypeService) {
		this.dictTypeService = dictTypeService;
	}
	
	
}
