package com.xplatform.base.system.statistics.datasource.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
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
import com.xplatform.base.framework.core.util.ContextHolderUtils;
import com.xplatform.base.framework.core.util.StringUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.service.AuthorityService;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.statistics.datasource.entity.DataSourceEntity;
import com.xplatform.base.system.statistics.datasource.service.DataSourceService;
import com.xplatform.base.system.statistics.field.service.FieldService;

/**
 * @Title: Controller
 * @Description: 数据源
 * @author onlineGenerator
 * @date 2014-07-02 11:07:23
 * @version V1.0
 *
 */
@Controller
@RequestMapping("/dataSourceController")
public class DataSourceController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger
			.getLogger(DataSourceController.class);

	@Resource
	private DataSourceService dataSourceService;

	@Resource
	private AuthorityService authorityService;

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
	 * 数据源列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "dataSource")
	public ModelAndView dataSource(HttpServletRequest request) {
		return new ModelAndView(
				"platform/system/statistics/dataSource/dataSourceList");
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
	public void datagrid(DataSourceEntity dataSource,
			HttpServletRequest request, HttpServletResponse response,
			DataGrid dataGrid) {
		UserEntity user = ClientUtil.getUserEntity();
		Map<String, String> map = new HashMap<String, String>();
		map.put("userId", user.getId());
		CriteriaQuery cq = new CriteriaQuery(DataSourceEntity.class, dataGrid);
		// 查询条件组装器
		HqlGenerateUtil.installHql(cq, dataSource, request.getParameterMap());
		cq.add();
		this.dataSourceService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除数据源
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(DataSourceEntity dataSource,
			HttpServletRequest request) {
		message = "数据源删除成功";
		try {
			fieldService.deleteDataSourceField(dataSource.getId());
			dataSourceService.delete(dataSource.getId());
		} catch (Exception e) {
			e.printStackTrace();
			message = "数据源删除失败";
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 批量删除数据源
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request) {
		String ids = request.getParameter("ids");
		message = "数据源删除成功";

		if (StringUtils.isNotBlank(ids)) {
			String[] idArr = StringUtil.split(ids, ",");
			for (String id : idArr) {
				fieldService.deleteDataSourceField(id);
				try {
					dataSourceService.delete(id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "数据源删除成功";
				}
			}
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
	@RequestMapping(params = "dataSourceEdit")
	public ModelAndView dataSourceEdit(DataSourceEntity dataSource, Model model) {
		if (StringUtil.isNotEmpty(dataSource.getId())) {
			dataSource = dataSourceService.get(dataSource.getId());
			model.addAttribute("dataSource", dataSource);
		}
		return new ModelAndView(
				"platform/system/statistics/dataSource/dataSourceEdit");
	}

	/**
	 * 新增或修改数据源
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "savedataSource")
	@ResponseBody
	public AjaxJson savedataSource(DataSourceEntity dataSource) {
		boolean flag = true;
		result.setObj("right");
		if (dataSource.getType().contains("sql")
				&& StringUtils.isNotEmpty(dataSource.getValue())) {
			flag=  fieldService.judenSqlValid(dataSource.getValue());
			if(!flag){
			message = "数据值填写错误";
			result.setObj("error");
			}
		}
		if (flag) {
			try {
				if (StringUtil.isNotEmpty(dataSource.getId())) {
					message = "数据源更新成功";
					dataSourceService.update(dataSource);
				} else {
					message = "数据源新增成功";
					dataSourceService.save(dataSource);
				}
			} catch (Exception e) {
				// TODO: handle exception
				message = "数据源操作失败";
			}
			if (dataSource.getType().contains("sql")
					&& StringUtils.isNotEmpty(dataSource.getValue())) {
				String sql = dataSource.getValue();
				fieldService.deleteDataSourceField(dataSource.getId());// 在插入数据前删除相应记录
				try {
					fieldService.insertDataSourceField(sql, dataSource.getId());
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					message = "数据值填写错误";
					result.setObj("error");
					flag = false;
				}// sql查询结果字段，插入到t_sys_field表中
			}
		}
		result.setMsg(message);
		return result;
	}

	/**
	 * 获取数据列表
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "getList")
	@ResponseBody
	public List<DataSourceEntity> getList() {
		UserEntity user = ClientUtil.getUserEntity();
		List<DataSourceEntity> list = dataSourceService.getList(user);
		return list;
	}
}
