package com.xplatform.base.system.log.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.log.entity.OperLogEntity;
import com.xplatform.base.system.log.service.OperLogService;


/**   
 * @Title: Controller
 * @Description: 系统日志
 * @author onlineGenerator
 * @date 2014-06-17 19:16:27
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/operLogController")
public class OperLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(OperLogController.class);

	@Resource
	private OperLogService operLogService;
	
	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 系统日志列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "operLog")
	public ModelAndView operLog(HttpServletRequest request) {
		return new ModelAndView("platform/system/log/operLogList");
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
	public void datagrid(OperLogEntity operLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(OperLogEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, operLog, request.getParameterMap());
		HttpSession session = ContextHolderUtils.getSession();
		//获取client对象，得到当前用户的Institution的id
		Client client = ClientManager.getInstance().getClient(session.getId());
		cq.add();
		this.operLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除系统日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(OperLogEntity operLog, HttpServletRequest request) {
		message = "系统日志删除成功";
		try{
			operLogService.delete(operLog.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "系统日志删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除系统日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
				operLogService.batchDelete(ids);
				message = "系统日志删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "系统日志删除失败";
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
	@RequestMapping(params = "operLogEdit")
	public ModelAndView operLogEdit(OperLogEntity operLog,Model model) {
		if (StringUtil.isNotEmpty(operLog.getId())) {
			operLog = operLogService.get(operLog.getId());
			model.addAttribute("operLog", operLog);
		}
		return new ModelAndView("platform/system/log/operLogEdit");
	}
	


	/**
	 * 新增或修改系统日志
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveOperLog")
	@ResponseBody
	public AjaxJson saveOperLog(OperLogEntity operLog) {
		try {
			if (StringUtil.isNotEmpty(operLog.getId())) {
				message = "系统日志更新成功";
				operLogService.update(operLog);
			} else {
				message = "系统日志新增成功";
				operLogService.save(operLog);
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "系统日志操作失败";
		}
		result.setMsg(message);
		return result;
	}
}
