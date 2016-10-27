package com.xplatform.base.orgnaization.module.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.common.model.json.TreeGrid;
import com.xplatform.base.framework.core.common.model.json.TreeNode;
import com.xplatform.base.framework.core.common.service.CommonService;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.core.util.ExceptionUtil;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.core.util.TreeMapper;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.framework.tag.vo.easyui.TreeGridModel;
import com.xplatform.base.orgnaization.module.entity.ModuleEntity;
import com.xplatform.base.orgnaization.module.service.ModuleService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.system.dict.entity.DictValueEntity;
import com.xplatform.base.system.dict.service.DictValueService;

/**
 * 
 * description : 模块管理controller ，维护树的过程中注意维护level,is_leaf,tree_index字段
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
@RequestMapping("/moduleController")
public class ModuleController extends BaseController {

	@Resource
	private ModuleService moduleService;
	@Resource
	private CommonService commonService;
	@Resource
	private DictValueService dictValueService;

	private String message;

	/**
	 * 模块管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "module")
	public ModelAndView module(HttpServletRequest request) {
		return new ModelAndView("platform/organization/module/moduleList");
	}

	/**
	 * 模板日志管理页面跳转
	 * @author binyong
	 * @createtime 2014年6月17日 下午17:10:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "logSwitch")
	public ModelAndView logSwitch(HttpServletRequest request) {
		return new ModelAndView("platform/system/log/logSwitchList");
	}

	/**
	 * 模块树
	 * @author xiehs
	 * @createtime 2014年5月24日 下午6:13:48
	 * @param module
	 * @param request
	 * @param response
	 */
	@RequestMapping(params = "tree")
	public void tree(ModuleEntity module, HttpServletRequest request, HttpServletResponse response) {
		String parentId = "";
		boolean b = Boolean.FALSE;
		if (StringUtil.isNotEmpty(request.getParameter("id"))) {
			parentId = request.getParameter("id");
		} else {
			parentId = "-1";
		}
		List<ModuleEntity> moduleList = moduleService.findByProperty(ModuleEntity.class, "parent.id", parentId);
		// 树的转换
		Map<String, String> propertyMapping = new HashMap<String, String>();
		propertyMapping.put(TreeMapper.PropertyType.ID.getValue(), "id");
		propertyMapping.put(TreeMapper.PropertyType.TEXT.getValue(), "name");
		propertyMapping.put(TreeMapper.PropertyType.LEAF.getValue(), "isLeaf");
		propertyMapping.put(TreeMapper.PropertyType.ATTRIBUTES.getValue(), "url,code");
		List<TreeNode> treeList = TreeMapper.buildJsonTree(moduleList, propertyMapping);
		TagUtil.tree(response, treeList);
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param module
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(ModuleEntity module, HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(ModuleEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, module, request.getParameterMap());
			//自定义追加查询条件
		if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
			cq.eq("parent.id", request.getParameter("parentId"));
		} else {
			cq.eq("parent.id", "-1");
		}
		cq.add();
		this.moduleService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author binyong
	 * @createtime 2014年6月17日 下午16:57:00
	 * @param module
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "logdatagrid")
	@ResponseBody
	public List<TreeGrid> logdatagrid(ModuleEntity module, HttpServletRequest request, HttpServletResponse response,
			TreeGrid treegrid) {
		long begin = System.currentTimeMillis();
		CriteriaQuery cq = new CriteriaQuery(ModuleEntity.class);
		if (treegrid.getId() != null) {
			cq.eq("parent.id", treegrid.getId());
		}
		if (treegrid.getId() == null) {
			cq.eq("parent.id", "-1");
		}
		if (StringUtils.isNotEmpty(request.getParameter("name"))) {
			cq.like("name", "%" + request.getParameter("name") + "%");
		}
		if (StringUtils.isNotEmpty(request.getParameter("code"))) {
			cq.like("code", "%" + request.getParameter("code") + "%");
		}
		cq.add();
		List<ModuleEntity> list = commonService.getListByCriteriaQuery(cq, false);
		List<TreeGrid> treeGrids = new ArrayList<TreeGrid>();

		TreeGridModel treeGridModel = new TreeGridModel();
		List<String> attributes = new ArrayList<String>();
		treeGridModel.setIcon("iconCls");
		treeGridModel.setTextField("name");
		treeGridModel.setParentText("ModuleEntity_name");
		treeGridModel.setParentId("ModuleEntity_id");
		treeGridModel.setSrc("url");
		treeGridModel.setIdField("id");
		treeGridModel.setChildList("children");
		treeGridModel.setStatus("status");
		treeGridModel.setCode("code");
		treeGridModel.setIsLeaf("isLeaf");
		/*attributes.add("code");
		attributes.add("status");
		attributes.add("description");*/
		treeGrids = commonService.treegrid(list, treeGridModel, attributes);
		long end = System.currentTimeMillis();
		return treeGrids;
	}

	@RequestMapping(params = "setStatus")
	@ResponseBody
	public AjaxJson setStatus(HttpServletRequest request) throws BusinessException {
		AjaxJson result = new AjaxJson();
		/*String status = request.getParameter("status");
		String id = request.getParameter("id");*/
		String parameters = request.getParameter("parameters");
		Map<String, Object> map = (Map<String, Object>) JSONArray.parse(parameters);
		String id = map.get("id").toString();
		String status = map.get("status").toString();
		if ("1".equals(status)) {
			status = "0";
		} else {
			status = "1";
		}
		if (StringUtil.isEmpty(id) || StringUtil.isEmpty(status)) {
			result.setMsg("操作失败");
		} else {
			ModuleEntity module = moduleService.get(id);
			module.setStatus(status);
			moduleService.update(module);
			if ("1".equals(status)) {
				result.setMsg("开启日志成功");
			} else {
				result.setMsg("关闭日志成功");
			}

		}

		return result;
	}

	/**
	 * 模块删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param module
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(ModuleEntity module, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		message = "模块删除成功";
		try {
			List<ModuleEntity> moduleList = moduleService.findByProperty(ModuleEntity.class, "parent.id", module.getId());
			if (moduleList != null && moduleList.size() > 0) {
				message = "请先删除下级模块";
			} else {
				moduleService.delete(module.getId());
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除模块
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:27:15
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		String ids = request.getParameter("ids");
		try {
			moduleService.batchDelete(ids);
			message = "删除成功";
		} catch (BusinessException e) {
			// TODO: handle exception
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
	@RequestMapping(params = "moduleEdit")
	public ModelAndView moduleEdit(HttpServletRequest request, ModuleEntity module, Model model) {
		String parentId = request.getParameter("parentId");

		if (StringUtil.isNotEmpty(module.getId())) {
			module = moduleService.get(module.getId());
		}

		//如果是二级菜单之后的新增页面,则自动设置子系统标识
		if (StringUtil.isEmpty(module.getId()) && StringUtil.isNotEmpty(parentId)) {
			//获得一级菜单模块
			ModuleEntity parentModule = moduleService.getRoot(parentId);
			//获得一级菜单的子系统标识
			if (parentModule.getLevel().equals(1)) {
				module.setSubsystem(parentModule.getSubsystem());
			}
		}

		model.addAttribute("parentId", parentId);
		model.addAttribute("module", module);
		return new ModelAndView("platform/organization/module/moduleEdit");
	}

	/**
	 * 新增或修改模块
	 * @author xiehs
	 * @createtime 2014年5月24日 下午1:19:16
	 * @param module
	 * @return
	 */
	@RequestMapping(params = "saveModule")
	@ResponseBody
	public AjaxJson saveModule(ModuleEntity module, HttpServletRequest request) {
		AjaxJson result = new AjaxJson();
		try {
			if (StringUtil.isNotEmpty(module.getId())) {
				message = "模块更新成功";
				moduleService.update(module);
			} else {
				message = "模块新增成功";
				ModuleEntity parent = new ModuleEntity();
				if (StringUtil.isNotEmpty(request.getParameter("parentId"))) {
					parent.setId(request.getParameter("parentId"));
				} else {
					parent.setId("-1");
				}
				module.setParent(parent);
				module.setStatus("0");
				moduleService.save(module);
			}
		} catch (BusinessException e) {
			// TODO: handle exception
			message = e.getMessage();
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * @author xiaqiang
	 * @createtime 2014年12月11日 下午3:02:13
	 * @Decription 查询子系统图标及中文
	 *
	 * @param module
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "querySubsystemInfo")
	@ResponseBody
	public Map<String, String[]> querySubsystemInfo(ModuleEntity module, HttpServletRequest request) {
		List<DictValueEntity> list = dictValueService.findValuesByType("subsystem");
		Map<String, String[]> map = new HashMap<String, String[]>();
		for (DictValueEntity value : list) {
			map.put(value.getValue(), new String[] { value.getExtend1(), value.getName()});
		}
		return map;
	}
}
