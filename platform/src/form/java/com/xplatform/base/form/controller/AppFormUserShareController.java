package com.xplatform.base.form.controller;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.xplatform.base.form.entity.AppFormUserShareEntity;
import com.xplatform.base.form.service.AppFormUserShareService;
import com.xplatform.base.framework.core.common.controller.BaseController;
import com.xplatform.base.framework.core.common.exception.BusinessException;
import com.xplatform.base.framework.core.common.hibernate.qbc.CriteriaQuery;
import com.xplatform.base.framework.core.common.model.json.AjaxJson;
import com.xplatform.base.framework.core.common.model.json.DataGrid;
import com.xplatform.base.framework.core.extend.hqlsearch.HqlGenerateUtil;
import com.xplatform.base.framework.tag.core.easyui.TagUtil;
import com.xplatform.base.orgnaization.user.vo.UserTypeVo;
import com.xplatform.base.platform.common.service.SysUserService;
import com.xplatform.base.platform.common.utils.ClientUtil;

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
@RequestMapping("/appFormUserShareController")
public class AppFormUserShareController extends BaseController {
	
	@Resource
	private AppFormUserShareService appFormUserShareService;
	@Resource
	private SysUserService sysUserService;
	private AjaxJson result = new AjaxJson();
	
	private String message;

	/**
	 * 字典类型管理列表页跳转
	 * @author xiehs
	 * @createtime 2014年5月24日 下午12:59:50
	 * @param request
	 * @return
	 */
	@RequestMapping(params = "appForm")
	public ModelAndView appForm(HttpServletRequest request) {
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
	public void datagrid(AppFormUserShareEntity appForm,HttpServletRequest request, HttpServletResponse response, DataGrid dataGrid) {
		CriteriaQuery cq = new CriteriaQuery(AppFormUserShareEntity.class, dataGrid);
		//查询条件组装器
		HqlGenerateUtil.installHql(cq, appForm, request.getParameterMap());
		cq.add();
		this.appFormUserShareService.getDataGridReturn(cq, true);
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
	public AjaxJson delete(AppFormUserShareEntity AppForm, HttpServletRequest request) {
		String ids = request.getParameter("ids");
		appFormUserShareService.deleteEntityByIds(ids);
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
	@RequestMapping(params = "appFormEdit")
	public ModelAndView appFormEdit(AppFormUserShareEntity appForm,Model model) {
		if (StringUtil.isNotEmpty(appForm.getId())) {
			appForm = appFormUserShareService.get(appForm.getId());
			model.addAttribute("AppForm", appForm);
		}
		return new ModelAndView("platform/system/dict/AppFormEdit");
	}
	
	/**
	 * 保存或删除from中用户id
	 * @author lixt
	 * @createtime 2015年11月03日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "saveOrUpdate")
	@ResponseBody
	public AjaxJson saveOrUpdate(AppFormUserShareEntity appForm, HttpServletRequest request) {
		String users=request.getParameter("users");
		String formId=request.getParameter("formId");
		List<UserTypeVo> userList=sysUserService.getMulUsers(users);
		try {
			if(userList!=null && userList.size()>0){
				this.appFormUserShareService.updateUsers(formId, ClientUtil.getUserId(), userList);
			}
		} catch (Exception e) {
			result.setSuccess(false);
			result.setMsg("分享失败");
		}
		
		return result;
	}

	/**
	 * 保存或删除from中用户id
	 * @author lixt
	 * @createtime 2015年11月03日 下午1:19:16
	 * @param AppForm
	 * @return
	 * @throws BusinessException 
	 */
	@RequestMapping(params = "queryShareForm")
	@ResponseBody
	public AjaxJson queryShareForm(AppFormUserShareEntity appForm, HttpServletRequest request) throws BusinessException {
		List<Map<String,Object>> list = this.appFormUserShareService.queryShareFormList(ClientUtil.getUserId());
		result.setObj(list);
		result.setMsg("查询成功");
		result.setSuccess(true);
		return result;
	}
	
	@RequestMapping(params = "queryShareUser")
	@ResponseBody
	public AjaxJson queryShareUser(AppFormUserShareEntity appForm, HttpServletRequest request) throws BusinessException {
		String formId=request.getParameter("formId");
		List<AppFormUserShareEntity> list = this.appFormUserShareService.queryShareUser(ClientUtil.getUserId(),formId);
		List<UserTypeVo> userList=new ArrayList<UserTypeVo>();
		for(AppFormUserShareEntity userShare:list){
			UserTypeVo user=new UserTypeVo();
			user.setId(userShare.getUserId());
			user.setType(userShare.getType());
			user.setName(userShare.getName());
			userList.add(user);
		}
		result.setObj(userList);
		result.setMsg("查询成功");
		result.setSuccess(true);
		return result;
	}
	
}
