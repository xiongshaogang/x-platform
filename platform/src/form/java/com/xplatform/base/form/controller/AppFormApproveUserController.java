package com.xplatform.base.form.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jodd.util.StringUtil;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xplatform.base.form.entity.AppFormApproveUser;
import com.xplatform.base.form.service.AppFormApproveUserService;
import com.xplatform.base.form.service.AppFormFieldService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;

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
@RequestMapping("/appFormApproveUserController")
public class AppFormApproveUserController extends BaseController {
	
	@Resource
	private AppFormApproveUserService appFormApproveUserService;
	
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appFormApproveUser")
	public ModelAndView appFormApproveUser(HttpServletRequest request) {
		return new ModelAndView("main/home/form/formHome");
	}

	/**
	 * 单表hibernate组装表格数据
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:57:00
	 * @param AppForm
	 * @param request
	 * @param response
	 * @param dataGrid
	 */
	@RequestMapping(params = "datagrid")
	public void datagrid(AppFormApproveUser appFormApproveUser,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AppFormApproveUser.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, appFormApproveUser, request.getParameterMap());
		cq.add();
		this.appFormApproveUserService.getDataGridReturn(cq, true);
		TagUtil.datagrid(response, dataGrid);
	}

	/**
	 * 字典类型删除
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:58:16
	 * @param AppForm
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "delete")
	@ResponseBody
	public AjaxJson delete(AppFormApproveUser appFormApproveUser, HttpServletRequest request) {
		String ids = request.getParameter("ids");
		appFormApproveUserService.deleteEntityByIds(ids);
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
	@RequestMapping(params = "appFormApproveUserEdit")
	public ModelAndView appFormApproveUserEdit(AppFormApproveUser appFormApproveUser,Model model) {
		if (StringUtil.isNotEmpty(appFormApproveUser.getId())) {
			appFormApproveUser = appFormApproveUserService.get(appFormApproveUser.getId());
			model.addAttribute("AppFormApproveUser", appFormApproveUser);
		}
		return new ModelAndView("platform/system/dict/AppFormApproveUserEdit");
	}
	
	/**
	 * 增加或删除saveAppFormApproveUser
	 * @author lixt
	 * @createtime 2015年11月03日 下午16:19:16
	 * @param formId，userIds，userNames，orderbys
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "saveAppFormApproveUser")
	@ResponseBody
	public AjaxJson saveAppFormApproveUser(AppFormApproveUser appFormApproveUser, HttpServletRequest request) throws BusinessException {
		
		/*String formId = request.getParameter("formId");
		String userIds = request.getParameter("userIds");
		String userNames = request.getParameter("userNames");
		String orderbys = request.getParameter("orderbys");
		
		if(StringUtil.isEmpty(formId)){
			result.setMsg("参数传递有误");
			result.setSuccess(false);
		}else{
			result = this.appFormApproveUserService.saveOrDeleteAFAU(formId, userIds, userNames, orderbys);
			if(result.isSuccess()){
				List<AppFormApproveUser> list = this.appFormApproveUserService.getList();
				result.setObj(list);
			}
		}*/
		return result;
	}
	
	/**
	 * 增加或删除saveAppFormApproveUser
	 * @author lixt
	 * @createtime 2015年11月03日 下午16:19:16
	 * @param formId，userIds，userNames，orderbys
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "queryAppFormApproveUser")
	@ResponseBody
	public AjaxJson queryAppFormApproveUser(AppFormApproveUser appFormApproveUser, HttpServletRequest request) throws BusinessException {
		
		List<AppFormApproveUser> list = this.appFormApproveUserService.getList();
		result.setObj(list);
		result.setMsg("查询成功");
		result.setSuccess(true);
		
		return result;
	}

	
	
}
