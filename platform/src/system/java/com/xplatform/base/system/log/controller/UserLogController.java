package com.xplatform.base.system.log.controller;
import java.util.HashMap;
import java.util.Map;

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
import com.xplatform.base.orgnaization.user.entity.UserEntity;
import com.xplatform.base.platform.common.manager.ClientManager;
import com.xplatform.base.platform.common.utils.ClientUtil;
import com.xplatform.base.platform.common.vo.Client;
import com.xplatform.base.system.log.entity.UserLogEntity;
import com.xplatform.base.system.log.service.UserLogService;



/**   
 * @Title: Controller
 * @Description: 用户日志
 * @author onlineGenerator
 * @date 2014-06-17 10:05:54
 * @version V1.0   
 *
 */
@Controller
@RequestMapping("/userLogController")
public class UserLogController extends BaseController {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(UserLogController.class);

	@Resource
	private UserLogService userLogService;
	
	private AjaxJson result = new AjaxJson();
	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}


	/**
	 * 用户日志列表 页面跳转
	 * 
	 * @return
	 */
	@RequestMapping(params = "userLog")
	public ModelAndView userLog(HttpServletRequest request) {
		return new ModelAndView("platform/system/log/userLogList");
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
	public void datagrid(UserLogEntity userLog,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(UserLogEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, userLog, request.getParameterMap());
		HttpSession session = ContextHolderUtils.getSession();
		//获取client对象，得到当前用户的Institution的id
		Client client = ClientManager.getInstance().getClient(session.getId());
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("time", "desc");
		cq.setOrder(map);
		cq.add();
		this.userLogService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 删除用户日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(UserLogEntity userLog, HttpServletRequest request) {
		message = "用户日志删除成功";
		try{
			userLogService.delete(userLog.getId());
		}catch(Exception e){
			e.printStackTrace();
			message = "用户日志删除失败";
		}
		result.setMsg(message);
		return result;
	}
	
	/**
	 * 批量删除用户日志
	 * 
	 * @return
	 */
	@RequestMapping(params = "batchDelete")
	@ResponseBody
	public AjaxJson batchDelete(HttpServletRequest request){
		String ids=request.getParameter("ids");
		try{
				userLogService.batchDelete(ids);
				message = "用户日志删除成功";
		}catch(Exception e){
			e.printStackTrace();
			message = "用户日志删除失败";
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
	@RequestMapping(params = "userLogEdit")
	public ModelAndView userLogEdit(UserLogEntity userLog,Model model) {
		if (StringUtil.isNotEmpty(userLog.getId())) {
			userLog = userLogService.get(userLog.getId());
			model.addAttribute("userLog", userLog);
		}
		return new ModelAndView("platform/system/log/userLogEdit");
	}
	


	/**
	 * 新增或修改用户日志
	 * 
	 * @param ids
	 * @return
	 */
	@RequestMapping(params = "saveUserLog")
	@ResponseBody
	public AjaxJson saveUserLog(UserLogEntity userLog) {
		try {
			if (StringUtil.isNotEmpty(userLog.getId())) {
				message = "用户日志更新成功";
				userLogService.update(userLog);
			} else {
				message = "用户日志新增成功";
				userLogService.save(userLog);
			}
		} catch (Exception e) {
			// TODO: handle exception
			message = "用户日志操作失败";
		}
		result.setMsg(message);
		return result;
	}
}
